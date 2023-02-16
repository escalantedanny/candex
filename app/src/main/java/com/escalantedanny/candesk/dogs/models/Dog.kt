package com.escalantedanny.candesk.dogs.models

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Dog(

    val id: Long,
    val dogType: String,
    val heightFemale: String,
    val heightMale: String,
    val imageURL: String,
    val index: Long,
    val lifeExpectancy: String,
    val nameEn: String,
    val nameEs: String,
    val temperament: String,
    val temperamentEn: String,
    val weightFemale: String,
    val weightMale: String,
    val createdAt: String,
    val updatedAt: String,
    val mlID: String,
    val inCollection: Boolean = true
) : Parcelable, Comparable<Dog> {
    override fun compareTo(other: Dog): Int {
        return if (this.index > other.index) {
            1
        } else {
            -1
        }
    }
}
