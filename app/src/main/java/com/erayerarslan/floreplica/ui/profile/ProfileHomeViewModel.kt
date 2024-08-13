package com.erayerarslan.floreplica.ui.profile

import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.erayerarslan.floreplica.model.User
import com.erayerarslan.floreplica.repository.AuthenticationRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileHomeViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository
): ViewModel()  {
    val user : MutableLiveData<User?> = MutableLiveData()
    val isLoading : MutableLiveData<Boolean> = MutableLiveData(false)
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val databaseReference = FirebaseDatabase.getInstance().reference


    fun getUser(){

        val uid = firebaseAuth.currentUser?.uid ?: return
        isLoading.value = true
        val databaseReference = FirebaseDatabase.getInstance().reference
        val userRef = databaseReference.child("Users").child(uid)
        val emailLogin = firebaseAuth.currentUser?.email

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Kullanıcı bilgilerini alın
                val firstName = dataSnapshot.child("firstName").getValue(String::class.java)
                val lastName = dataSnapshot.child("lastName").getValue(String::class.java)
                val email = dataSnapshot.child("email").getValue(String::class.java)

                Log.d("FirebaseDatabase", "First Name: $firstName, Email: $email ,Last Name: $lastName" )
                if (email == null){
                    user.value = User(firstName,lastName,emailLogin)
                }else{
                    user.value = User(firstName,lastName,email)
                }

                isLoading.value = false

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Hata durumunda yapılacak işlemler
                Log.e("FirebaseDatabase", "Error getting user data", databaseError.toException())
            }
        })
    }

}