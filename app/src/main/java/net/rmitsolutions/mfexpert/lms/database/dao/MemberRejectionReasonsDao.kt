package net.rmitsolutions.mfexpert.lms.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import net.rmitsolutions.mfexpert.lms.database.entities.MemberRejectionReasons

@Dao
interface MemberRejectionReasonsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memberRejectionReasons: MemberRejectionReasons)

    @Query("SELECT * FROM member_rejection_reasons")
    fun getAllMemberRejectionReasons(): List<MemberRejectionReasons>

    @Query("SELECT name FROM member_rejection_reasons")
    fun getAllMemberRejectionReasonNames() : List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(memberRejectionReasons: List<MemberRejectionReasons>?)
}