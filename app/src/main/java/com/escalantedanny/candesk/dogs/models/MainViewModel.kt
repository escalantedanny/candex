package com.escalantedanny.candesk.dogs.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import com.escalantedanny.candesk.dogs.repositories.DogRepository
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _dog = MutableLiveData<Dog>()
    val dog: LiveData<Dog>
        get() = _dog

    private val _status = MutableLiveData<ApiResponseStatus<Dog>>()
    val status: LiveData<ApiResponseStatus<Dog>>
        get() = _status

    private val dogRepository = DogRepository()

    fun getDogByMlId(mlDogId:String){
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(dogRepository.getDogByMlId(mlDogId))
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<Dog>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dog.value = apiResponseStatus.data!!
        }
        _status.value = apiResponseStatus
    }

}