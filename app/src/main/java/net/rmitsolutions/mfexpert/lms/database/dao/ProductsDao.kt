package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.KycDetails
import net.rmitsolutions.mfexpert.lms.database.entities.Products

@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(products: Products)

    @Query("SELECT * FROM products")
    fun getAllProducts(): List<Products>

    @Query("SELECT name FROM products")
    fun getAllProductNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(products: List<Products>?)
}