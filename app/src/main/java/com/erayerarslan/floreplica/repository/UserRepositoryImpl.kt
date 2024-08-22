package com.erayerarslan.floreplica.repository

import android.util.Log
import com.erayerarslan.floreplica.core.Response
import com.erayerarslan.floreplica.model.ProductItem
import com.erayerarslan.floreplica.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val databaseReference: DatabaseReference

) : UserRepository {

    override suspend fun getUserData(): Flow<Response<User>> = flow {

            emit(Response.Loading)

            val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
            val emailLogin = auth.currentUser?.email

            val userRef = databaseReference.child("Users").child(uid)
            val user = suspendCoroutine<User?> { continuation ->
                userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val firstName = dataSnapshot.child("firstName").getValue(String::class.java)
                        val lastName = dataSnapshot.child("lastName").getValue(String::class.java)
                        val email = dataSnapshot.child("email").getValue(String::class.java)
                        val gender = dataSnapshot.child("gender").getValue(String::class.java)


                        val user = if (email == null) {
                            User(firstName, lastName, emailLogin,gender)
                        } else {
                            User(firstName, lastName, email,gender)
                        }
                        continuation.resume(user)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        continuation.resumeWithException(databaseError.toException())
                    }
                })
            }

        // Eğer kullanıcı verisi alınmışsa Success olarak döndürülür
            user?.let {
                emit(Response.Success(it))
            } ?: throw Exception("User data is null")

    }

    override suspend fun updateUserData(user: User): Flow<Response<User>> = flow {
        emit(Response.Loading)
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val emailLogin = auth.currentUser?.email
        val userRef = databaseReference.child("Users").child(uid)
        userRef.child("firstName").setValue(user.firstName)
        userRef.child("lastName").setValue(user.lastName)
        userRef.child("email").setValue(emailLogin)
        userRef.child("gender").setValue(user.gender)



        emit(Response.Success(user))

    }
    override suspend fun getFavoriteProductIds(): List<String> {
        val uid = auth.currentUser?.uid ?: throw Exception("User not logged in")
        val favoriteIds = mutableListOf<String>()
        val userRef = databaseReference.child("Users").child(uid).child("favorites")

        val snapshot = suspendCoroutine<DataSnapshot> { continuation ->
            userRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    continuation.resume(task.result!!)
                } else {
                    continuation.resumeWithException(task.exception!!)
                }
            }
        }

        snapshot.children.forEach { child ->
            favoriteIds.add(child.key ?: "")
        }
        return favoriteIds
    }



}
