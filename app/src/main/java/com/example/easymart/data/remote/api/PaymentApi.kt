package com.example.easymart.data.remote.api

import com.example.easymart.data.remote.dto.payment.CreateOrderRequest
import com.example.easymart.data.remote.dto.payment.CreateOrderResponse
import com.example.easymart.data.remote.dto.payment.InitPaymentResponse
import com.example.easymart.data.remote.dto.payment.PaymentStatusResponse
import com.example.easymart.data.remote.dto.payment.RegisterDeviceTokenRequest
import com.example.easymart.data.remote.dto.payment.RegisterDeviceTokenResponse
import com.example.easymart.data.remote.dto.payment.PayOsCreatePaymentRequest
import com.example.easymart.data.remote.dto.payment.PayOsCreatePaymentResponse
import com.example.easymart.data.remote.dto.payment.PayOsOrderStatusResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PaymentApi {
    @POST("orders")
    suspend fun createOrder(@Body body: CreateOrderRequest): CreateOrderResponse

    // existing init / status
    @POST("payments/{serverOrderId}/init")
    suspend fun initPayment(
        @Path("serverOrderId") serverOrderId: Int,
        @Query("provider") provider: String = "momo"
    ): InitPaymentResponse

    @GET("payments/{serverOrderId}/status")
    suspend fun getPaymentStatus(@Path("serverOrderId") serverOrderId: Int): PaymentStatusResponse

    // register FCM token for a user (backend stores token)
    @POST("devices/register")
    suspend fun registerDeviceToken(@Body req: RegisterDeviceTokenRequest): RegisterDeviceTokenResponse

    // PayOS backend (Node demo)
    @POST("create-payment")
    suspend fun createPayOsPayment(
        @Body body: PayOsCreatePaymentRequest
    ): PayOsCreatePaymentResponse

    // NEW: endpoint chuẩn cho polling
    @GET("payment-status/{orderCode}")
    suspend fun getPayOsPaymentStatus(
        @Path("orderCode") orderCode: Long
    ): PayOsOrderStatusResponse

    // giữ lại endpoint debug cũ
    @GET("order/{orderCode}")
    suspend fun getPayOsOrderStatus(
        @Path("orderCode") orderCode: Long
    ): PayOsOrderStatusResponse
}