package com.escalantedanny.candesk.auth.models

import com.squareup.moshi.Json

class SignUpModel(
    val email: String,
    val password: String,
    @field:Json(name = "password_confirmation") val confirmationPassword: String
)