package com.escalantedanny.candesk.dogs.api

import com.escalantedanny.candesk.dogs.models.ResponseDogsModel
import com.escalantedanny.candesk.utils.Constants.BASE_URL
import com.escalantedanny.candesk.utils.Constants.GET_ALL_DOGS
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import retrofit2.http.GET

private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()


interface ApiServer {
    @GET(GET_ALL_DOGS)
    suspend fun getAllDogs(): ResponseDogsModel
}

object DogsApi {
    val retrofitService : ApiServer by lazy {
        retrofit.create(ApiServer::class.java)
    }
}