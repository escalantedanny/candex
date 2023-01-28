package com.escalantedanny.candesk.models

import com.google.gson.annotations.SerializedName

data class ResponseDogsModel (
    val message: String,

    @SerializedName("is_success")
    val isSuccess: Boolean,

    val data: Data
)

data class Data (
    val dogs: List<DogModel>
)
