package com.erayerarslan.floreplica.model

import com.google.gson.annotations.SerializedName

data class ProductItem(

    @SerializedName("id")
    val id: Int?,
    @SerializedName("price")
    val price: Double?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("image")
    val image: String?,
   // @SerializedName("category")
   // val category: String?
)
