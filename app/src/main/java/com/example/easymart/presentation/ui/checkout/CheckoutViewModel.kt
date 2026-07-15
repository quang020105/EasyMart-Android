package com.example.easymart.presentation.ui.checkout

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.easymart.data.mapper.cartToOrderItem
import com.example.easymart.domain.model.Address
import com.example.easymart.domain.model.CartItem
import com.example.easymart.domain.model.Order
import com.example.easymart.domain.model.OrderStatus
import com.example.easymart.domain.model.PaymentMethod
import com.example.easymart.domain.model.PaymentResult
import com.example.easymart.domain.model.PaymentStatus
import com.example.easymart.domain.usecase.auth.ObserveCurrentUserUseCase
import com.example.easymart.domain.usecase.cart.RemovePurchasedCartItemsUseCase
import com.example.easymart.domain.usecase.order.DeductStockAfterOrderSuccessUseCase
import com.example.easymart.domain.usecase.order.OrderAutoProcessUseCase
import com.example.easymart.domain.usecase.order.SyncOrderUseCase
import com.example.easymart.domain.usecase.payment.ProcessPaymentUseCase
import com.example.easymart.domain.usecase.payment.PollPayOsPaymentStatusUseCase
import com.example.easymart.domain.usecase.payment.UpdateLocalOrderPaymentStatusUseCase
import com.example.easymart.domain.usecase.product.GetProductUseCase
import com.example.easymart.presentation.common.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val processPaymentUseCase: ProcessPaymentUseCase,
    private val orderAutoProcessUseCase: OrderAutoProcessUseCase,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val pollPayOsPaymentStatusUseCase: PollPayOsPaymentStatusUseCase,
    private val updateLocalOrderPaymentStatusUseCase: UpdateLocalOrderPaymentStatusUseCase,
    private val syncOrderUseCase: SyncOrderUseCase,
    private val getProductUseCase: GetProductUseCase,
    private val deductStockAfterOrderSuccessUseCase: DeductStockAfterOrderSuccessUseCase,
    private val removePurchasedCartItemsUseCase: RemovePurchasedCartItemsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState

    private val _uiEvent = MutableSharedFlow<CheckoutUiEvent?>()
    val uiEvent: SharedFlow<CheckoutUiEvent?> = _uiEvent

    init {
        observeCurrentUser()
    }

    private fun observeCurrentUser() {
        viewModelScope.launch {
            observeCurrentUserUseCase().collect { user ->
                _uiState.update {
                    it.copy(currentUserId = user?.id)
                }
            }
        }
    }

    fun pay(
        cartItems: List<CartItem>,
        address: Address?,
        paymentMethod: PaymentMethod?
    ) {

        val userId = uiState.value.currentUserId
        if (userId == null) {
            viewModelScope.launch {
                _uiEvent.emit(
                    CheckoutUiEvent.ShowErrorMessage("Bạn cần đăng nhập để thanh toán")
                )
            }
            return
        }

        Log.d("CheckoutViewModel", "$address, $paymentMethod")
        if (address == null) {
            viewModelScope.launch {
                _uiEvent.emit(CheckoutUiEvent.ShowErrorMessage(message = "Vui lòng chọn địa chỉ"))
            }
            return
        }
        if (paymentMethod == null) {
            viewModelScope.launch {
                _uiEvent.emit(CheckoutUiEvent.ShowErrorMessage(message = "Vui lòng chọn phương thức thanh toán"))
            }
            return
        }

        val shippingAddress = Address(
            name = address.name,
            phone = address.phone,
            addressString = address.addressString,
        )
        val order = Order(
            userId = userId, //getCurrentUserIdUseCase() chưa xử lý
            orderNumber = "",//chưa xử lý
            items = cartItems.map { it.cartToOrderItem() },
            totalAmount = cartItems.sumOf { it.totalPrice }.toLong(),
            status = OrderStatus.CREATED,
            paymentStatus = PaymentStatus.UNPAID,
            paymentMethod = paymentMethod,
            shippingAddress = shippingAddress,
            createdAt = System.currentTimeMillis()
        )

        for ((index, item) in cartItems.withIndex()) {
            Log.d(
                "CheckoutViewModel",
                "Item #$index | id=${item.id} | name=${item.product.name} | qty=${item.quantity} | " +
                        "price=${item.price} | totalPrice=${item.totalPrice}"
            )
        }



        viewModelScope.launch {
            //gọi usecase để xử lý thanh toán
            processPaymentUseCase(order, paymentMethod)
                //onStart để cập nhật trạng thái xử lý trước khi bắt đầu thu thập kết quả
                .onStart {
                    _uiState.update { it.copy(isProcessing = true) }
                }
                .collect { result ->
                    Log.d("CheckoutViewModel", "Payment result: $result")
                    when (result) {
                        is PaymentResult.Pending -> {
                            _uiState.update { it.copy(isProcessing = true) }
                        }

                        is PaymentResult.Redirect -> {
                            _uiState.update { it.copy(isProcessing = false) }
                            _uiEvent.emit(
                                CheckoutUiEvent.NavigateToOnlineProcessing(
                                    checkoutUrl = result.deeplink,
                                    orderCode = result.orderCode,
                                    localOrderId = result.localOrderId
                                )
                            )
                        }

                        is PaymentResult.Processing -> {
                            _uiState.update {
                                it.copy(isProcessing = true)
                            }
                        }

                        is PaymentResult.Success -> {
                            val stockUpdated = deductStockAfterSuccessfulOrder(result.orderId)
                            if (!stockUpdated) {
                                _uiState.update { it.copy(isProcessing = false) }
                                return@collect
                            }

                            val order = order.copy(id = result.orderId, stockDeducted = true)
                            _uiState.update {
                                it.copy(isProcessing = false, order = order)
                            }

                            //xóa sản phẩm trong giỏ hàng sau khi đặt hàng thành công
                            removePurchasedCartItemsUseCase(cartItems)

                            if (paymentMethod == PaymentMethod.ONLINE_GATEWAY) {
                                _uiEvent.emit(
                                    CheckoutUiEvent.NavigateToOnlineProcessing(
                                        checkoutUrl = null,
                                        orderCode = null,
                                        localOrderId = result.orderId
                                    )
                                )
                            } else {
                                _uiEvent.emit(
                                    CheckoutUiEvent.NavigateToSuccess
                                )
                                syncOrderInBackground(result.orderId)
                            }
                        }

                        is PaymentResult.Failed -> {
                            _uiState.update { it.copy(isProcessing = false) }
                            if (paymentMethod == PaymentMethod.ONLINE_GATEWAY) {
                                _uiEvent.emit(
                                    CheckoutUiEvent.NavigateToPaymentFailed(
                                        result.orderID,
                                        result.reason
                                    )
                                )
                            } else {
                                _uiEvent.emit(CheckoutUiEvent.ShowErrorMessage(result.reason))
                            }
                        }
                    }
                }
        }
    }


    fun selectAddressClick() {
        viewModelScope.launch {
            _uiEvent.emit(CheckoutUiEvent.NavigateToSelectAddress)
        }
    }

    fun selectPaymentClick() {
        viewModelScope.launch {
            _uiEvent.emit(CheckoutUiEvent.NavigateSelectPaymentMethod)
        }
    }

    // gọi workManager để tự động chuyển trạng thái đơn hàng (nếu đặt hàng thành công)
    fun startAutoProcessOrder() {
        val order = uiState.value.order
        if (order != null) {
            viewModelScope.launch {
                Log.d("CheckoutViewModel", "startAutoProcessOrder: ${order.id}")
                orderAutoProcessUseCase.start(orderId = order.id)
            }
        }
    }

    suspend fun pollPayOsAndUpdate(orderCode: Long, localOrderId: Int): PaymentStatus {
        val status = pollPayOsPaymentStatusUseCase(orderCode)
        if (status == PaymentStatus.PAID || status == PaymentStatus.FAILED) {
            updateLocalOrderPaymentStatusUseCase(localOrderId, status)
        }
        if (status == PaymentStatus.PAID) {
            deductStockAfterSuccessfulOrder(localOrderId)
            syncOrderInBackground(localOrderId)
        }
        return status
    }

    suspend fun markPayOsCancelled(localOrderId: Int) {
        updateLocalOrderPaymentStatusUseCase(localOrderId, PaymentStatus.FAILED)
    }

    fun loadQuickOrder(productId: Int, quantity: Int) {
        viewModelScope.launch {
            getProductUseCase(productId).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val product = resource.data
                        val cartItem = CartItem(
                            id = product.id,
                            product = product,
                            quantity = quantity.coerceAtLeast(1),
                            price = product.price
                        )
                        _uiState.update {
                            it.copy(
                                quickOrderItems = listOf(cartItem),
                                isQuickOrderActive = true
                            )
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    fun clearQuickOrder() {
        _uiState.update { it.copy(quickOrderItems = emptyList(), isQuickOrderActive = false) }
    }

    private fun syncOrderInBackground(orderId: Int) {
        viewModelScope.launch {
            runCatching {
                syncOrderUseCase(orderId)
            }.onFailure { error ->
                Log.e("CheckoutViewModel", "syncOrder failed: $orderId", error)
            }
        }
    }

    private suspend fun deductStockAfterSuccessfulOrder(orderId: Int): Boolean {
        return when (val result = deductStockAfterOrderSuccessUseCase(orderId)) {
            is Resource.Success -> true
            is Resource.Error -> {
                val message = result.message ?: "Không thể cập nhật tồn kho"
                Log.e("CheckoutViewModel", "deductStockAfterSuccessfulOrder failed: $message")
                _uiEvent.emit(CheckoutUiEvent.ShowErrorMessage(message))
                false
            }
            is Resource.Loading -> false
        }
    }
}
