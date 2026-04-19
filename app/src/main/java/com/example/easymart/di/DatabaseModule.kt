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
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private val MIGRATION_19_20 = object : Migration(19, 20) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE address ADD COLUMN userUid TEXT")
            db.execSQL("ALTER TABLE address ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE address ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE address ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE address ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_20_21 = object : Migration(20, 21) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                "CREATE TABLE IF NOT EXISTS product (" +
                    "id INTEGER NOT NULL, " +
                    "name TEXT NOT NULL, " +
                    "description TEXT, " +
                    "price REAL NOT NULL, " +
                    "imageUrl TEXT NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "updatedAt INTEGER NOT NULL, " +
                    "PRIMARY KEY(id)" +
                ")"
            )
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): EasyMartDatabase {
        return Room.databaseBuilder(
                appContext,
                EasyMartDatabase::class.java,
                "easy_mart_database",
            )
            .addMigrations(MIGRATION_19_20, MIGRATION_20_21)
            .fallbackToDestructiveMigration(false).build()
    }

    @Singleton
    @Provides
    fun provideCartDao(database: EasyMartDatabase) = database.getCartDao()

    @Singleton
    @Provides
    fun provideAddressDao(database: EasyMartDatabase) = database.getALlAddressDao()

    @Singleton
    @Provides
    fun provideOrderDao(database: EasyMartDatabase) = database.getOrderDao()

    @Singleton
    @Provides
    fun provideWalletDao(database: EasyMartDatabase) = database.getWalletDao()

    @Singleton
    @Provides
    fun providePaymentDao(database: EasyMartDatabase) = database.getPaymentDao()

    @Singleton
    @Provides
    fun provideProductDao(database: EasyMartDatabase) = database.getProductDao()
}