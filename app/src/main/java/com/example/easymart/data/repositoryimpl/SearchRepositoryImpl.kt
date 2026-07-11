package com.example.easymart.data.repositoryimpl

import android.util.Log
import com.algolia.client.api.SearchClient
import com.algolia.client.model.search.SearchParamsObject
import com.example.easymart.data.mapper.toDomain
import com.example.easymart.data.remote.api.AlgoliaApi
import com.example.easymart.data.remote.api.AlgoliaKeyRequest
import com.example.easymart.data.remote.dto.ProductApiDto
import com.example.easymart.data.remote.provider.AlgoliaProvider
import com.example.easymart.domain.model.Product
import com.example.easymart.domain.repository.SearchRepository
import com.google.gson.Gson
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val backendApi: AlgoliaApi,
    private val gson: Gson
) : SearchRepository {
    private val indexName = "products"

    override suspend fun searchProducts(query: String): List<Product> {
        return try {
            val client = getAlgoliaClient()
            val searchParams = SearchParamsObject(query = query)
            val searchResponse = client.searchSingleIndex(indexName, searchParams)

            searchResponse.hits.mapNotNull { hit ->
                try {
                    val jsonString = gson.toJson(hit.additionalProperties)
                    val productDto = gson.fromJson(jsonString, ProductApiDto::class.java)
                    productDto.toDomain()
                } catch (e: Exception) {
                    null
                }
            }
        } catch (e: LinkageError) {
            Log.e("SearchRepo", "Search dependency error", e)
            emptyList()
        } catch (e: Exception) {
            Log.d("SearchRepo", "Search failed: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getSuggestions(query: String): List<String> {
        return try {
            val client = getAlgoliaClient()
            val searchParams = SearchParamsObject(query = query, hitsPerPage = 5)
            val searchResponse = client.searchSingleIndex(indexName, searchParams)

            searchResponse.hits.mapNotNull { hit ->
                try {
                    val jsonString = gson.toJson(hit.additionalProperties)
                    val productDto = gson.fromJson(jsonString, ProductApiDto::class.java)
                    productDto.title
                } catch (e: Exception) {
                    Log.d("SearchRepo", "Suggestion item parse failed: ${e.message}")
                    null
                }
            }
        } catch (e: LinkageError) {
            Log.e("SearchRepo", "Suggestion dependency error", e)
            emptyList()
        } catch (e: Exception) {
            Log.d("SearchRepo", "Suggestion failed: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getAlgoliaClient(): SearchClient {
        if (!AlgoliaProvider.isInit()) {
            val response = backendApi.getAlgoliaKey(
                AlgoliaKeyRequest(index = indexName)
            )
            AlgoliaProvider.init(response.appId, response.apiKey)
        }

        return AlgoliaProvider.getClient()
    }
}
