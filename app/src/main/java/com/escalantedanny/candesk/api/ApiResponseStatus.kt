package com.escalantedanny.candesk.api

import com.escalantedanny.candesk.models.DogModel

/*
* clase enum para manejo de errores
enum class ApiResponseStatus {
    LOADING,
    ERROR,
    SUCCESS
}*/


sealed class ApiResponseStatus() {
    class Success(val dogList: List<DogModel>) : ApiResponseStatus()
    class Loading() : ApiResponseStatus()
    class Error(val message: String) : ApiResponseStatus()
}