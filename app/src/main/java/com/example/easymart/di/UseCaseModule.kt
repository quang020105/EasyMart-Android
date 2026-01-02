package com.example.easymart.di

import com.example.easymart.domain.repository.AddressRepository
import com.example.easymart.domain.repository.CartRepository
import com.example.easymart.domain.repository.LocationRepository
import com.example.easymart.domain.repository.PaymentRepository
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.domain.repository.SearchRepository
import com.example.easymart.domain.usecase.address.GetAllAddressUseCase
import com.example.easymart.domain.usecase.address.GetDefaultAddressUseCase
import com.example.easymart.domain.usecase.address.InsertNewAddressUseCase
import com.example.easymart.domain.usecase.cart.AddToCartUseCase
import com.example.easymart.domain.usecase.cart.ClearAllCartsUseCase
import com.example.easymart.domain.usecase.cart.GetAllCartItemsUseCase
import com.example.easymart.domain.usecase.cart.UpdateCartQuantityUseCase
import com.example.easymart.domain.usecase.location.GetDistrictsUseCase
import com.example.easymart.domain.usecase.location.GetProvincesUseCase
import com.example.easymart.domain.usecase.location.GetWardsUseCase
import com.example.easymart.domain.usecase.payment.GetWalletBalanceUseCase
import com.example.easymart.domain.usecase.payment.ProcessPaymentUseCase
import com.example.easymart.domain.usecase.product.GetAllProductUseCase
import com.example.easymart.domain.usecase.product.GetProductUseCase
import com.example.easymart.domain.usecase.search.GetSuggestionUseCase
import com.example.easymart.domain.usecase.search.SearchProductUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetProductsUseCase(productRepo: ProductRepository): GetAllProductUseCase =
        GetAllProductUseCase(productRepo)

    @Provides
    fun provideGetCartItemsUseCase(cartRepo: CartRepository): GetAllCartItemsUseCase =
        GetAllCartItemsUseCase(cartRepo)

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
}