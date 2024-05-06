package com.example.prm_zad1.adapters

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.prm_zad1.R
import com.example.prm_zad1.data.ProductDatabase
import com.example.prm_zad1.databinding.ListIteamBinding
import com.example.prm_zad1.model.Product
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread

class ProductViewHolder(val binding: ListIteamBinding)
    : RecyclerView.ViewHolder(binding.root) {
    fun bind(product: Product,
             onProductRemoved: (Product) -> Unit,
             onProductMarkedAsDeleted: (Product) -> Unit,
             navigate : (Long) -> Unit
    ) {
        binding.apply {
            binding.productName.text = product.name
            binding.expirationDate.text = LocalDateTime.ofInstant(Instant.ofEpochMilli(product.expirationDate), ZoneId.of("Europe/Warsaw")).format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy"))
            binding.category.text = binding.root.context.resources.getStringArray(R.array.category)[product.category]
            binding.quantity.text = product.quantity.toString()
            binding.isDeleted.text = product.isDeleted.toString()
            binding.image.setImageResource(product.resId)
        }
        binding.root.setOnLongClickListener {
            if (!product.isExpired()) {
                AlertDialog.Builder(binding.root.context)
                    .setTitle(R.string.remove_question)
                    .setPositiveButton(R.string.remove_answer_yes) { _, _ ->
                        onProductRemoved(product)
                    }
                    .setNegativeButton(R.string.remove_answer_no) { _, _ -> }
                    .show()
            }
            if (product.isExpired() && !product.isDeleted) {
                onProductMarkedAsDeleted(product)
            }
            true
        }
        binding.root.setOnClickListener {
            if (!product.isExpired()) {
                navigate(product.id)
            } else {
                Toast.makeText(binding.root.context, R.string.expired_product, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

class ProductsAdapter(db : ProductDatabase,
                      navigate : (Long) -> Unit,
                      onListUpdate : (Int) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {
    private val db = db
    private val navigate = navigate
    private val onListUpdate = onListUpdate
    private val data = mutableListOf<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ListIteamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val onProductRemoved: (Product) -> Unit = { product ->
            val index = data.indexOf(product)
            data.remove(product)
            onListUpdate(data.size)
            notifyItemRemoved(index)
            thread {
                db.products.remove(product.id)
            }
        }
        val onProductMarkedAsDeleted: (Product) -> Unit = { product ->
            val index = data.indexOf(product)
            data[index] = product.copy(isDeleted = true)
            onListUpdate(data.size)
            notifyItemChanged(index)
            thread {
                db.products.markAsDeleted(product.id)
            }
        }

        holder.bind(data[position], onProductRemoved, onProductMarkedAsDeleted, navigate)
    }

    fun replace(newData: List<Product>) {
        data.clear()
        data.addAll(newData)
        onListUpdate(data.size)
        notifyDataSetChanged()
    }

    fun removeItem(layoutPosition: Int): Product { //todo ??????????
        val product = data.removeAt(layoutPosition)
        notifyItemRemoved(layoutPosition)
        return product
    }

    fun updateItem(product: Product) {
        val index = data.indexOfFirst { it.id == product.id }
        if (index != -1) {
            data[index] = product
            notifyItemChanged(index)
        }
    }

}
