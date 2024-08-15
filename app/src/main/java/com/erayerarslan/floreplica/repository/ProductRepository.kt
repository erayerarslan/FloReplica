package com.erayerarslan.floreplica.repository

import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.network.ApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(val apiService: ApiService) {
    suspend fun getProductList() = apiService.getProductList()


    suspend fun getProductCategory() = apiService.getCategoryList()

    suspend fun getAllProducts(): List<ProductItem> {
        val response = apiService.getProductList()
        return if (response.isSuccessful) {
            response.body() ?: emptyList()
        } else {
            emptyList()
        }
    }
    suspend fun getProduct(productId: Int) =apiService.getProduct(productId)




}