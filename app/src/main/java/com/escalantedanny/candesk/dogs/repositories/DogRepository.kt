package com.escalantedanny.candesk.dogs.repositories

import android.util.Log
import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.api.DogsApi.retrofitService
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import com.escalantedanny.candesk.dogs.api.makeNetworkCall
import com.escalantedanny.candesk.dogs.dto.DogDTOMapper
import com.escalantedanny.candesk.dogs.models.DogModel
import com.escalantedanny.candesk.dogs.models.AddDogToUserModel
import com.escalantedanny.candesk.dogs.models.Dog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class DogRepository {

    suspend fun getDogsCollections(): ApiResponseStatus<List<DogModel>> {
        return withContext(Dispatchers.IO) {
            val allDogsListDeferred = async { downloadDogs() }
            val userDogsListDeferred = async { getUserDogs() }

            val allDogsListResponse = allDogsListDeferred.await()
            val userDogsListResponse = userDogsListDeferred.await()

            if (allDogsListResponse is ApiResponseStatus.Error) {
                allDogsListResponse
            } else if (userDogsListResponse is ApiResponseStatus.Error) {
                userDogsListResponse
            } else if (allDogsListResponse is ApiResponseStatus.Success && userDogsListResponse is ApiResponseStatus.Success) {
                ApiResponseStatus.Success(
                    getCollectionsList(
                        allDogsListResponse.data,
                        userDogsListResponse.data
                    )
                )
            } else {
                ApiResponseStatus.Error(R.string.error_desconocido)
            }
        }
    }

    private fun getCollectionsList(
        allDogsList: List<DogModel>,
        userDogsList: List<DogModel>
    ): List<DogModel> {
        return allDogsList.map {
            if (userDogsList.contains(it)) {
                Log.wtf("perro", "perro collection ${it.inCollection}")
                it
            } else {
                DogModel(
                    0, "", "", "", "", index = it.index, "", "", "",
                    "", "", "", "", "", "", "", inCollection = true
                )
            }
        }.sorted()
    }

    suspend fun downloadDogs(): ApiResponseStatus<List<DogModel>> = makeNetworkCall {
        val dogResponseList = retrofitService.getAllDogs()
        dogResponseList.data.dogs
    }

    suspend fun getUserDogs(): ApiResponseStatus<List<DogModel>> = makeNetworkCall {
        val dogResponseList = retrofitService.getUserDogs()
        dogResponseList.data.dogs
    }


    suspend fun getDogByMlId(mlDogId: String): ApiResponseStatus<Dog> = makeNetworkCall {

        val response = retrofitService.getDogByMlId(mlDogId)

        if (!response.isSuccess) {
            throw Exception(response.message)
        }

        val dogDTO = DogDTOMapper()
        dogDTO.fromDogDTOToDogDomain(response.data.dog)

    }

    suspend fun addDogToUser(dogId: Long): ApiResponseStatus<Any> = makeNetworkCall {
        val addDogToUserDTO = AddDogToUserModel(dogId)
        val defaultResponse =
            retrofitService.addDogTOUser(addDogToUserDTO)

        if (!defaultResponse.isSuccess) {
            throw Exception(defaultResponse.message)
        }
    }
}