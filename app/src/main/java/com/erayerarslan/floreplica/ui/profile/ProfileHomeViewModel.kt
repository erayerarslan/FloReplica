package com.erayerarslan.floreplica.ui.profile

import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.floreplica.core.Response
import com.erayerarslan.floreplica.model.User
import com.erayerarslan.floreplica.repository.AuthenticationRepository
import com.erayerarslan.floreplica.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileHomeViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _profileHomeState = MutableStateFlow<Response<User>>(Response.Loading)
    val profileHomeState: StateFlow<Response<User>> = _profileHomeState

    suspend fun getUser() {
        _profileHomeState.value = Response.Loading
        viewModelScope.launch {
            userRepository.getUserData().collect { response ->
                _profileHomeState.value = response

            }
        }
    }
}