package com.mariabuliga.softpaws.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mariabuliga.softpaws.R
import com.mariabuliga.softpaws.data.model.CatDataItem
import com.mariabuliga.softpaws.databinding.CatItemBinding

class CatAdapter(private val catInterface: CatInterface) :
    RecyclerView.Adapter<CatAdapter.PetViewHolder>() {

    inner class PetViewHolder(val binding: CatItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(cat: CatDataItem) {
            cat.image?.url?.let {
                Glide.with(itemView)
                    .load(cat.image.url)
                    .placeholder(R.drawable.pet_icon_light_grey)
                    .error(R.drawable.pet_icon_light_grey)
                    .into(binding.catImage)
            }

            binding.catName.text = cat.name

            binding.catImage.setOnClickListener {
                catInterface.onCatClick(cat)
            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<CatDataItem>() {
        override fun areItemsTheSame(
            oldItem: CatDataItem,
            newItem: CatDataItem
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CatDataItem,
            newItem: CatDataItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    var cats: List<CatDataItem>
        get() = asyncListDiffer.currentList
        set(value) {
            asyncListDiffer.submitList(value)
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PetViewHolder {
        val binding = CatItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PetViewHolder,
        position: Int
    ) {
        val cat = cats[position]

        holder.bind(cat)
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

}