package com.escalantedanny.candesk.auth.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escalantedanny.candesk.auth.models.User
import com.escalantedanny.candesk.auth.models.UserResponse
import com.escalantedanny.candesk.auth.repositories.AuthRepository
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import kotlinx.coroutines.launch

class AuthViewModels : ViewModel() {

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse>
        get() = _user

    private val _status = MutableLiveData<ApiResponseStatus<UserResponse>>()
    val status: LiveData<ApiResponseStatus<UserResponse>>
        get() = _status

    private val authRepository = AuthRepository()

    fun signUp(
        email: String,
        password: String, passwordConfirmation: String
    ) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(authRepository.signUp(email, password, passwordConfirmation))
        }
    }

    fun signIn(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(authRepository.signIn(email, password))
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<UserResponse>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _user.value = apiResponseStatus.data!!
        }
        _status.value = apiResponseStatus
    }


}