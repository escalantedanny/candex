package com.escalantedanny.candesk.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class DogModel(

    val id: Long,

    @field:Json(name = "dog_type")
    val dogType: String,

    @field:Json(name = "height_female")
    val heightFemale: String,

    @field:Json(name = "height_male")
    val heightMale: String,

    @field:Json(name = "image_url")
    val imageURL: String,

    val index: Long,

    @field:Json(name = "life_expectancy")
    val lifeExpectancy: String,

    @field:Json(name = "name_en")
    val nameEn: String,

    @field:Json(name = "name_es")
    val nameEs: String,

    val temperament: String,

    @field:Json(name = "temperament_en")
    val temperamentEn: String,

    @field:Json(name = "weight_female")
    val weightFemale: String,

    @field:Json(name = "weight_male")
    val weightMale: String,

    @field:Json(name = "created_at")
    val createdAt: String,

    @field:Json(name = "updated_at")
    val updatedAt: String,

    @field:Json(name = "ml_id")
    val mlID: String
) : Parcelable
