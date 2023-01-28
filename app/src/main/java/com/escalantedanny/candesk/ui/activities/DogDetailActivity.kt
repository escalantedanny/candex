package com.escalantedanny.candesk.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import coil.load
import com.bumptech.glide.Glide
import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.databinding.ActivityDogDetailBinding
import com.escalantedanny.candesk.models.DogModel

class DogDetailActivity : AppCompatActivity() {

    companion object {
        const val DOG_KEY = "dog"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dog = intent?.extras?.getParcelable<DogModel>(DOG_KEY)

        if (dog == null){
            Toast.makeText(this, R.string.erro_showing_dog_not_found, Toast.LENGTH_LONG).show()
            finish()
            return
        }
        binding.dogImage.load(dog.imageURL)
        binding.dogIndex.text = getString(R.string.dog_index_format, dog.index)
        binding.lifeExpectancy.text = getString(R.string.dog_life_expectancy_format, dog.lifeExpectancy)
        binding.dog = dog

    }
}