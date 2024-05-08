package com.example.prm_zad1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.prm_zad1.data.ProductDatabase
import com.example.prm_zad1.data.model.ProductEntity
import com.example.prm_zad1.fragment.ARG_EDIT_ID
import com.example.prm_zad1.fragment.EditFragment
import com.example.prm_zad1.fragment.ListFragment
import java.util.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), Navigable {
    private lateinit var listFragment: ListFragment
    private lateinit var db : ProductDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = ProductDatabase.open(this)
        thread {
            db.products.deleteAllProducts()
            loadDataIfEmptyDB()
        }


        setContentView(R.layout.activity_main)

        listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }

    private fun loadDataIfEmptyDB() {
        if (db.products.getAll().isEmpty()) {
            db.products.addAll(
                arrayOf(
                    ProductEntity(
                        name = "Apap",
                        category = 0,
                        expirationDate = Calendar.getInstance()
                            .apply {
                                add(Calendar.DAY_OF_MONTH, 3)
                                add(Calendar.MONTH, 1)
                                add(Calendar.YEAR, 1)
                            }
                            .timeInMillis,
                        isDeleted = false,
                        icon = resources.getResourceEntryName(R.drawable.medicine_01),
                        quantity = 0,
                        unit = ""
                    ),
                    ProductEntity(
                        name = "Aspirin",
                        category = 0,
                        expirationDate = Calendar.getInstance()
                            .apply {
                                add(Calendar.DAY_OF_MONTH, 13)
                                add(Calendar.MONTH, 12)
                                add(Calendar.YEAR, 1)
                            }
                            .timeInMillis,
                        isDeleted = false,
                        icon = resources.getResourceEntryName(R.drawable.medicine_02),
                        quantity = 1,
                        unit = "10 x 500 mg"
                    ),
                    ProductEntity(
                        name = "Ibuprom",
                        category = 0,
                        expirationDate = Calendar.getInstance()
                            .apply {
                                add(Calendar.DAY_OF_MONTH, 5)
                                add(Calendar.MONTH, 1)
                                add(Calendar.YEAR, -1)
                            }
                            .timeInMillis,
                        isDeleted = false,
                        icon = resources.getResourceEntryName(R.drawable.medicine_03),
                        quantity = 2,
                        unit = "10 x 200 mg"
                    ),
                    ProductEntity(
                        name = "Shower Gel",
                        category = 1,
                        expirationDate = Calendar.getInstance()
                            .apply {
                                add(Calendar.DAY_OF_MONTH, 1)
                                add(Calendar.MONTH, 1)
                                add(Calendar.YEAR, 2)
                            }
                            .timeInMillis,
                        isDeleted = false,
                        icon = resources.getResourceEntryName(R.drawable.cosmetic_01),
                        quantity = 3,
                        unit = "350 ml"
                    ),
                    ProductEntity(
                        name = "Toohtpaste",
                        category = 1,
                        expirationDate = Calendar.getInstance()
                            .apply {
                                add(Calendar.DAY_OF_MONTH, 21)
                                add(Calendar.MONTH, 1)
                                add(Calendar.YEAR, 3)
                            }
                            .timeInMillis,
                        isDeleted = false,
                        icon = resources.getResourceEntryName(R.drawable.cosmetic_02),
                        quantity = 4,
                        unit = "100 ml"
                    ),
                    ProductEntity(
                        name = "Cream",
                        category = 1,
                        expirationDate = Calendar.getInstance()
                            .apply {
                                add(Calendar.DAY_OF_MONTH, 1)
                                add(Calendar.MONTH, 1)
                                add(Calendar.YEAR, -2)
                            }
                            .timeInMillis,
                        isDeleted = false,
                        icon = resources.getResourceEntryName(R.drawable.cosmetic_03),
                        quantity = 1,
                        unit = "50 ml"
                    ),
                    ProductEntity(
                        name = "Tea",
                        category = 2,
                        expirationDate = Calendar.getInstance()
                            .apply {
                                add(Calendar.DAY_OF_MONTH, 1)
                                add(Calendar.MONTH, 10)
                                add(Calendar.YEAR, -2)
                            }
                            .timeInMillis,
                        isDeleted = true,
                        icon = resources.getResourceEntryName(R.drawable.food_01),
                        quantity = 1,
                        unit = "250 g"
                    ),
                    ProductEntity(
                        name = "Chocolate",
                        category = 2,
                        expirationDate = Calendar.getInstance()
                            .apply {
                                add(Calendar.DAY_OF_MONTH, 9)
                                add(Calendar.MONTH, 11)
                                add(Calendar.YEAR, 2)
                            }
                            .timeInMillis,
                        isDeleted = true,
                        icon = resources.getResourceEntryName(R.drawable.food_02),
                        quantity = 0,
                        unit = ""
                    ),
                    ProductEntity(
                        name = "Musli",
                        category = 2,
                        expirationDate = Calendar.getInstance()
                            .apply {
                                add(Calendar.DAY_OF_MONTH, 15)
                                add(Calendar.MONTH, 2)
                                add(Calendar.YEAR, -3)
                            }
                            .timeInMillis,
                        isDeleted = true,
                        icon = resources.getResourceEntryName(R.drawable.food_02),
                        quantity = 1,
                        unit = "270 g"
                    )
                )
            )
        }
    }

    override fun navigate(to: Navigable.Destination, id: Long?) {
        supportFragmentManager.beginTransaction().apply {
            when (to) {
                Navigable.Destination.LIST -> {
                    replace(R.id.container, listFragment, listFragment.javaClass.name)
                    addToBackStack(listFragment.javaClass.name)
                }
                Navigable.Destination.ADD, Navigable.Destination.EDIT -> {
                    replace(R.id.container,
                        EditFragment()::class.java,
                        Bundle().apply { putLong(ARG_EDIT_ID, id ?: -1L) },
                        EditFragment::class.java.name)
                    addToBackStack(EditFragment::class.java.name)
                }
            }
        }.commit()
    }

}