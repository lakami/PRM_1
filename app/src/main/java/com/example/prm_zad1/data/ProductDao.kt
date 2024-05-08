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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addProduct(newProduct: ProductEntity)

    @Update
    fun updateProduct(newProduct: ProductEntity)

    @Query("UPDATE product SET isDeleted = ${true} WHERE id = :id")
    fun markAsDeleted(id: Long)

    @Query("DELETE FROM product")
    fun deleteAllProducts()

    @Insert()
    fun addAll(products: Array<ProductEntity>)

    @Query("DELETE FROM product WHERE id = :id")
    fun remove(id: Long)
}