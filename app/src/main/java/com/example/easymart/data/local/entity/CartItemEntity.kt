package com.example.easymart.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "cart_items",
    foreignKeys = [
        ForeignKey(
            entity = CartEntity::class,
            parentColumns = ["id"],
            childColumns = ["cartId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("cartId"),
        Index("productId"),
        Index(value = ["cartId", "productId"], unique = true)
    ]
)

data class CartItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cartId: String,
    val productId: Int,
    val name: String,
    val price: Double,
    val imageUrl: String?,
    val quantity: Int,
    val isSynced: Boolean = true, // đánh dấu đã đồng bộ với server hay chưa
    val isDeleted: Boolean = false, // xoá mềm để đồng bộ delete lên server
    val updatedAt: Long = System.currentTimeMillis(),
    val addAt: Long = System.currentTimeMillis(),
)
