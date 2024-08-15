package com.erayerarslan.floreplica.network

import com.erayerarslan.floreplica.model.ProductItem

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ApiService {
    @GET("products")

    suspend fun getProductList() : Response<List<ProductItem>>

    @GET("products/categories")
    suspend fun getCategoryList() : Response<List<String>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductItem




}