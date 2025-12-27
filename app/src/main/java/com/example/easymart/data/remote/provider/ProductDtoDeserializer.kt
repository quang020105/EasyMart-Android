package com.example.easymart.data.remote.provider

import com.example.easymart.data.remote.dto.ProductDto
import com.google.gson.*
import java.lang.reflect.Type

class ProductDtoDeserializer : JsonDeserializer<ProductDto> {
    private fun unwrap(elem: JsonElement): JsonElement {
        when {
            elem.isJsonNull -> return elem
            elem.isJsonPrimitive -> return elem
            elem.isJsonArray -> {
                val arr = JsonArray()
                elem.asJsonArray.forEach { arr.add(unwrap(it)) }
                return arr
            }
            elem.isJsonObject -> {
                val obj = elem.asJsonObject
                if (obj.has("content") && obj.entrySet().size <= 2) {
                    return unwrap(obj.get("content"))
                }
                val out = JsonObject()
                for ((k, v) in obj.entrySet()) out.add(k, unwrap(v))
                return out
            }
            else -> return elem
        }
    }

    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): ProductDto {
        val unwrapped = unwrap(json)
        return Gson().fromJson(unwrapped, ProductDto::class.java)
    }
}