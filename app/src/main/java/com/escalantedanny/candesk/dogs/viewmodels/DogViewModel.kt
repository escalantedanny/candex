package com.escalantedanny.candesk.dogs.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import com.escalantedanny.candesk.dogs.models.DogModel
import com.escalantedanny.candesk.dogs.repositories.DogRepository
import kotlinx.coroutines.launch

class DogViewModel : ViewModel() {

    private val _dogList = MutableLiveData<List<DogModel>>()
    val dogList: LiveData<List<DogModel>>
        get() = _dogList

    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>>
        get() = _status

    private val dogRepository = DogRepository()

    init {
        downloadDogs()
    }

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }
    }

    private fun downloadDogs() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(dogRepository.downloadDogs())
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<List<DogModel>>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dogList.value = apiResponseStatus.data!!
        }
        _status.value = apiResponseStatus as ApiResponseStatus<Any>
    }

    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {

        if (apiResponseStatus is ApiResponseStatus.Success) {
            downloadDogs()
        }
        _status.value = apiResponseStatus
    }

}