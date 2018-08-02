package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.AssignCategory
import net.rmitsolutions.mfexpert.lms.database.entities.KycDetails

@Dao
interface AssignCategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(assignCategory: AssignCategory)

    @Query("SELECT * FROM assign_category")
    fun getAllAssignCategory(): List<AssignCategory>

    @Query("SELECT name FROM assign_category")
    fun getAllAssignCategoryNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(assignCategory: List<AssignCategory>?)
}