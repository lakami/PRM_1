package com.example.prm_zad1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prm_zad1.Navigable
import com.example.prm_zad1.R
import com.example.prm_zad1.adapters.ProductsAdapter
import com.example.prm_zad1.data.ProductDatabase
import com.example.prm_zad1.data.model.ProductEntity
import com.example.prm_zad1.databinding.FragmentListBinding
import com.example.prm_zad1.model.Product
import kotlin.concurrent.thread

class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private var adapter: ProductsAdapter? = null
    private lateinit var db : ProductDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = ProductDatabase.open(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentListBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductsAdapter(db,
            navigate = {
                (activity as? Navigable)?.navigate(Navigable.Destination.EDIT, it)
            },
            onListUpdate = {
                binding.countListElements.text = resources.getString(R.string.count_products_on_list, it)
            }
        )
        binding.filterCategory.setSelection(3)
        binding.filterState.setSelection(2)

        loadData(binding.filterCategory.selectedItemPosition, binding.filterState.selectedItemPosition)

        binding.list.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }
        binding.btAdd.setOnClickListener {
            (activity as? Navigable)?.navigate(Navigable.Destination.ADD)
        }

        binding.filterCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadData(position, binding.filterState.selectedItemPosition)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                loadData(3, binding.filterState.selectedItemPosition)
            }
        }
        binding.filterState.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadData(binding.filterCategory.selectedItemPosition, position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                loadData(binding.filterCategory.selectedItemPosition, 2)
            }
        }
    }

    fun getData(selectedCategory: Int, selectedState: Int): List<ProductEntity> {
        lateinit var productEntities: List<ProductEntity>
        if (selectedCategory == 3) {
            productEntities = db.products.getAll()
        } else {
            productEntities = db.products.getByCategory(selectedCategory)
        }
        return productEntities.filter { product ->
            if (selectedState == 2) {
                return@filter true
            } else if (selectedState == 0) {
                return@filter product.isExpired()
            } else {
                return@filter !product.isExpired()
            }
        }
    }

    fun loadData(selectedCategory: Int, selectedState: Int) = thread {
        val productEntities = getData(selectedCategory, selectedState)

        val products = productEntities.map { entity ->
            Product(
                entity.id,
                entity.name,
                entity.category,
                entity.quantity,
                entity.unit,
                entity.isDeleted,
                entity.expirationDate,
                resources.getIdentifier(entity.icon, "drawable", requireContext().packageName)
            )
        }

        requireActivity().runOnUiThread {
            adapter?.replace(products)
        }
    }

    override fun onStart() {
        super.onStart()
        loadData(binding.filterCategory.selectedItemPosition, binding.filterState.selectedItemPosition)
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

}