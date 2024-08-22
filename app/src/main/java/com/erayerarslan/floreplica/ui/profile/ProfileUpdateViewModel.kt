package com.erayerarslan.floreplica.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.erayerarslan.floreplica.core.Response
import com.erayerarslan.floreplica.model.User
import com.erayerarslan.floreplica.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileUpdateViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _profileUpdateState = MutableStateFlow<Response<User>>(Response.Loading)
    private val profileUpdateState: StateFlow<Response<User>> = _profileUpdateState

    suspend fun getUser() {

        _profileUpdateState.value = Response.Loading
        viewModelScope.launch {
            userRepository.getUserData().collect { response ->
                _profileUpdateState.value = response


            }
        }
    }
}