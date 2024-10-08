package com.erayerarslan.floreplica.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.floreplica.repository.AuthenticationRepository
import com.erayerarslan.floreplica.core.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.tasks.await

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _signInState = MutableStateFlow<Response<Any>>(Response.Loading)
    val signInState: StateFlow<Response<Any>> = _signInState
    private val _signAnonymously = MutableStateFlow<Response<Any>>(Response.Loading)
    val signAnonymously: StateFlow<Response<Any>> get() = _signAnonymously

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _signInState.value = Response.Loading
            authRepository.login(email, password).collect {
                when (it) {
                    is Response.Loading -> {
                        _signInState.value = Response.Loading
                    }

                    is Response.Success -> {
                        _signInState.value = Response.Success("Sign-in successful")
                    }

                    is Response.Error -> {
                        _signInState.value = Response.Error(it.message)

                    }
                }
            }
        }

    }

    fun signInAnonymously() {
        viewModelScope.launch {
            _signAnonymously.value = Response.Loading
            authRepository.signInAnonymously().collect {
                when(it){
                    is Response.Loading -> {
                        _signAnonymously.value = Response.Loading
                    }
                    is Response.Success -> {
                        _signAnonymously.value = Response.Success("Sign-in successful")
                    }
                    is Response.Error -> {
                        _signAnonymously.value = Response.Error(it.message)
                    }
                }
            }
        }
    }

}