package com.example.easymart.di

import com.example.easymart.data.network.ConnectivityNetworkObserver
import com.example.easymart.domain.network.NetworkObserver
import com.example.easymart.data.remote.api.AlgoliaApi
import com.example.easymart.data.remote.api.GeminiApi
import com.example.easymart.data.remote.api.LocationApi
import com.example.easymart.data.remote.api.PaymentApi
import com.example.easymart.data.remote.api.ProductApi
import com.example.easymart.data.remote.dto.ProductApiDto
import com.example.easymart.data.remote.provider.ProductDtoDeserializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val PRODUCT_BASE_URL = "https://fakestoreapi.com/"
    private const val ALGOLIA_BASE_URL = "http://10.0.2.2:8080/"
    private const val LOCATION_BASE_URL = "https://provinces.open-api.vn/"
    private const val PAYMENT_BASE_URL = "http://127.0.0.1:3000/"
    private const val GEMINI_BASE_URL = "https://generativelanguage.googleapis.com/"

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    @Named("searchProductGson")
    fun provideGsonSearchProducts(): Gson = GsonBuilder()
        .registerTypeAdapter(ProductApiDto::class.java, ProductDtoDeserializer()).create()

    @Provides
    @Singleton
    @Named("locationGson")
    fun provideGsonLocation(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkhttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }
        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    @Named("gemini")
    fun provideGeminiRetrofit(ok: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GEMINI_BASE_URL)
            .client(ok)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("products")
    fun provideProductRetrofit(@Named("searchProductGson") gson: Gson, ok: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PRODUCT_BASE_URL)
            .client(ok)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("locations")
    fun provideLocationRetrofit(@Named("locationGson") gson: Gson, ok: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LOCATION_BASE_URL)
            .client(ok)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    @Provides
    @Singleton
    @Named("algolia")
    fun provideAlgoliaRetrofit(ok: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ALGOLIA_BASE_URL)
            .client(ok)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("payment")
    fun providePaymentRetrofit(ok: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(PAYMENT_BASE_URL)
            .client(ok)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideProductApi(@Named("products") retrofit: Retrofit): ProductApi =
        retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideLocationApi(@Named("locations") retrofit: Retrofit): LocationApi =
        retrofit.create(LocationApi::class.java)

    @Provides
    @Singleton
    fun provideAlgoliaApi(@Named("algolia") retrofit: Retrofit): AlgoliaApi =
        retrofit.create(AlgoliaApi::class.java)

    @Provides
    @Singleton
    fun providePaymentApi(@Named("payment") retrofit: Retrofit): PaymentApi =
        retrofit.create(PaymentApi::class.java)

    @Provides
    @Singleton
    fun provideGeminiApi(@Named("gemini") retrofit: Retrofit): GeminiApi =
        retrofit.create(GeminiApi::class.java)


    @Provides
    @Singleton
    fun provideNetworkObserver(
        observer: ConnectivityNetworkObserver
    ): NetworkObserver = observer
}