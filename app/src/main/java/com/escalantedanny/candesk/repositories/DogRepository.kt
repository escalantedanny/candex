package com.escalantedanny.candesk.repositories

import com.escalantedanny.candesk.api.ApiResponseStatus
import com.escalantedanny.candesk.api.DogsApi
import com.escalantedanny.candesk.api.DogsApi.retrofitService
import com.escalantedanny.candesk.models.DogModel
import com.escalantedanny.candesk.models.ResponseDogsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DogRepository {

    suspend fun downloadDogs() : ApiResponseStatus {
        return withContext(Dispatchers.IO){
            try {
                val dogResponseList = retrofitService.getAllDogs()
                dogResponseList.data.dogs
                ApiResponseStatus.Success(dogResponseList.data.dogs)
            }catch (_:Exception){
                ApiResponseStatus.Error("Error al generar la lista de animales")
            }

        }
    }

}