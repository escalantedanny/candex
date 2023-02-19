package com.escalantedanny.candesk.dogs.api

import com.squareup.moshi.Json

class DogApiResponse (
    val message: String,
    @field:Json(name = "is_success") val isSuccess: Boolean,
    val data: DogApiResponseDetail
)