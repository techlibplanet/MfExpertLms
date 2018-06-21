package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Relation

@Dao
interface RelationDao {

    @Insert
    fun insert(relation: Relation)

    @Query("SELECT * FROM districts")
    fun getAllRelation(): List<Relation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(relation: List<Relation>?)
}