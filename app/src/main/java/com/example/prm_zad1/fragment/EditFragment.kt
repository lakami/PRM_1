package com.example.prm_zad1.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prm_zad1.R
import com.example.prm_zad1.adapters.ProductImagesAdapter
import com.example.prm_zad1.data.ProductDatabase
import com.example.prm_zad1.data.model.ProductEntity
import com.example.prm_zad1.databinding.FragmentEditBinding
import java.util.*
import kotlin.concurrent.thread

const val ARG_EDIT_ID = "edit_id"

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: ProductImagesAdapter
    private lateinit var db: ProductDatabase
    private var product: ProductEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = ProductDatabase.open(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductImagesAdapter()

        binding.expirationDate.minDate = Calendar.getInstance().timeInMillis //data nie może być wcześniejsza niż dzisiejsza
        binding.productName.addTextChangedListener {
            if (binding.productName.text.isNullOrBlank()) {
                binding.productName.error = this.resources.getText(R.string.name_empty_info)
            } else
            binding.productName.error = null
        }
        binding.unit.addTextChangedListener {
            if (binding.unit.text.isNullOrBlank() && binding.quntity.text.toString().toInt() > 0) {
                binding.unit.error = this.resources.getText(R.string.unit_empty_info)
            } else
            binding.unit.error = null
        }
        binding.quntity.addTextChangedListener {
            if (binding.quntity.text.isNullOrBlank()) {
                binding.quntity.setText("0")
                binding.quntity.selectAll()
                Toast.makeText(context, this.resources.getText(R.string.quantity_empty_info), Toast.LENGTH_SHORT).show()
            }
            if (binding.quntity.text.toString().toInt() > 0 && binding.unit.text.isNullOrBlank()) {
                binding.unit.error = this.resources.getText(R.string.unit_empty_info)
            }
            if (binding.quntity.text.toString().toInt() == 0) {
                binding.unit.error = null
            }
        }

        binding.expirationDate.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            view.date = calendar.timeInMillis //przestawianie daty w CalendarView
        }

        var id = requireArguments().getLong(ARG_EDIT_ID, -1)
        if (id != -1L) {
            thread {
                product = db.products.getProduct(id)
                requireActivity().runOnUiThread {
                    binding.productName.setText(product?.name ?: "")
                    binding.category.setSelection(product?.category ?: 0)
                    binding.quntity.setText(product?.quantity?.toString() ?: "")
                    binding.unit.setText(product?.unit ?: "")
                    binding.isDeleted.isChecked = product?.isDeleted ?: false
                    binding.expirationDate.date = product?.expirationDate ?: System.currentTimeMillis()

                    adapter.setSelection(
                        product?.icon?.let {
                            resources.getIdentifier(it, "drawable", requireContext().packageName)
                        }
                    )
                }
            }
        } else {
            binding.quntity.setText("0")
        }

        binding.images.apply {
            adapter = this@EditFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }

        binding.btSave.setOnClickListener {
            onSaveClick()
        }
    }

    private fun onSaveClick() {
        if (binding.productName.text.isNullOrBlank()) {
            binding.productName.error = this.resources.getText(R.string.name_empty_info)
            return
        }
        if (binding.unit.text.isNullOrBlank() && binding.quntity.text.toString().toInt() > 0) {
            binding.unit.error = this.resources.getText(R.string.unit_empty_info)
            return
        }

        val product = product?.copy(
            name = binding.productName.text.toString(),
            category = binding.category.selectedItemPosition,
            quantity = binding.quntity.text.toString().toInt(),
            unit = binding.unit.text.toString(),
            isDeleted = binding.isDeleted.isChecked,
            expirationDate = binding.expirationDate.date,
            icon = resources.getResourceEntryName(adapter.selectedIdRes)
        ) ?: ProductEntity(
            name = binding.productName.text.toString(),
            category = binding.category.selectedItemPosition,
            quantity = binding.quntity.text.toString().toInt(),
            unit = binding.unit.text.toString(),
            isDeleted = binding.isDeleted.isChecked,
            expirationDate = binding.expirationDate.date,
            icon = resources.getResourceEntryName(adapter.selectedIdRes)
        )
        this.product = product
        thread {
            db.products.addProduct(product)
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }

}