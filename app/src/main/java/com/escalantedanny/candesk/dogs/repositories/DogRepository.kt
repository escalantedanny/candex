package com.escalantedanny.candesk.dogs.repositories

import android.util.Log
import com.escalantedanny.candesk.api.DogsApi.retrofitService
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import com.escalantedanny.candesk.dogs.api.makeNetworkCall
import com.escalantedanny.candesk.dogs.models.DogModel
import com.escalantedanny.candesk.dogs.models.AddDogToUserModel

class DogRepository {

    suspend fun downloadDogs(): ApiResponseStatus<List<DogModel>> {
        return makeNetworkCall {
            val dogResponseList = retrofitService.getAllDogs()
            dogResponseList.data.dogs
        }
    }

    suspend fun addDogToUser(dogId:Long): ApiResponseStatus<Any> =
        makeNetworkCall {
            val addDogToUserDTO = AddDogToUserModel(dogId)
            val defaultResponse =
                retrofitService.addDogTOUser(addDogToUserDTO)

            if (!defaultResponse.isSuccess) {
                throw Exception(defaultResponse.message)
            }
        }

}