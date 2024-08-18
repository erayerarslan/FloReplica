package com.erayerarslan.floreplica.model

data class User(
    var firstName: String? = null,
    var lastName: String? = null,
    var email: String? = null,
    var favorites: List<String?>? = emptyList(),
    val favoritesProducts: List<ProductItem>? = null
)