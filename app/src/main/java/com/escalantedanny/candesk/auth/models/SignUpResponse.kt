package com.escalantedanny.candesk.auth.models

import com.squareup.moshi.Json

class SignUpResponse(
    val message: String,
    @field:Json(name = "is_success") val isSuccess: Boolean,
    val data: UserResponse
)