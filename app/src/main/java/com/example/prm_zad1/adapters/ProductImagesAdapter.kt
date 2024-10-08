package com.example.prm_zad1.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prm_zad1.R
import com.example.prm_zad1.databinding.ProductImageBinding


class ProductImageViewHolder(val binding: ProductImageBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(resId: Int, isSelected: Boolean = false) {
        binding.image.setImageResource(resId)
        binding.selectedFrame.visibility = if (isSelected) {
            ViewGroup.VISIBLE
        } else {
            ViewGroup.INVISIBLE
        }
    }
}

class ProductImagesAdapter : RecyclerView.Adapter<ProductImageViewHolder>() {
    private val images = listOf(
        R.drawable.cosmetic_01,
        R.drawable.cosmetic_02,
        R.drawable.cosmetic_03,
        R.drawable.medicine_01,
        R.drawable.medicine_02,
        R.drawable.medicine_03,
        R.drawable.food_01,
        R.drawable.food_02
    )
    private var selectedPosition: Int = 0
    val selectedIdRes : Int
        get() = images[selectedPosition]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductImageViewHolder {
        val binding = ProductImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductImageViewHolder(binding).also { vh ->
            binding.root.setOnClickListener {
                setSelected(vh.layoutPosition)
            }
        }
    }

    private fun setSelected(layoutPosition: Int) {
        notifyItemChanged(selectedPosition)
        selectedPosition = layoutPosition
        notifyItemChanged(selectedPosition)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ProductImageViewHolder, position: Int) {
        holder.bind(images[position], position == selectedPosition)
    }

    fun setSelection(icon: Int?) {
        val index = images.indexOfFirst { it == icon }
        if (index != -1) {
            return setSelected(index)
        }
    }

}