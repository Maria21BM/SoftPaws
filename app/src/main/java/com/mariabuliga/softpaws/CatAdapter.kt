package com.mariabuliga.softpaws

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mariabuliga.softpaws.databinding.CatItemBinding
import com.mariabuliga.softpaws.models.CatData

class CatAdapter() : RecyclerView.Adapter<CatAdapter.PetViewHolder>() {

    inner class PetViewHolder(val binding: CatItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<CatData>() {
        override fun areItemsTheSame(
            oldItem: CatData,
            newItem: CatData
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.name == newItem.name
        }

        override fun areContentsTheSame(
            oldItem: CatData,
            newItem: CatData
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    fun saveData(data: List<CatData>){
        asyncListDiffer.submitList(data)
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
        val currentSelection = asyncListDiffer.currentList[position]
        holder.binding.petImage
    }

    override fun getItemCount(): Int {
        return asyncListDiffer.currentList.size
    }

}