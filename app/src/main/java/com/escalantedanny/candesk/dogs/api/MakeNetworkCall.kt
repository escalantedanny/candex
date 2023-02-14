package com.escalantedanny.candesk.dogs.api

import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.api.DogsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

suspend fun <T> makeNetworkCall(
    call: suspend () -> T
): ApiResponseStatus<T> {
    return withContext(Dispatchers.IO) {
        try {
            val dogResponseList = DogsApi.retrofitService.getAllDogs()
            dogResponseList.data.dogs
            ApiResponseStatus.Success(call())
        } catch (_: UnknownHostException) {
            ApiResponseStatus.Error(R.string.no_internet)
        } catch (e: Exception) {
            val errorMessage = when(e.message){
                "sign_up_error" -> R.string.sign_up_error
                "sign_in_error" -> R.string.sign_in_error
                "user_already_exists" -> R.string.user_already_exists
                else -> R.string.error_desconocido
            }
            ApiResponseStatus.Error(errorMessage)
        }

    }
}