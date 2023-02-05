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
            ApiResponseStatus.Error(R.string.error_desconocido)
        }

    }
}