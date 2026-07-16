package com.example.easymart.data.mapper

import android.annotation.SuppressLint
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.model.ProductRating
import com.example.easymart.utils.legacyPriceToVnd
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

fun mapProductSearchHitJson(json: String): Product? {
    val root = runCatching {
        JsonParser.parseString(json).unwrapContent().asJsonObject
    }.getOrNull() ?: return null

    val id = root.readInt("id")
        ?: root.readString("objectID")?.toIntOrNull()
        ?: return null

    val name = root.readString("name")
        ?: root.readString("title")
        ?: return null

    val imageUrls = root.readStringList("imageUrls")
    val imageUrl = root.readString("imageUrl")
        ?: root.readString("image")
        ?: imageUrls.firstOrNull().orEmpty()

    val ratingObject = root.readObject("rating")

    return Product(
        id = id,
        name = name,
        description = root.readString("description"),
        priceVnd = root.readLong("priceVnd")
            ?: legacyPriceToVnd(id, root.readDouble("price") ?: 0.0),
        imageUrl = imageUrl,
        imageUrls = imageUrls,
        brand = root.readString("brand").orEmpty(),
        category = root.readString("category").orEmpty(),
        stockQuantity = root.readInt("stockQuantity") ?: 0,
        soldQuantity = root.readInt("soldQuantity") ?: 0,
        rating = ProductRating(
            rate = root.readDouble("ratingRate")
                ?: ratingObject?.readDouble("rate")
                ?: 0.0,
            count = root.readInt("ratingCount")
                ?: ratingObject?.readInt("count")
                ?: 0
        ),
        isVisible = root.readBoolean("isVisible")
            ?: root.readBoolean("visible")
            ?: true,
        createdAt = root.readLong("createdAt") ?: 0L,
        updatedAt = root.readLong("updatedAt") ?: 0L,
        isDeleted = root.readBoolean("isDeleted")
            ?: root.readBoolean("deleted")
            ?: false,
        isSynced = true,
        storagePath = root.readString("storagePath")
    )
}

private fun JsonObject.readObject(name: String): JsonObject? {
    val value = get(name)?.unwrapContent() ?: return null
    return value.takeIf { it.isJsonObject }?.asJsonObject
}

private fun JsonObject.readString(name: String): String? {
    val value = get(name)?.unwrapContent() ?: return null
    if (!value.isJsonPrimitive) return null

    return runCatching { value.asString.trim() }
        .getOrNull()
        ?.takeIf { it.isNotBlank() }
}

private fun JsonObject.readDouble(name: String): Double? {
    val value = get(name)?.unwrapContent() ?: return null
    if (!value.isJsonPrimitive) return null

    return runCatching { value.asDouble }.getOrNull()
}

private fun JsonObject.readLong(name: String): Long? {
    val value = get(name)?.unwrapContent() ?: return null
    if (!value.isJsonPrimitive) return null

    return runCatching { value.asLong }.getOrNull()
}

private fun JsonObject.readInt(name: String): Int? {
    val value = get(name)?.unwrapContent() ?: return null
    if (!value.isJsonPrimitive) return null

    return runCatching { value.asInt }.getOrNull()
}

private fun JsonObject.readBoolean(name: String): Boolean? {
    val value = get(name)?.unwrapContent() ?: return null
    if (!value.isJsonPrimitive) return null

    return runCatching { value.asBoolean }.getOrNull()
}

private fun JsonObject.readStringList(name: String): List<String> {
    val value = get(name)?.unwrapContent() ?: return emptyList()
    if (!value.isJsonArray) return emptyList()

    return value.asJsonArray.mapNotNull { item ->
        item.unwrapContent()
            .takeIf { it.isJsonPrimitive }
            ?.let { runCatching { it.asString.trim() }.getOrNull() }
            ?.takeIf { it.isNotBlank() }
    }
}

@SuppressLint("CheckResult")
private fun JsonElement.unwrapContent(): JsonElement {
    if (!isJsonObject) return this

    val obj = asJsonObject
    if (obj.has("content") && obj.entrySet().size <= 2) {
        return obj.get("content").unwrapContent()
    }

    return this
}
