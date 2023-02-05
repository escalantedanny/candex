package com.escalantedanny.candesk.dogs.models

import com.squareup.moshi.Json

class AddDogToUserModel(
    @field:Json(name = "dog_id") val dogId: Long
)