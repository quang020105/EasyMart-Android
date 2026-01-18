package com.example.easymart.di

import com.example.easymart.data.local.dao.AddressDao
import com.example.easymart.data.local.dao.CartDao
import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.WalletDao
import com.example.easymart.data.remote.api.BackendApi
import com.example.easymart.data.remote.api.LocationApi
import com.example.easymart.data.remote.api.ProductApi
import com.example.easymart.data.repositoryimpl.AddressRepositoryImpl
import com.example.easymart.data.repositoryimpl.CartRepositoryImpl
import com.example.easymart.data.repositoryimpl.LocationRepositoryImpl
import com.example.easymart.data.repositoryimpl.OrderRepositoryImpl
import com.example.easymart.data.repositoryimpl.PaymentRepositoryImpl
import com.example.easymart.data.repositoryimpl.ProductRepositoryImpl
import com.example.easymart.data.repositoryimpl.SearchRepositoryImpl
import com.example.easymart.domain.payment.process.CODProcesser
import com.example.easymart.domain.payment.process.EWalletProcessor
import com.example.easymart.domain.payment.process.OnlineGatewayProcessor
import com.example.easymart.domain.repository.AddressRepository
import com.example.easymart.domain.repository.CartRepository
import com.example.easymart.domain.repository.LocationRepository
import com.example.easymart.domain.repository.OrderRepository
import com.example.easymart.domain.repository.PaymentRepository
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.domain.repository.SearchRepository
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideProductRepository(api: ProductApi): ProductRepository = ProductRepositoryImpl(api)

    @Provides
    @Singleton
    fun provincesLocationRepository(api: LocationApi): LocationRepository =
        LocationRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideCartRepository(dao: CartDao): CartRepository = CartRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAddressRepository(dao: AddressDao): AddressRepository = AddressRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideSearchRepository(
        backendApi: BackendApi,
        @Named("searchProductGson") gson: Gson
    ): SearchRepository = SearchRepositoryImpl(backendApi, gson)

    @Provides
    @Singleton
    fun providePaymentRepository(
        orderDao: OrderDao,
        walletDao: WalletDao,
        codProc: CODProcesser,
        eWalletProc: EWalletProcessor,
        onlineGatewayProc: OnlineGatewayProcessor
    ): PaymentRepository =
        PaymentRepositoryImpl(orderDao, walletDao, codProc, eWalletProc, onlineGatewayProc)

    @Provides
    @Singleton
    fun provideOrderRepository(dao: OrderDao): OrderRepository = OrderRepositoryImpl(dao)

}