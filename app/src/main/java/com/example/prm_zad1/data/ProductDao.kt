package com.example.prm_zad1.data

import androidx.room.*
import com.example.prm_zad1.data.model.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM product ORDER BY expirationDate ASC")
    fun getAll(): List<ProductEntity>

    @Query("SELECT * FROM product WHERE category = :category ORDER BY expirationDate ASC")
    fun getByCategory(category: Int): List<ProductEntity>

    @Query("SELECT * FROM product WHERE id = :id")
    fun getProduct(id: Long): ProductEntity

//    @Query("SELECT * FROM product ORDER BY name ASC") //todo czy to potrzebne
//    fun getAllSortedByName(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(newProduct: ProductEntity)

    @Update
    fun updateProduct(newProduct: ProductEntity)

    @Query("UPDATE product SET isDeleted = ${true} WHERE id = :id")
    fun markAsDeleted(id: Long)

//    @Query("DELETE FROM product WHERE category = 0") //todo czyszczenie
//    fun deleteWrongCategory()

    @Query("DELETE FROM product WHERE id = :id")
    fun remove(id: Long)
}