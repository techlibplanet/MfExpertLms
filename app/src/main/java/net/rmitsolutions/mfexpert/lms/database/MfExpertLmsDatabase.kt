package net.rmitsolutions.mfexpert.lms.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import net.rmitsolutions.mfexpert.lms.database.dao.*
import net.rmitsolutions.mfexpert.lms.database.entities.*
import net.rmitsolutions.mfexpert.lms.viewmodels.*

/**
 * Created by Madhu on 24-Apr-2018.
 */
@Database(entities = [User::class,PersonalInfoModel::class,AddressModel::class,FamilyDetailsInfoModel::class,
    IdentificationInfoModel::class,VillageInfoModel::class,CenterPrimaryInformationModel::class,CenterAddressInformationModel::class,
    GroupModel::class, District::class, Relation::class, Occupation::class, Literacy::class, PrimaryKYC::class,
    SecondaryKYC::class, LoanPurpose::class, KycDetails::class, Products::class, Banks::class, Nationality::class,
    Religion::class, Caste::class, HouseOwnership::class, IncomeProof::class, AssignCategory::class, LoanCloseType::class,
    MemberRejectionReasons::class, LoanRejectionReasons::class], version =5)
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
    abstract fun kycDetailsDao() : KycDetailsDao
    abstract fun productsDao() : ProductsDao
    abstract fun banksDao() : BanksDao
    abstract fun nationalityDao() : NationalityDao
    abstract fun religionDao() : ReligionDao
    abstract fun casteDao() : CasteDao
    abstract fun houseOwnershipDao() : HouseOwnershipDao
    abstract fun incomeProofDao() : IncomeProofDao
    abstract fun assignCategoryDao(): AssignCategoryDao
    abstract fun loanCloseTypeDao() : LoanCloseTypeDao
    abstract fun memberRejectionReasonsDao() : MemberRejectionReasonsDao
    abstract fun loanRejectionReasonsDao() : LoanRejectionReasonsDao
}
