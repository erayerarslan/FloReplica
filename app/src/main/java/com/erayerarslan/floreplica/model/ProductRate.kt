package com.erayerarslan.floreplica.model

import com.google.gson.annotations.SerializedName

data class ProductRate(
    @SerializedName("rate")
    val rate: Double,
    @SerializedName("count")
    val count: Int

)
