package com.example.easymart.presentation.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.auth.ObserveCurrentUserUseCase
import com.example.easymart.domain.usecase.cart.AddToCartUseCase
import com.example.easymart.domain.usecase.cart.ClearAllCartsUseCase
import com.example.easymart.domain.usecase.cart.ObserveCartUseCase
import com.example.easymart.domain.usecase.cart.SyncCartUseCase
import com.example.easymart.domain.usecase.cart.UpdateCartQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val observeCartUS: ObserveCartUseCase,
    private val clearAllUS: ClearAllCartsUseCase,
    private val addToCartUS: AddToCartUseCase,
    private val updateQuantityUS: UpdateCartQuantityUseCase,
    private val observeCurrentUserUS: ObserveCurrentUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val syncCartUseCase: SyncCartUseCase
) : ViewModel() {
    //phí ship mặc định
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CartEvent>()
    val event = _event.asSharedFlow()

    // Lưu danh sách productIds đã chọn khi vào checkout (để restore sau khi login)
    private var pendingSelectedProductIds: Set<Int>? = null

    init {
        observeCart()
        observeCheckAll()
    }

    private fun observeCheckAll() {
        viewModelScope.launch {
            uiState.collect { state ->
                val allChecked = state.items.isNotEmpty() && state.items.all { it.isChecked }
                if (state.checkedAll != allChecked) {
                    _uiState.update { it.copy(checkedAll = allChecked) }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCart() {
        viewModelScope.launch {
            //lấy user hiện tại rồi mới lấy giỏ hàng
            observeCurrentUserUS()
                .flatMapLatest { user ->
                    observeCartUS(user?.id)
                }
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .onEach {
                    _uiState.update { it.copy(isLoading = false) }
                }
                .collect { items ->
                    //tránh reset lại check đã chọn khi reset UI do room cập nhật
                    _uiState.update { state ->
                        val updatedItems = items.map { domain ->
                            val prev = state.items.find { it.product.id == domain.product.id }
                            
                            // Ưu tiên restore từ pendingSelectedProductIds (sau khi login)
                            // Nếu không có pendingSelectedProductIds, giữ lại trạng thái checked cũ
                            val shouldBeChecked = when {
                                pendingSelectedProductIds != null -> {
                                    // Nếu có pendingSelectedProductIds, restore dựa trên đó
                                    pendingSelectedProductIds!!.contains(domain.product.id)
                                }
                                prev != null -> {
                                    // Giữ lại trạng thái checked cũ nếu item đã tồn tại
                                    prev.isChecked
                                }
                                else -> {
                                    // Mặc định không checked cho item mới
                                    false
                                }
                            }
                            
                            domain.copy(isChecked = shouldBeChecked)
                        }
                        
                        // Chỉ clear pendingSelectedProductIds khi đã restore thành công
                        // (tức là tất cả các productIds trong pendingSelectedProductIds đã có trong items)
                        if (pendingSelectedProductIds != null && items.isNotEmpty()) {
                            val currentProductIds = items.map { it.product.id }.toSet()
                            val allRestored = pendingSelectedProductIds!!.all { productId ->
                                currentProductIds.contains(productId)
                            }
                            
                            if (allRestored) {
                                Log.d("CartViewModel", "All selected items restored, clearing pendingSelectedProductIds")
                                pendingSelectedProductIds = null
                            } else {
                                val missingIds = pendingSelectedProductIds!!.filter { !currentProductIds.contains(it) }
                                Log.d("CartViewModel", "Waiting for items to be merged. Missing productIds: $missingIds")
                            }
                        }
                        
                        state.copy(items = updatedItems)
                    }
                }
        }
    }

    fun addProductToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            val userId = getCurrentUserUseCase()?.id
            runCatching {
                val newCartItem = CartItem(
                    id = product.id,
                    product = product,
                    quantity = quantity,
                    unitPriceVnd = product.priceVnd,
                )
                addToCartUS(userId, newCartItem)
            }.onSuccess {
                _event.emit(CartEvent.ShowMessage("Đã thêm vào giỏ hàng"))
            }.onFailure {
                _event.emit(CartEvent.ShowMessage("Không thể thêm sản phẩm"))
            }
        }
    }

    fun updateQuantity(cartItem: CartItem, delta: Int) {
        if (delta < 0 && cartItem.quantity <= 1) {
            _uiState.update { state ->
                state.copy(pendingRemoveItem = cartItem)
            }
            return
        }

        viewModelScope.launch {
            runCatching {
                updateQuantityUS(cartItem, delta)
            }.onFailure {
                _event.emit(CartEvent.ShowMessage("Không thể cập nhật số lượng"))
                //Log.e("CartViewModelError", "updateQuantity: ${it.message}")
            }
        }
    }

    fun confirmRemovePendingItem() {
        val target = _uiState.value.pendingRemoveItem ?: return
        viewModelScope.launch {
            runCatching {
                updateQuantityUS(target, -target.quantity)
            }.onSuccess {
                _uiState.update { it.copy(pendingRemoveItem = null) }
                _event.emit(CartEvent.ShowMessage("Đã xóa sản phẩm"))
            }.onFailure {
                _event.emit(CartEvent.ShowMessage("Không thể xóa sản phẩm"))
                //Log.e("CartViewModelError", "confirmRemovePendingItem: ${it.message}")
            }
        }
    }

    fun cancelRemovePendingItem() {
        _uiState.update { it.copy(pendingRemoveItem = null) }
    }

    fun requestRemoveSelectedItems() {
        val hasSelected = _uiState.value.selectedItems.isNotEmpty()
        if (!hasSelected) {
            viewModelScope.launch {
                _event.emit(CartEvent.ShowMessage("Chưa có sản phẩm nào được chọn"))
            }
            return
        }
        _uiState.update { it.copy(pendingRemoveSelected = true) }
    }

    fun cancelRemoveSelectedItems() {
        _uiState.update { it.copy(pendingRemoveSelected = false) }
    }

    fun confirmRemoveSelectedItems() {
        val selectedItems = _uiState.value.selectedItems
        if (selectedItems.isEmpty()) {
            _uiState.update { it.copy(pendingRemoveSelected = false) }
            return
        }
        viewModelScope.launch {
            runCatching {
                selectedItems.forEach { item ->
                    updateQuantityUS(item, -item.quantity)
                }
            }.onSuccess {
                _uiState.update { it.copy(pendingRemoveSelected = false) }
                _event.emit(CartEvent.ShowMessage("Đã xóa ${selectedItems.size} sản phẩm"))
            }.onFailure {
                _event.emit(CartEvent.ShowMessage("Không thể xóa các sản phẩm đã chọn"))
            }
        }
    }

    fun onCheckChanged(id: Int, isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                items = state.items.map {
                    if (it.id == id) it.copy(isChecked = isChecked) else it
                }
            )
        }
    }

    fun onCheckedChangeAll(isChecked: Boolean) {
        _uiState.update { state ->
            state.copy(
                items = state.items.map { it.copy(isChecked = isChecked) },
                checkedAll = isChecked
            )
        }
    }

    /**
     * Lưu danh sách sản phẩm đã chọn khi vào checkout
     * (để restore sau khi login thành công)
     */
    fun saveSelectedItemsForCheckout() {
        val selectedProductIds = _uiState.value.items
            .filter { it.isChecked }
            .map { it.product.id }
            .toSet()
        
        if (selectedProductIds.isNotEmpty()) {
            pendingSelectedProductIds = selectedProductIds
            //Log.d("CartViewModel", "Saved selected items for checkout: $selectedProductIds")
        }
    }

    /**
     * Restore lại trạng thái checked cho các sản phẩm đã chọn sau khi login
     * Method này được gọi từ CheckOutRoute để đảm bảo restore ngay khi vào checkout
     * Không clear pendingSelectedProductIds ở đây, để observeCart có thể restore lại nếu items chưa đầy đủ
     */

    //chưa test
    fun restoreSelectedItemsAfterLogin() {
        val pendingIds = pendingSelectedProductIds ?: return
        val currentItems = _uiState.value.items
        
        if (currentItems.isEmpty()) {
            return
        }
        
        val currentProductIds = currentItems.map { it.product.id }.toSet()
        val itemsToRestore = pendingIds.filter { currentProductIds.contains(it) }
        
        if (itemsToRestore.isNotEmpty()) {
            _uiState.update { state ->
                state.copy(
                    items = state.items.map { item ->
                        if (pendingIds.contains(item.product.id)) {
                            item.copy(isChecked = true)
                        } else {
                            item
                        }
                    }
                )
            }
        }
    }


    //đồng bộ giỏ hàng
    fun syncCart(userId: String?) {
        if (userId == null) return

        viewModelScope.launch {
            syncCartUseCase(userId)
        }
    }
}
