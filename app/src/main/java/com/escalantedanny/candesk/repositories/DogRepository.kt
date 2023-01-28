package com.escalantedanny.candesk.repositories

import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.api.ApiResponseStatus
import com.escalantedanny.candesk.api.DogsApi
import com.escalantedanny.candesk.api.DogsApi.retrofitService
import com.escalantedanny.candesk.api.makeNetworkCall
import com.escalantedanny.candesk.models.DogModel
import com.escalantedanny.candesk.models.ResponseDogsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

class DogRepository {

    suspend fun downloadDogs(): ApiResponseStatus<List<DogModel>> {
        return makeNetworkCall {
            val dogResponseList = DogsApi.retrofitService.getAllDogs()
            dogResponseList.data.dogs
        }
    }

}