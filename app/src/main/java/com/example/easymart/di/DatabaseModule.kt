package com.example.easymart.di

import android.content.Context
import androidx.room.Room
import com.example.easymart.data.local.db.EasyMartDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): EasyMartDatabase {
        return Room.databaseBuilder(
                appContext,
                EasyMartDatabase::class.java,
                "easy_mart_database",
            ).fallbackToDestructiveMigration(false).build()
    }

    @Provides
    fun provideCartDao(database: EasyMartDatabase) = database.getCartDao()

    @Provides
    fun provideAddressDao(database: EasyMartDatabase) = database.getALlAddressDao()
}