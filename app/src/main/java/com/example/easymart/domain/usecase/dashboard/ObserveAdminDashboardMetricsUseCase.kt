package com.example.easymart.domain.usecase.dashboard

import com.example.easymart.domain.dashboard.DashboardMetricsCalculator
import com.example.easymart.domain.model.AdminDashboardMetrics
import com.example.easymart.domain.model.DashboardPeriod
import com.example.easymart.domain.repository.OrderRepository
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.presentation.common.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObserveAdminDashboardMetricsUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val productRepository: ProductRepository,
    private val calculator: DashboardMetricsCalculator
) {
    operator fun invoke(period: DashboardPeriod): Flow<Resource<AdminDashboardMetrics>> {
        return combine(
            orderRepository.observeAllOrdersForAdmin(),
            productRepository.observeAllProductsForAdmin()
        ) { orders, productResult ->
            when (productResult) {
                is Resource.Success -> Resource.Success(
                    calculator.calculate(
                        orders = orders,
                        products = productResult.data,
                        period = period
                    )
                )
                is Resource.Error -> Resource.Error(productResult.message)
                Resource.Loading -> Resource.Loading
            }
        }.catch { error ->
            emit(Resource.Error(error.message ?: "Không thể tải thống kê quản trị"))
        }
    }
}
