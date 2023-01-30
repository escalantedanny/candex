package com.escalantedanny.candesk.dogs.repositories

import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import com.escalantedanny.candesk.dogs.api.DogsApi
import com.escalantedanny.candesk.dogs.api.DogsApi.retrofitService
import com.escalantedanny.candesk.dogs.api.makeNetworkCall
import com.escalantedanny.candesk.dogs.models.DogModel
import com.escalantedanny.candesk.dogs.models.ResponseDogsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class DogRepository {

    suspend fun downloadDogs(): ApiResponseStatus<List<DogModel>> {
        return makeNetworkCall {
            val dogResponseList = retrofitService.getAllDogs()
            dogResponseList.data.dogs
        }
    }

}