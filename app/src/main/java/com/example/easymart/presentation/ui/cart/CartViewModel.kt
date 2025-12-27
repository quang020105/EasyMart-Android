package com.example.easymart.presentation.ui.cart

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.usecase.cart.AddToCartUseCase
import com.example.easymart.domain.usecase.cart.ClearAllCartsUseCase
import com.example.easymart.domain.usecase.cart.GetAllCartItemsUseCase
import com.example.easymart.domain.usecase.cart.UpdateCartQuantityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getAllCartItemsUS: GetAllCartItemsUseCase,
    private val clearAllUS: ClearAllCartsUseCase,
    private val addToCartUS: AddToCartUseCase,
    private val updateQuantityUS: UpdateCartQuantityUseCase
) : ViewModel() {
    //phí ship mặc định
    private val shippingPerItem = 15000*1.0 /26333

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems = _cartItems.asStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val selectedItems = cartItems.map { cartItems -> cartItems.filter { it.isChecked } }

    val subtotal: StateFlow<Double> = selectedItems.map { cartItemsChecked ->
        cartItemsChecked.sumOf { it.totalPrice }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0.0)

    val shipping: StateFlow<Double> = selectedItems.map { cartItemsChecked ->
        cartItemsChecked.sumOf { (it.quantity * shippingPerItem).toDouble() }
    }.stateIn(viewModelScope, SharingStarted.Eagerly,0.0)

    val total: StateFlow<Double> = combine(subtotal, shipping){s,sh -> s + sh}
        .stateIn(viewModelScope, SharingStarted.Eagerly,0.0)

    private val _checkedAll = MutableStateFlow(false)
    val checkedAll = _checkedAll.asStateFlow()

    private val _event = MutableSharedFlow<CartEvent>()
    val event = _event.asSharedFlow()

    init {
        fetchCartItems()
    }

    private fun fetchCartItems() {
        viewModelScope.launch {
            getAllCartItemsUS().collect{ fetchedCartItems ->
                //tránh reset lại check đã chọn khi reset UI do room cập nhật
                val current = _cartItems.value
                val merged = fetchedCartItems.map { item ->
                    val prev = current.find { it.id == item.id }
                    if(prev != null){
                        item.copy(isChecked = prev.isChecked)
                    } else{
                        item
                    }
                }
                _cartItems.value = merged
            }
        }
    }

    fun addProductToCart(product: Product, quantity: Int = 1) {
        viewModelScope.launch {
            try {
                val newCartItem = CartItem(
                    id = product.id,
                    product = product,
                    quantity = quantity,
                    price = product.price,
                )
                addToCartUS(newCartItem)
                _event.emit(CartEvent.ShowMessage("Đã thêm vào giỏ hàng"))
            } catch (e: Exception) {
                _event.emit(CartEvent.ShowMessage("Không thể thêm sản phẩm vào giỏ hàng"))
                Log.e("CartViewModelError", "${e.message}")
            }
        }
    }

    fun updateQuantity(cartItem: CartItem, delta: Int) {
        viewModelScope.launch {
            try {
                updateQuantityUS(cartItem, delta)
            } catch (e: Exception) {
                _event.emit(CartEvent.ShowMessage("Không thể cập nhật số lượng"))
                Log.e("CartViewModelError", "updateQuantity: ${e.message}")
            }
        }
    }

    fun onCheckChanged(id: Int, isChecked: Boolean) {
        _cartItems.update { cartItems ->
            cartItems.map { item ->
                if(item.id == id){
                    item.copy(isChecked = isChecked)
                } else {
                    item
                }
            }
        }
    }

    fun onCheckedChangeAll(isChecked: Boolean){
        _cartItems.update {cartItems ->
            cartItems.map { item ->
                item.copy(isChecked = isChecked)
            }
        }
        _checkedAll.value = isChecked
    }
}