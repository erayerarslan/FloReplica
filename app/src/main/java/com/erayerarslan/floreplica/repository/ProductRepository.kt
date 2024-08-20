package com.erayerarslan.floreplica.repository

import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.network.ApiService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

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

    suspend fun getProductsByIds(ids: List<String>): List<ProductItem> {
        val products = mutableListOf<ProductItem>()
        val response = apiService.getProductList()
        response.body()?.forEach { product ->
            if (ids.contains(product.id.toString())) {
                products.add(product)

            }
        }

        return products
    }
    suspend fun getProductListCategory(productId: Int): ProductItem {
        val response = apiService.getProduct(productId)
        val responseList = getAllProducts()
        val products = mutableListOf<ProductItem>()
        responseList.forEach { product ->
            if (product.category == response.category) {
                products.add(product)
            }
        }
        return products[0]


    }



}