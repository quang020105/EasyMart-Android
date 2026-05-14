package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.algolia.client.model.search.SearchParamsObject
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.remote.api.AlgoliaKeyRequest
import com.example.easymart.data.remote.dto.ProductApiDto
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.SearchRepository
import com.example.easymart.data.remote.provider.AlgoliaProvider
import com.google.gson.Gson
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val backendApi: com.example.easymart.data.remote.api.AlgoliaApi,
    private val gson: Gson
) : SearchRepository {
    private val indexName = "products"
    
    override suspend fun searchProducts(query: String): List<Product> {
        return try {
            if (!AlgoliaProvider.isInit()) {
                // Lấy Algolia keys từ backend nếu chưa init
                val response = backendApi.getAlgoliaKey(
                    AlgoliaKeyRequest(index = indexName)
                )
                AlgoliaProvider.init(response.appId, response.apiKey)
            }
            
            val client = AlgoliaProvider.getClient()
            val searchParams = SearchParamsObject(query = query)
            val searchResponse = client.searchSingleIndex(indexName, searchParams)
            
            searchResponse.hits.mapNotNull { hit ->
                try {
                    // Parse JSON từ hit, hit là JsonObject
                    val jsonString = gson.toJson(hit.additionalProperties)
                    val productDto = gson.fromJson(jsonString, ProductApiDto::class.java)
                    productDto.toDomain()
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getSuggestions(query: String): List<String> {
        return try {
            if (!AlgoliaProvider.isInit()) {
                val response = backendApi.getAlgoliaKey(
                    AlgoliaKeyRequest(index = indexName)
                )
                AlgoliaProvider.init(response.appId, response.apiKey)
            }
            
            val client = AlgoliaProvider.getClient()
            val searchParams = SearchParamsObject(query = query, hitsPerPage = 5)
            val searchResponse = client.searchSingleIndex(indexName, searchParams)
            Log.d("SearchRepo", "${AlgoliaProvider.isInit()}")
            Log.d("SearchRepo", "${searchResponse.hits}")
            
            searchResponse.hits.mapNotNull { hit ->
                Log.d("SearchRepo", "hit.additionalProperties: ${gson.toJson(hit.additionalProperties)}")
                try {
                    val jsonString = gson.toJson(hit.additionalProperties)
                    val productDto = gson.fromJson(jsonString, ProductApiDto::class.java)
                    Log.d("SearchRepo", productDto.title)
                    productDto.title
                } catch (e: Exception) {
                    Log.d("SearchRepo", "Lỗi trong searchResponse: ${e.message}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.d("SearchRepo", "Lỗi : ${e.message}")
            emptyList()
        }
    }
}
