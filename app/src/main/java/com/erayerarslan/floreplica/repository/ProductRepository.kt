package com.erayerarslan.floreplica.repository

import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.network.ApiService
import javax.inject.Inject

class ProductRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getProductList() = apiService.getProductList()
}