package com.example.easymart.di

import com.example.easymart.data.local.dao.AddressDao
import com.example.easymart.data.local.dao.CartDao
import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.ProductDao
import com.example.easymart.data.local.dao.WalletDao
import com.example.easymart.data.local.datasource.CartLocalDataSource
import com.example.easymart.data.local.datasource.ProductLocalDataSource
import com.example.easymart.data.remote.api.AlgoliaApi
import com.example.easymart.data.remote.api.LocationApi
import com.example.easymart.data.remote.api.PaymentApi
import com.example.easymart.data.remote.api.ProductApi
import com.example.easymart.data.remote.datasource.AddressRemoteDataSource
import com.example.easymart.data.remote.datasource.CartRemoteDataSource
import com.example.easymart.data.remote.datasource.FirestoreAddressRemoteDataSource
import com.example.easymart.data.remote.datasource.FirestoreCartRemoteDataSource
import com.example.easymart.data.remote.datasource.ProductRemoteDataSource
import com.example.easymart.data.repositoryimpl.AddressRepositoryImpl
import com.example.easymart.data.repositoryimpl.CartRepositoryImpl
import com.example.easymart.data.repositoryimpl.FirebaseAuthRepositoryImpl
import com.example.easymart.data.repositoryimpl.LocationRepositoryImpl
import com.example.easymart.data.repositoryimpl.OrderRepositoryImpl
import com.example.easymart.data.repositoryimpl.PaymentRepositoryImpl
import com.example.easymart.data.repositoryimpl.ProductRepositoryImpl
import com.example.easymart.data.repositoryimpl.SearchRepositoryImpl
import com.example.easymart.domain.payment.process.CODProcessor
import com.example.easymart.domain.payment.process.EWalletProcessor
import com.example.easymart.domain.payment.process.OnlineGatewayProcessor
import com.example.easymart.domain.repository.AddressRepository
import com.example.easymart.domain.repository.AuthRepository
import com.example.easymart.domain.repository.CartRepository
import com.example.easymart.domain.repository.LocationRepository
import com.example.easymart.domain.repository.OrderRepository
import com.example.easymart.domain.repository.PaymentRepository
import com.example.easymart.domain.repository.ProductRepository
import com.example.easymart.domain.repository.SearchRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideProductLocalDataSource(dao: ProductDao): ProductLocalDataSource =
        ProductLocalDataSource(dao)

    @Provides
    @Singleton
    fun provideProductRemoteDataSource(api: ProductApi): ProductRemoteDataSource =
        ProductRemoteDataSource(api)

    @Provides
    @Singleton
    fun provideProductRepository(
        localDS: ProductLocalDataSource,
        remoteDS: ProductRemoteDataSource
    ): ProductRepository = ProductRepositoryImpl(localDS, remoteDS)

    @Provides
    @Singleton
    fun provincesLocationRepository(api: LocationApi): LocationRepository =
        LocationRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideCartRemoteDataSource(firestore: FirebaseFirestore): CartRemoteDataSource =
        FirestoreCartRemoteDataSource(firestore)

    @Provides
    @Singleton
    fun provideCartRepository(
        dao: CartDao,
        localDS: CartLocalDataSource,
        remoteDS: CartRemoteDataSource
    ): CartRepository = CartRepositoryImpl(dao, localDS, remoteDS)

    @Provides
    @Singleton
    fun provideAddressRemoteDataSource(firestore: FirebaseFirestore): AddressRemoteDataSource =
        FirestoreAddressRemoteDataSource(firestore)

    @Provides
    @Singleton
    fun provideAddressRepository(
        dao: AddressDao,
        remoteDS: AddressRemoteDataSource
    ): AddressRepository = AddressRepositoryImpl(dao, remoteDS)

    @Provides
    @Singleton
    fun provideSearchRepository(
        backendApi: AlgoliaApi,
        @Named("searchProductGson") gson: Gson
    ): SearchRepository = SearchRepositoryImpl(backendApi, gson)

    @Provides
    @Singleton
    fun providePaymentRepository(
        orderDao: OrderDao,
        walletDao: WalletDao,
        codProc: CODProcessor,
        paymentApi: PaymentApi,
        eWalletProc: EWalletProcessor,
        onlineGatewayProc: OnlineGatewayProcessor
    ): PaymentRepository =
        PaymentRepositoryImpl(orderDao, walletDao, paymentApi,  codProc,eWalletProc,onlineGatewayProc)

    @Provides
    @Singleton
    fun provideOrderRepository(dao: OrderDao): OrderRepository = OrderRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): AuthRepository = FirebaseAuthRepositoryImpl(auth, firestore)

}