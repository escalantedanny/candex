package com.escalantedanny.candesk.dogs.dto

import com.escalantedanny.candesk.dogs.models.Dog
import com.escalantedanny.candesk.dogs.models.DogModel

class DogDTOMapper {

    fun fromDogDTOToDogDomain(dogDTO: DogModel): Dog {
        return Dog(
            dogDTO.id,
            dogDTO.dogType,
            dogDTO.heightFemale,
            dogDTO.heightMale,
            dogDTO.imageURL,
            dogDTO.index,
            dogDTO.lifeExpectancy,
            dogDTO.nameEn,
            dogDTO.nameEs,
            dogDTO.temperament,
            dogDTO.temperamentEn,
            dogDTO.weightFemale,
            dogDTO.weightMale,
            dogDTO.createdAt,
            dogDTO.updatedAt,
            dogDTO.mlID,
            dogDTO.inCollection,
        )
    }

}