package com.escalantedanny.candesk.dogs.api

import com.escalantedanny.candesk.dogs.models.DogModel

/*
* clase enum para manejo de errores
enum class ApiResponseStatus {
    LOADING,
    ERROR,
    SUCCESS
}*/


sealed class ApiResponseStatus<T> {
    class Success<T>(val data: T) : ApiResponseStatus<T>()
    class Loading<T> : ApiResponseStatus<T>()
    class Error<T>(val messageId: Int) : ApiResponseStatus<T>()
}