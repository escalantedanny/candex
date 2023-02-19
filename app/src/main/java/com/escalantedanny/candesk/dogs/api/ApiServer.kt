package com.escalantedanny.candesk.api

import com.escalantedanny.candesk.auth.models.SignInModel
import com.escalantedanny.candesk.auth.models.SignUpModel
import com.escalantedanny.candesk.auth.models.SignUpResponse
import com.escalantedanny.candesk.dogs.api.DogApiResponse
import com.escalantedanny.candesk.dogs.models.AddDogToUserModel
import com.escalantedanny.candesk.responses.ResponseDogsModel
import com.escalantedanny.candesk.responses.ApiServiceInterceptor
import com.escalantedanny.candesk.responses.DefaultResponse
import com.escalantedanny.candesk.utils.Constants.ADD_DOGS_TO_USER
import com.escalantedanny.candesk.utils.Constants.BASE_URL
import com.escalantedanny.candesk.utils.Constants.GET_ALL_DOGS
import com.escalantedanny.candesk.utils.Constants.GET_DOG_BY_ML_ID
import com.escalantedanny.candesk.utils.Constants.GET_USER_DOGS
import com.escalantedanny.candesk.utils.Constants.SIGN_IN_TO_USER
import com.escalantedanny.candesk.utils.Constants.SIGN_UP_TO_USER
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

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

    @POST(SIGN_UP_TO_USER)
    suspend fun signUp(@Body dataSignUp: SignUpModel): SignUpResponse

    @POST(SIGN_IN_TO_USER)
    suspend fun signIn(@Body dataSignUp: SignInModel): SignUpResponse

    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_HEADER_KEY}: true")
    @GET(GET_USER_DOGS)
    suspend fun getUserDogs(): ResponseDogsModel

    @GET(GET_DOG_BY_ML_ID)
    suspend fun getDogByMlId(@Query("ml_id") mlId: String) : DogApiResponse

}

object DogsApi {
    val retrofitService: ApiServer by lazy {
        retrofit.create(ApiServer::class.java)
    }
}