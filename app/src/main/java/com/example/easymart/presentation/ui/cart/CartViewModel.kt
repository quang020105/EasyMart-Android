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
import com.example.easymart.domain.usecase.cart.UpdateCartQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
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
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    //phí ship mặc định
    private val shippingPerItem = 15000 * 1.0 / 26333

//    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
//    val cartItems = _cartItems.asStateFlow().stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
//        initialValue = emptyList()
//    )
//
//    val selectedItems = cartItems.map { cartItems -> cartItems.filter { it.isChecked } }
//
//    val subtotal: StateFlow<Double> = selectedItems.map { cartItemsChecked ->
//        cartItemsChecked.sumOf { it.totalPrice }
//    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)
//
//    val shipping: StateFlow<Double> = selectedItems.map { cartItemsChecked ->
//        cartItemsChecked.sumOf { (it.quantity * shippingPerItem).toDouble() }
//    }.stateIn(viewModelScope, SharingStarted.Eagerly,0.0)
//
//    val total: StateFlow<Double> = combine(subtotal, shipping){s,sh -> s + sh}
//        .stateIn(viewModelScope, SharingStarted.Eagerly,0.0)
//
//    private val _checkedAll = MutableStateFlow(false)
//    val checkedAll = _checkedAll.asStateFlow()

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    private val _event = MutableSharedFlow<CartEvent>()
    val event = _event.asSharedFlow()

    init {
        observeCart()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeCart() {
        viewModelScope.launch {
            //lấy user hiện tại rồi mới lấy giỏ hàng
            observeCurrentUserUS()
                .flatMapLatest { user ->
                    observeCartUS(user?.id)
                }
                .collect { items ->
                    //tránh reset lại check đã chọn khi reset UI do room cập nhật
                    _uiState.update { state ->
                        state.copy(
                            items = items.map { domain ->
                                val prev = state.items.find { it.id == domain.id }
                                domain.copy(isChecked = prev?.isChecked ?: false)
                            }
                        )
                    }
                }
//            observeCartUS(userId).collect { items ->
//                //tránh reset lại check đã chọn khi reset UI do room cập nhật
//                _uiState.update { state ->
//                    state.copy(
//                        items = items.map { domain ->
//                            val prev = state.items.find { it.id == domain.id }
//                            domain.copy(isChecked = prev?.isChecked ?: false)
//                        }
//                    )
//                }
//            }
        }
    }

    fun addProductToCart(product: Product, quantity: Int = 1) {
//        viewModelScope.launch {
//            val userId = getCurrentUserUseCase()?.id
//            try {
//                val newCartItem = CartItem(
//                    id = product.id,
//                    product = product,
//                    quantity = quantity,
//                    price = product.price,
//                )
//                addToCartUS(userId, newCartItem)
//                _event.emit(CartEvent.ShowMessage("Đã thêm vào giỏ hàng"))
//            } catch (e: Exception) {
//                _event.emit(CartEvent.ShowMessage("Không thể thêm sản phẩm vào giỏ hàng"))
//                Log.e("CartViewModelError", "${e.message}")
//            }
//        }

        viewModelScope.launch {
            val userId = getCurrentUserUseCase()?.id
            runCatching {
                val newCartItem = CartItem(
                    id = product.id,
                    product = product,
                    quantity = quantity,
                    price = product.price,
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
//        viewModelScope.launch {
//            try {
//                updateQuantityUS(cartItem, delta)
//            } catch (e: Exception) {
//                _event.emit(CartEvent.ShowMessage("Không thể cập nhật số lượng"))
//                Log.e("CartViewModelError", "updateQuantity: ${e.message}")
//            }
//        }
        viewModelScope.launch {
            runCatching {
                updateQuantityUS(cartItem, delta)
            }.onFailure {
                _event.emit(CartEvent.ShowMessage("Không thể cập nhật số lượng"))
                Log.e("CartViewModelError", "updateQuantity: ${it.message}")
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
}