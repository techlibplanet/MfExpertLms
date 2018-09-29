package net.rmitsolutions.mfexpert.lms.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.Relation

@Dao
interface RelationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(relation: Relation)

    @Query("SELECT * FROM relations")
    fun getAllRelation(): List<Relation>

    @Query("SELECT name FROM relations")
    fun getAllRelationsNames(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(relation: List<Relation>?)
}