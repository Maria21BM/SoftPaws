package com.mariabuliga.softpaws

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mariabuliga.softpaws.databinding.PetItemBinding
import com.mariabuliga.softpaws.models.PetData

class PetAdapter() : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    inner class PetViewHolder(val binding: PetItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<PetData>() {
        override fun areItemsTheSame(
            oldItem: PetData,
            newItem: PetData
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: PetData,
            newItem: PetData
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(data: List<PetData>){
        asyncListDiffer.submitList(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PetViewHolder {
        val binding = PetItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PetViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PetViewHolder,
        position: Int
    ) {
        val currentSelection = asyncListDiffer.currentList[position]
        holder.binding.petImage
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

}