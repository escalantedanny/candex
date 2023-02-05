package com.escalantedanny.candesk.api

import com.escalantedanny.candesk.constants.Constants.ADD_DOGS_TO_USER
import com.escalantedanny.candesk.constants.Constants.BASE_URL
import com.escalantedanny.candesk.constants.Constants.GET_ALL_DOGS
import com.escalantedanny.candesk.dogs.models.AddDogToUserModel
import com.escalantedanny.candesk.responses.ResponseDogsModel
import com.escalantedanny.candesk.responses.ApiServiceInterceptor
import com.escalantedanny.candesk.responses.DefaultResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

private val okHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(ApiServiceInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiServer {
    @GET(GET_ALL_DOGS)
    suspend fun getAllDogs(): ResponseDogsModel

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @POST(ADD_DOGS_TO_USER)
    suspend fun addDogTOUser(@Body addDogToUserDTO: AddDogToUserModel): DefaultResponse

}

object DogsApi {
    val retrofitService: ApiServer by lazy {
        retrofit.create(ApiServer::class.java)
    }
}