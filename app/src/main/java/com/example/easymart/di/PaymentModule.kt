package com.example.easymart.di

import com.example.easymart.data.local.dao.OrderDao
import com.example.easymart.data.local.dao.PaymentDao
import com.example.easymart.data.local.dao.WalletDao
import com.example.easymart.domain.payment.process.CODProcesser
import com.example.easymart.domain.payment.process.EWalletProcessor
import com.example.easymart.domain.payment.process.OnlineGatewayProcessor
import com.example.easymart.domain.payment.simulator.EWalletSimulator
import com.example.easymart.domain.payment.simulator.FakeGateway
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PaymentModule {
    @Singleton
    @Provides
    fun provideEWalletSimulator(): EWalletSimulator = EWalletSimulator()

    @Singleton
    @Provides
    fun provideFakeGateway(): FakeGateway = FakeGateway()

    @Singleton
    @Provides
    fun provideCODProcessor(
        orderDao: OrderDao,
        paymentDao: PaymentDao
    ): CODProcesser = CODProcesser(orderDao, paymentDao)

    @Singleton
    @Provides
    fun provideEWalletProcessor(
        walletDao: WalletDao,
        paymentDao: PaymentDao,
        orderDao: OrderDao,
        eWalletSimulator: EWalletSimulator,
    ): EWalletProcessor = EWalletProcessor(walletDao, paymentDao, orderDao, eWalletSimulator)

    @Singleton
    @Provides
    fun provideOnlineGatewayProcessor(
        getWay: FakeGateway,
        paymentDao: PaymentDao,
        orderDao: OrderDao
    ): OnlineGatewayProcessor = OnlineGatewayProcessor(getWay, paymentDao, orderDao)
}