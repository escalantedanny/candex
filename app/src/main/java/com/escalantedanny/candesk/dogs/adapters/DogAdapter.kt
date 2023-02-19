package com.escalantedanny.candesk.dogs.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.escalantedanny.candesk.R
import com.escalantedanny.candesk.databinding.DogListItemBinding
import com.escalantedanny.candesk.dogs.dto.DogDTOMapper
import com.escalantedanny.candesk.dogs.models.Dog
import com.escalantedanny.candesk.dogs.models.DogModel

class DogAdapter : ListAdapter<DogModel, DogAdapter.DogViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<DogModel>() {
        override fun areItemsTheSame(oldItem: DogModel, newItem: DogModel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: DogModel, newItem: DogModel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private var onItemClickListener: ((Dog) -> Unit)? = null
    fun setOnItemClickListener(onItemClickListener: (Dog) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val binding = DogListItemBinding.inflate(LayoutInflater.from(parent.context))
        return DogViewHolder(binding)
    }

    override fun onBindViewHolder(dogViewHolder: DogViewHolder, position: Int) {
        val dog = getItem(position)
        dogViewHolder.bin(dog)
    }

    inner class DogViewHolder(private val binding: DogListItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bin( dog : DogModel) {

            val dogDTOMapper = DogDTOMapper()
            val dog2 = dogDTOMapper.fromDogDTOToDogDomain(dog)

            if (!dog2.inCollection){

                binding.dogListItemLayout.background = ContextCompat.getDrawable(
                    binding.dogImage.context,
                    R.drawable.dog_list_item_background
                )

                binding.dogImage.visibility = View.VISIBLE
                binding.dogImageNull.visibility = View.GONE

                binding.dogListItemLayout.setOnClickListener {
                    onItemClickListener?.invoke(dog2)
                }

                binding.dogImage.load(dog2.imageURL)
            } else {
                binding.dogImage.visibility = View.GONE
                binding.dogImageNull.visibility = View.VISIBLE
                binding.dogListItemLayout.background = ContextCompat.getDrawable(
                    binding.dogImage.context,
                    R.drawable.dog_list_item_null_backgroung
                )
                binding.dogListItemLayout.setOnLongClickListener() {
                    true
                }
            }

        }
    }

}