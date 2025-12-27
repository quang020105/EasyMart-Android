package com.example.easymart.data.remote.provider

import com.algolia.client.api.SearchClient

object AlgoliaProvider {
    private var client: SearchClient? = null
    //khởi tạo algolia sau khi lấy appId và apiKey từ backend
    fun init(appId: String, apiKey: String) {
        client = SearchClient(appId, apiKey)
    }

    fun isInit() = client != null

    fun getClient(): SearchClient {
        return client ?: throw IllegalStateException("Algolia client not initialized")
    }
}