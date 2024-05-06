package com.example.prm_zad1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prm_zad1.data.ProductDatabase
import com.example.prm_zad1.fragment.ARG_EDIT_ID
import com.example.prm_zad1.fragment.EditFragment
import com.example.prm_zad1.fragment.ListFragment
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), Navigable {
    private lateinit var listFragment: ListFragment

    override fun onCreate(savedInstanceState: Bundle?) {
//        thread { ProductDatabase.open(this).products.deleteWrongCategory() }//todo: usuwanie zlej kategorii do wywalenia

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
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
                    addToBackStack(EditFragment::class.java.name) //todo:cofanie do elementu
                }
//                Navigable.Destination.EDIT -> {
//                    replace(
//                        R.id.container,
//                        EditFragment()::class.java,
//                        Bundle().apply { putLong(ARG_EDIT_ID, id ?: -1L) },
//                        EditFragment::class.java.name
//                    )
//                    addToBackStack(EditFragment::class.java.name)
//                }
            }
        }.commit()
    }

}