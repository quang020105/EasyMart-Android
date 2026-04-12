package com.example.easymart.di

import android.content.Context
import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.domain.repository.AddressRepository
import com.example.easymart.domain.repository.AuthRepository
import com.example.easymart.domain.repository.CartRepository
import com.example.easymart.domain.repository.LocationRepository
import com.example.easymart.domain.repository.OrderRepository
import com.example.easymart.domain.repository.PaymentRepository
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.domain.repository.SearchRepository
import com.example.easymart.domain.usecase.address.GetAllAddressUseCase
import com.example.easymart.domain.usecase.address.GetDefaultAddressUseCase
import com.example.easymart.domain.usecase.address.InsertNewAddressUseCase
import com.example.easymart.domain.usecase.auth.GetCurrentUserUseCase
import com.example.easymart.domain.usecase.auth.LoginUseCase
import com.example.easymart.domain.usecase.auth.LogoutUseCase
import com.example.easymart.domain.usecase.auth.ObserveCurrentUserUseCase
import com.example.easymart.domain.usecase.auth.SendPasswordResetEmailUseCase
import com.example.easymart.domain.usecase.auth.SignUpUseCase
import com.example.easymart.domain.usecase.cart.AddToCartUseCase
import com.example.easymart.domain.usecase.cart.ClearAllCartsUseCase
import com.example.easymart.domain.usecase.cart.MergeGuestCartIntoUserUseCase
import com.example.easymart.domain.usecase.cart.ObserveCartUseCase
import com.example.easymart.domain.usecase.cart.SyncCartUseCase
import com.example.easymart.domain.usecase.cart.UpdateCartQuantityUseCase
import com.example.easymart.domain.usecase.location.GetDistrictsUseCase
import com.example.easymart.domain.usecase.location.GetProvincesUseCase
import com.example.easymart.domain.usecase.location.GetWardsUseCase
import com.example.easymart.domain.usecase.order.CancelOrderUseCase
import com.example.easymart.domain.usecase.order.GetObserveAllOrdersUseCase
import com.example.easymart.domain.usecase.order.GetOrderDetailUseCase
import com.example.easymart.domain.usecase.order.GetOrderItemUseCase
import com.example.easymart.domain.usecase.order.OrderAutoProcessUseCase
import com.example.easymart.domain.usecase.payment.GetWalletBalanceUseCase
import com.example.easymart.domain.usecase.payment.ProcessPaymentUseCase
import com.example.easymart.domain.usecase.payment.PollPayOsPaymentStatusUseCase
import com.example.easymart.domain.usecase.payment.UpdateLocalOrderPaymentStatusUseCase
import com.example.easymart.domain.usecase.product.GetAllProductUseCase
import com.example.easymart.domain.usecase.product.GetProductUseCase
import com.example.easymart.domain.usecase.search.GetSuggestionUseCase
import com.example.easymart.domain.usecase.search.SearchProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    //product
    @Provides
    fun provideGetProductsUseCase(productRepo: ProductRepository): GetAllProductUseCase =
        GetAllProductUseCase(productRepo)


    //cart

    @Provides
    fun provideGetCartItemsUseCase(cartRepo: CartRepository): ObserveCartUseCase =
        ObserveCartUseCase(cartRepo)

    @Provides
    fun provideAddToCartUseCase(cartRepo: CartRepository): AddToCartUseCase =
        AddToCartUseCase(cartRepo)

    @Provides
    fun provideUpdateQuantityUseCase(cartRepo: CartRepository): UpdateCartQuantityUseCase =
        UpdateCartQuantityUseCase(cartRepo)

    @Provides
    fun provideClearAllCartItemsUseCase(cartRepo: CartRepository): ClearAllCartsUseCase =
        ClearAllCartsUseCase(cartRepo)

    @Provides
    fun provideMergeCartUseCase(cartRepo: CartRepository): MergeGuestCartIntoUserUseCase =
        MergeGuestCartIntoUserUseCase(cartRepo)

    @Provides
    fun provideSyncCartUseCase(cartRepo: CartRepository): SyncCartUseCase =
        SyncCartUseCase(cartRepo)


    //search

    @Provides
    fun provideSearchProductUseCase(searchRepo: SearchRepository): SearchProductUseCase =
        SearchProductUseCase(searchRepo)

    @Provides
    fun provideGetSuggestionsUseCase(searchRepo: SearchRepository): GetSuggestionUseCase =
        GetSuggestionUseCase(searchRepo)

    @Provides
    fun provideGetProductUseCase(productRepo: ProductRepository): GetProductUseCase =
        GetProductUseCase(productRepo)

    @Provides
    fun provideGetAddressesUseCase(addressRepo: AddressRepository): GetAllAddressUseCase =
        GetAllAddressUseCase(addressRepo)

    @Provides
    fun provideInsertAddressesUseCase(addressRepo: AddressRepository): InsertNewAddressUseCase =
        InsertNewAddressUseCase(addressRepo)

    @Provides
    fun provideGetDefaultAddressUseCase(addressRepo: AddressRepository): GetDefaultAddressUseCase =
        GetDefaultAddressUseCase(addressRepo)


    //location

    @Provides
    fun provideGetProvincesUseCase(locationRepo: LocationRepository): GetProvincesUseCase =
        GetProvincesUseCase(locationRepo)

    @Provides
    fun provideGetDistrictsUseCase(locationRepo: LocationRepository): GetDistrictsUseCase =
        GetDistrictsUseCase(locationRepo)

    @Provides
    fun provideGetWardsUseCase(locationRepo: LocationRepository): GetWardsUseCase =
        GetWardsUseCase(locationRepo)

    //payment
    @Provides
    fun provideProcessPaymentUseCase(paymentRepo: PaymentRepository): ProcessPaymentUseCase =
        ProcessPaymentUseCase(paymentRepo)

    @Provides
    fun provideWalletBalanceUseCase(paymentRepo: PaymentRepository): GetWalletBalanceUseCase =
        GetWalletBalanceUseCase(paymentRepo)

    @Provides
    fun providePollPayOsPaymentStatusUseCase(paymentRepo: PaymentRepository): PollPayOsPaymentStatusUseCase =
        PollPayOsPaymentStatusUseCase(paymentRepo)

    @Provides
    fun provideUpdateLocalOrderPaymentStatusUseCase(paymentRepo: PaymentRepository): UpdateLocalOrderPaymentStatusUseCase =
        UpdateLocalOrderPaymentStatusUseCase(paymentRepo)


    //workManager giả lập xử lý đơn

    @Provides
    fun provideOrderAutoProcessUseCase(
        @ApplicationContext context: Context
    ): OrderAutoProcessUseCase = OrderAutoProcessUseCase(context)

    @Provides
    fun provideCancelUseUseCase(
        orderDao: OrderDao,
        orderAutoProcessUC: OrderAutoProcessUseCase
    ) = CancelOrderUseCase(orderDao, orderAutoProcessUC)


    // order

    @Provides
    fun provideGetObserveAllOrdersUseCase(
        orderRepo: OrderRepository
    ) = GetObserveAllOrdersUseCase(orderRepo)

    @Provides
    fun provideGetOrderItemUseCase(
        orderRepo: OrderRepository
    ) = GetOrderItemUseCase(orderRepo)

    @Provides
    fun provideGetOrderDetailUseCase(
        orderRepo: OrderRepository
    ) = GetOrderDetailUseCase(orderRepo)


    // auth

    @Provides
    fun provideSignUpUseCase(
        authRepo: AuthRepository
    ) = SignUpUseCase(authRepo)

    @Provides
    fun provideLoginUseCase(
        authRepo: AuthRepository
    ) = LoginUseCase(authRepo)

    @Provides
    fun provideLogoutUseCase(
        authRepo: AuthRepository
    ) = LogoutUseCase(authRepo)

    @Provides
    fun provideGetCurrentUserUseCase(
        authRepo: AuthRepository
    ) = GetCurrentUserUseCase(authRepo)

    @Provides
    fun provideObserveUserUseCase(authRepo: AuthRepository): ObserveCurrentUserUseCase =
        ObserveCurrentUserUseCase(authRepo)

    @Provides
    fun provideSendPasswordResetEmailUseCase(authRepo: AuthRepository) =
        SendPasswordResetEmailUseCase(authRepo)

}