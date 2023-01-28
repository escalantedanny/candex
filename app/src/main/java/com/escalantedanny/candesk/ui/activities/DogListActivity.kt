package com.escalantedanny.candesk.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.adapters.DogAdapter
import com.escalantedanny.candesk.api.ApiResponseStatus
import com.escalantedanny.candesk.constants.Constants.SPAN_COUNT
import com.escalantedanny.candesk.databinding.ActivityDogListBinding
import com.escalantedanny.candesk.models.DogModel
import com.escalantedanny.candesk.ui.activities.DogDetailActivity.Companion.DOG_KEY
import com.escalantedanny.candesk.viewmodels.DogViewModel

class DogListActivity : AppCompatActivity() {

    private val dogListViewModel: DogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDogListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler = binding.dogRecycler
        recycler.layoutManager = GridLayoutManager(this, SPAN_COUNT)
        val adapter = DogAdapter()
        adapter.setOnItemClickListener {
            val intent = Intent(this, DogDetailActivity::class.java)
            intent.putExtra(DOG_KEY, it)
            startActivity(intent)
        }
        recycler.adapter = adapter
        dogListViewModel.dogList.observe(this){
            adapter.submitList(it)
        }

        dogListViewModel.status.observe(this){
            when(it){
                is ApiResponseStatus.Error -> {
                    binding.dogProgress.visibility = View.GONE
                    Toast.makeText(this, R.string.erro_showing_dog_not_found, Toast.LENGTH_LONG).show()
                }
                is ApiResponseStatus.Loading -> binding.dogProgress.visibility = View.VISIBLE
                is ApiResponseStatus.Success -> {
                    binding.dogProgress.visibility = View.GONE
                }
            }

        }



    }

}