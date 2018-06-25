package net.rmitsolutions.mfexpert.lms.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import net.rmitsolutions.mfexpert.lms.database.dao.*
import net.rmitsolutions.mfexpert.lms.database.entities.*
import net.rmitsolutions.mfexpert.lms.viewmodels.*

/**
 * Created by Madhu on 24-Apr-2018.
 */
@Database(entities = [User::class,PersonalInfoModel::class,AddressModel::class,FamilyDetailsInfoModel::class,IdentificationInfoModel::class,VillageInfoModel::class,CenterPrimaryInformationModel::class,CenterAddressInformationModel::class,GroupModel::class,
District::class, Relation::class, Occupation::class, Literacy::class, PrimaryKYC::class, SecondaryKYC::class, LoanPurpose::class], version =5)
abstract class MfExpertLmsDatabase : RoomDatabase() {
    abstract fun cbmDao(): CbmDao
    abstract fun villageDao(): VillageDao
    abstract fun centerDao(): CenterDao
    abstract fun groupDao(): GroupDao
    abstract fun districtDao(): DistrictDao
    abstract fun relationDao() : RelationDao
    abstract fun occupationDao() : OccupationDao
    abstract fun literacyDao() : LiteracyDao
    abstract fun primaryKycDao() : PrimaryKycDao
    abstract fun secondaryKycDao() : SecondaryKycDao
    abstract fun loanPurposeDao(): LoanPurposeDao
}
