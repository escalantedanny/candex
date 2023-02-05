package com.escalantedanny.candesk.dogs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.escalantedanny.candesk.databinding.DogListItemBinding
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

    private var onItemClickListener: ((DogModel) -> Unit)? = null
    fun setOnItemClickListener(onItemClickListener: (DogModel) -> Unit) {
        this.onItemClickListener = onItemClickListener
    }

    private var onLongItemClickListener: ((DogModel) -> Unit)? = null
    fun setOnLongItemClickListener(onLongItemClickListener: (DogModel) -> Unit) {
        this.onLongItemClickListener = onLongItemClickListener
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
            binding.dogListItemLayout.setOnClickListener {
                onItemClickListener?.invoke(dog)
            }
            binding.dogListItemLayout.setOnLongClickListener() {
                onLongItemClickListener?.invoke(dog)
                true
            }
            binding.dogImage.load(dog.imageURL)
        }
    }

}