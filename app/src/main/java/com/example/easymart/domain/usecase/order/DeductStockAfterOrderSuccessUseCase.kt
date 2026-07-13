package com.example.easymart.domain.usecase.order

import com.example.easymart.domain.repository.OrderRepository
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import javax.inject.Inject

class DeductStockAfterOrderSuccessUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(orderId: Int): Resource<Unit> {
        val order = orderRepository.getOrderById(orderId)
            ?: return Resource.Error("Không tìm thấy đơn hàng")

        if (order.stockDeducted) {
            return Resource.Success(Unit)
        }

        return when (val result = productRepository.deductStockForOrder(order)) {
            is Resource.Success -> {
                orderRepository.markStockDeducted(orderId)
                Resource.Success(Unit)
            }
            is Resource.Error -> result
            is Resource.Loading -> Resource.Loading
        }
    }
}
