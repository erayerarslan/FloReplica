package com.erayerarslan.floreplica.repository

import com.erayerarslan.floreplica.core.Response
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserData(): Flow<Response<User>>

    suspend fun updateUserData(user: User): Flow<Response<User?>>

    suspend fun getFavoriteProductIds(): List<String>
}