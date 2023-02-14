package com.escalantedanny.candesk.auth.repositories

import com.escalantedanny.candesk.api.DogsApi
import com.escalantedanny.candesk.auth.models.*
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import com.escalantedanny.candesk.dogs.api.makeNetworkCall
import com.escalantedanny.candesk.dogs.models.AddDogToUserModel

class AuthRepository {

    suspend fun signUp(email: String, password: String, confirmationPassword: String): ApiResponseStatus<UserResponse> {
        return makeNetworkCall {
            val sigUpDTO = SignUpModel(
                email = email,
                password = password,
                confirmationPassword = confirmationPassword
            )

            val signUpsResponse = DogsApi.retrofitService.signUp(sigUpDTO)

            if (!signUpsResponse.isSuccess){
                throw Exception(signUpsResponse.message)
            } else
                signUpsResponse.data

        }
    }

    suspend fun signIn(email: String, password: String): ApiResponseStatus<UserResponse> {
        return makeNetworkCall {
            val sigUpDTO = SignInModel(
                email = email,
                password = password
            )

            val signUpsResponse = DogsApi.retrofitService.signIn(sigUpDTO)

            if (!signUpsResponse.isSuccess){
                throw Exception(signUpsResponse.message)
            } else
                signUpsResponse.data

        }
    }
}