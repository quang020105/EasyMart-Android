package com.example.easymart.di

import com.example.easymart.domain.repository.AddressRepository
import com.example.easymart.domain.repository.CartRepository
import com.example.easymart.domain.repository.LocationRepository
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
    @Singleton
    fun provideGetProductsUseCase(productRepo: ProductRepository): GetAllProductUseCase =
        GetAllProductUseCase(productRepo)

    @Provides
    @Singleton
    fun provideGetCartItemsUseCase(cartRepo: CartRepository): GetAllCartItemsUseCase =
        GetAllCartItemsUseCase(cartRepo)

    @Provides
    @Singleton
    fun provideAddToCartUseCase(cartRepo: CartRepository): AddToCartUseCase =
        AddToCartUseCase(cartRepo)

    @Provides
    @Singleton
    fun provideUpdateQuantityUseCase(cartRepo: CartRepository): UpdateCartQuantityUseCase =
        UpdateCartQuantityUseCase(cartRepo)

    @Provides
    @Singleton
    fun provideClearAllCartItemsUseCase(cartRepo: CartRepository): ClearAllCartsUseCase =
        ClearAllCartsUseCase(cartRepo)

    @Provides
    @Singleton
    fun provideSearchProductUseCase(searchRepo: SearchRepository): SearchProductUseCase =
        SearchProductUseCase(searchRepo)

    @Provides
    @Singleton
    fun provideGetSuggestionsUseCase(searchRepo: SearchRepository): GetSuggestionUseCase =
        GetSuggestionUseCase(searchRepo)

    @Provides
    @Singleton
    fun provideGetProductUseCase(productRepo: ProductRepository): GetProductUseCase =
        GetProductUseCase(productRepo)

    @Provides
    @Singleton
    fun provideGetAddressesUseCase(addressRepo: AddressRepository): GetAllAddressUseCase =
        GetAllAddressUseCase(addressRepo)

    @Provides
    @Singleton
    fun provideInsertAddressesUseCase(addressRepo: AddressRepository): InsertNewAddressUseCase =
        InsertNewAddressUseCase(addressRepo)

    @Provides
    @Singleton
    fun provideGetDefaultAddressUseCase(addressRepo: AddressRepository): GetDefaultAddressUseCase =
        GetDefaultAddressUseCase(addressRepo)


    @Provides
    @Singleton
    fun provideGetProvincesUseCase(locationRepo: LocationRepository): GetProvincesUseCase =
        GetProvincesUseCase(locationRepo)

    @Provides
    @Singleton
    fun provideGetDistrictsUseCase(locationRepo: LocationRepository): GetDistrictsUseCase =
        GetDistrictsUseCase(locationRepo)

    @Provides
    @Singleton
    fun provideGetWardsUseCase(locationRepo: LocationRepository): GetWardsUseCase =
        GetWardsUseCase(locationRepo)
}