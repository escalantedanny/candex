package com.escalantedanny.candesk.dogs.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import coil.load
import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.databinding.ActivityDogDetailBinding
import com.escalantedanny.candesk.dogs.api.ApiResponseStatus
import com.escalantedanny.candesk.dogs.models.Dog
import com.escalantedanny.candesk.dogs.viewmodels.DogDetailViewModel

class DogDetailActivity : AppCompatActivity() {

    private val dogViewModel: DogDetailViewModel by viewModels()

    companion object {
        const val DOG_KEY = "dog"
        const val IS_RECOGNITION_KEY = "is_recognition"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        @Suppress("DEPRECATION")
        val dog = intent?.extras?.getParcelable<Dog>(DOG_KEY)
        val isRecognition = intent?.extras?.getBoolean(IS_RECOGNITION_KEY, false) ?: false

        if (dog == null){
            Toast.makeText(this, R.string.erro_showing_dog_not_found, Toast.LENGTH_LONG).show()
            return
        }
        binding.dogImage.load(dog.imageURL)
        binding.dogIndex.text = getString(R.string.dog_index_format, dog.index)
        binding.lifeExpectancy.text = getString(R.string.dog_life_expectancy_format, dog.lifeExpectancy)
        binding.dog = dog

        binding.closeButton.setOnClickListener {
            if (isRecognition) {
                dogViewModel.addDogToUser(dog.id)
            } else {
                finish()
            }

        }

        dogViewModel.status.observe(this){
            when(it){
                is ApiResponseStatus.Error -> {
                    binding.loadingWheel.visibility = View.GONE
                    Toast.makeText(this, R.string.erro_showing_dog_not_found, Toast.LENGTH_LONG).show()
                }
                is ApiResponseStatus.Loading -> binding.loadingWheel.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> {
                    binding.loadingWheel.visibility = View.GONE
                    finish()
                }
            }

        }

    }
}