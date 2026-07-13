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

    private val MIGRATION_21_22 = object : Migration(21, 22) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE product ADD COLUMN ratingRate REAL NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE product ADD COLUMN ratingCount INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_22_23 = object : Migration(22, 23) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE product ADD COLUMN isVisible INTEGER NOT NULL DEFAULT 1")
            db.execSQL("ALTER TABLE product ADD COLUMN createdAt INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_23_24 = object : Migration(23, 24) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE product ADD COLUMN isDeleted INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE product ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 1")
            db.execSQL("ALTER TABLE product ADD COLUMN storagePath TEXT")
            db.execSQL("ALTER TABLE product ADD COLUMN localImageUri TEXT")
        }
    }

    private val MIGRATION_24_25 = object : Migration(24, 25) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE product ADD COLUMN stockQuantity INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_25_26 = object : Migration(25, 26) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE product ADD COLUMN imageUrlsJson TEXT NOT NULL DEFAULT '[]'")
            db.execSQL("ALTER TABLE product ADD COLUMN localImageUrisJson TEXT NOT NULL DEFAULT '[]'")
        }
    }

    private val MIGRATION_26_27 = object : Migration(26, 27) {
        override fun migrate(db: SupportSQLiteDatabase) = Unit
    }

    private val MIGRATION_27_28 = object : Migration(27, 28) {
        override fun migrate(db: SupportSQLiteDatabase) = Unit
    }

    private val MIGRATION_28_29 = object : Migration(28, 29) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE orders ADD COLUMN remoteId TEXT")
            db.execSQL("ALTER TABLE orders ADD COLUMN isSynced INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE orders ADD COLUMN syncStatus TEXT NOT NULL DEFAULT 'PENDING'")
            db.execSQL("ALTER TABLE orders ADD COLUMN updatedAt INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_29_30 = object : Migration(29, 30) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
            UPDATE orders
            SET orderStatus = 'PACKING'
            WHERE orderStatus = 'PROCESSING'
        """.trimIndent())
        }
    }

    private val MIGRATION_30_31 = object : Migration(30, 31) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            UPDATE orders
            SET paymentStatus = 'PAID'
            WHERE paymentStatus = 'SUCCESS'
            """.trimIndent()
            )
        }
    }

    private val MIGRATION_31_32 = object : Migration(31, 32) {
        override fun migrate(db: SupportSQLiteDatabase) = Unit
    }

    private val MIGRATION_32_33 = object : Migration(32, 33) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE product ADD COLUMN brand TEXT NOT NULL DEFAULT ''")
        }
    }

    private val MIGRATION_33_34 = object : Migration(33, 34) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE orders ADD COLUMN stockDeducted INTEGER NOT NULL DEFAULT 0")
        }
    }

    private val MIGRATION_34_35 = object : Migration(34, 35) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE product ADD COLUMN soldQuantity INTEGER NOT NULL DEFAULT 0")
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
            .addMigrations(
                MIGRATION_19_20,
                MIGRATION_20_21,
                MIGRATION_21_22,
                MIGRATION_22_23,
                MIGRATION_23_24,
                MIGRATION_24_25,
                MIGRATION_25_26,
                MIGRATION_26_27,
                MIGRATION_27_28,
                MIGRATION_28_29,
                MIGRATION_29_30,
                MIGRATION_30_31,
                MIGRATION_31_32,
                MIGRATION_32_33,
                MIGRATION_33_34,
                MIGRATION_34_35
            )
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
