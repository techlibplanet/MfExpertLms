package net.rmitsolutions.mfexpert.lms.sync

import android.content.Context
import android.util.Log
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.libcam.Constants.logD
import net.rmitsolutions.mfexpert.lms.Constants
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.*
import net.rmitsolutions.mfexpert.lms.helpers.processRequest
import net.rmitsolutions.mfexpert.lms.helpers.showDialog
import net.rmitsolutions.mfexpert.lms.network.IMasters
import org.jetbrains.anko.toast
import retrofit2.Response
import java.io.IOException

// Sync Masters
class SyncMasters {

    private val TAG = SyncMasters::class.java.simpleName


    // Sync districts
    internal fun syncDistricts(context: Context, apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String? {
        var message = ""
        logD(TAG, "Start syncing district !")
        val response: Response<List<District>>
        try {
            response = masterService.getDistricts(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterDistricts", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val districts = response.body()
            val mDistrict = District()
            for(result in districts!!){
                logD("SyncMasterDistrict", "Id - ${result.id}")
                logD("SyncMasterDistrict", "Name - ${result.name}")
                mDistrict.id = result.id
                mDistrict.name = result.name
                database.districtDao().insert(mDistrict)
            }

            logD("SyncMasterDistrict", "District sync successfully")

        }else {
            return response.message()
        }

        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(masterService.getDistrict(apiAccessToken)
                .processRequest(context,
                        { districts ->
                            districts.forEach { r ->
                                logD(TAG,"Districts Id : ${r.id}")
                                logD(TAG, "Districts Name  : ${r.name}")
                            }
                        }
                ) { err ->
                    logD("SyncMasters","Error - $err")
                    when (err) {
                        Constants.TOKEN_REFRESH_SUCCESS -> {
                            message = Constants.TOKEN_REFRESH_SUCCESS
                            logD(TAG, "Token Refresh Success")
                            context.toast("Session Expired. Tap again to refresh.")

                            context.showDialog(context, "Session Expired. Tap again to refresh.")
                        }
                        Constants.TOKEN_REFRESH_FAILED -> {
                            logD(TAG, "Token refresh Failed")
                            message = Constants.TOKEN_REFRESH_FAILED
                        }
                        else -> {
                            logD("SyncMasters","Unknown error occurred - $err")
                            message = err.toString()
                        }
                    }
                }
        )

        return message
    }


    // Sync Literacy
    internal fun syncLiteracy(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing Literacy !")
        val response: Response<List<Literacy>>
        try {
            response = masterService.getLiteracyTypes(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterLiteracy", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val literacy = response.body()
            val mLiteracy = Literacy()
            for(result in literacy!!){
                logD("SyncMasterLiteracy", "Id - ${result.id}")
                logD("SyncMasterLiteracy", "Name - ${result.name}")
                mLiteracy.id = result.id
                mLiteracy.name = result.name
                database.literacyDao().insert(mLiteracy)
            }

            logD("SyncMasterLiteracy", "Literacy sync successfully")

        }else {
            return response.message()
        }
        return message
    }


    // Sync Loan Purpose
    internal fun syncLoanPurpose(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing Loan purpose !")
        val response: Response<List<LoanPurpose>>
        try {
            response = masterService.getPurposes(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterLoanPurpose", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val loanPurpose = response.body()
            val mLoanPurpose = LoanPurpose()
            for(result in loanPurpose!!){
                logD("SyncMasterLoanPurpose", "Id - ${result.id}")
                logD("SyncMasterLoanPurpose", "Name - ${result.name}")
                mLoanPurpose.id = result.id
                mLoanPurpose.name = result.name
                database.loanPurposeDao().insert(mLoanPurpose)
            }

            logD("SyncMasterLoanPurpose", "Loan Purpose sync successfully")

        }else {
            return response.message()
        }
        return message
    }

    // Sync Occupations
    internal fun syncOccupation(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing Occupations !")
        val response: Response<List<Occupation>>
        try {
            response = masterService.getOccupations(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterOccupations", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val occupations = response.body()
            val mOccupation = Occupation()
            for(result in occupations!!){
                logD("SyncMasterOccupation", "Id - ${result.id}")
                logD("SyncMasterOccupation", "Name - ${result.name}")
                mOccupation.id = result.id
                mOccupation.name = result.name
                database.occupationDao().insert(mOccupation)
            }

            logD("SyncMasterOccupations", "Occupation sync successfully")

        }else {
            return response.message()
        }
        return message
    }

    // Sync Primary Kyc
    internal fun syncPrimaryKyc(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing primary kyc !")
        val response: Response<List<PrimaryKYC>>
        try {
            response = masterService.getPrimaryKycDetails(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterPrimaryKyc", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val primaryKyc = response.body()
            val mPrimaryKyc = PrimaryKYC()
            for(result in primaryKyc!!){
                logD("SyncMasterPrimaryKyc", "Id - ${result.id}")
                logD("SyncMasterPrimaryKyc", "Name - ${result.name}")
                mPrimaryKyc.id = result.id
                mPrimaryKyc.name = result.name
                database.primaryKycDao().insert(mPrimaryKyc)
            }

            logD("SyncMasterPrimaryKyc", "Primary Kyc sync successfully")

        }else {
            return response.message()
        }
        return message
    }

    // Sync Relations
    internal fun syncRelations(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing relations !")
        val response: Response<List<Relation>>
        try {
            response = masterService.getRelations(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterRelation", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val relation = response.body()
            val mRelation = Relation()
            for(result in relation!!){
                logD("SyncMasterRelation", "Id - ${result.id}")
                logD("SyncMasterRelation", "Name - ${result.name}")
                mRelation.id = result.id
                mRelation.name = result.name
                database.relationDao().insert(mRelation)
            }

            logD("SyncMasterRelation", "Relations sync successfully")

        }else {
            return response.message()
        }
        return message
    }

    // Sync Secondary Kyc
    internal fun syncSecondaryKyc(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing secondary kyc !")
        val response: Response<List<SecondaryKYC>>
        try {
            response = masterService.getSecondaryKycDetails(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterSecondaryKyc", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val secondaryKyc = response.body()
            val mSecondaryKyc = SecondaryKYC()
            for(result in secondaryKyc!!){
                logD("SyncMasterSecondaryKyc", "Id - ${result.id}")
                logD("SyncMasterSecondaryKyc", "Name - ${result.name}")
                mSecondaryKyc.id = result.id
                mSecondaryKyc.name = result.name
                database.secondaryKycDao().insert(mSecondaryKyc)
            }

            logD("SyncMasterSecondaryKyc", "SecondaryKyc sync successfully")

        }else {
            return response.message()
        }
        return message
    }


    // Sync Banks
    internal fun syncBanksDetails(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing Banks !")
        val response: Response<List<Banks>>
        try {
            response = masterService.getBanks(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterBanks", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val banks = response.body()
            val mBanks = Banks()
            for(result in banks!!){
                logD("SyncMasterBanks", "Id - ${result.id}")
                logD("SyncMasterBanks", "Name - ${result.name}")
                mBanks.id = result.id
                mBanks.name = result.name
                database.banksDao().insert(mBanks)
            }

            logD("SyncMasterBanks", "Banks sync successfully")

        }else {
            return response.message()
        }
        return message
    }

    // Sync Religion
    internal fun syncReligionDetails(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing Religion !")
        val response: Response<List<Religion>>
        try {
            response = masterService.getReligionDetails(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterReligion", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val religion = response.body()
            val mReligion = Religion()
            for(result in religion!!){
                logD("SyncMasterReligion", "Id - ${result.id}")
                logD("SyncMasterReligion", "Name - ${result.name}")
                mReligion.id = result.id
                mReligion.name = result.name
                database.religionDao().insert(mReligion)
            }

            logD("SyncMasterReligion", "Religion sync successfully")

        }else {
            return response.message()
        }
        return message
    }

    // Sync Caste Details
    internal fun syncCasteDetails(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing Caste Details!")
        val response: Response<List<Caste>>
        try {
            response = masterService.getCasteDetails(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterCaste", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val caste = response.body()
            val mCaste = Caste()
            for(result in caste!!){
                logD("SyncMasterCaste", "Id - ${result.id}")
                logD("SyncMasterCaste", "Name - ${result.name}")
                mCaste.id = result.id
                mCaste.name = result.name
                database.casteDao().insert(mCaste)
            }

            logD("SyncMasterCaste", "Caste sync successfully")

        }else {
            return response.message()
        }
        return message
    }

    // Sync House Ownership Details
    internal fun syncHouseOwnershipDetails(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing House Ownership Details!")
        val response: Response<List<HouseOwnership>>
        try {
            response = masterService.getHouseOwnershipDetails(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterHOwnership", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val houseOwnership = response.body()
            val mHouseOwnership = HouseOwnership()
            for(result in houseOwnership!!){
                logD("SyncMasterHOwnership", "Id - ${result.id}")
                logD("SyncMasterHOwnership", "Name - ${result.name}")
                mHouseOwnership.id = result.id
                mHouseOwnership.name = result.name
                database.houseOwnershipDao().insert(mHouseOwnership)
            }

            logD("SyncMasterHOwnership", "House Ownership sync successfully")

        }else {
            return response.message()
        }
        return message
    }

    // Sync Income Proof Details
    internal fun syncIncomeProofDetails(apiAccessToken: String, database: MfExpertLmsDatabase, masterService: IMasters): String {
        var message = ""
        logD(TAG, "Start syncing Income Proof Details!")
        val response: Response<List<IncomeProof>>
        try {
            response = masterService.getIncomeProofDetails(apiAccessToken).execute()
        } catch (e: IOException) {
            e.printStackTrace()
            return e.message!!
        }
        Log.d("SyncMasterIncomeProof", "Response - $response")
        if (response.code() == 401){
            return response.message()
        }
        if (response.isSuccessful){
            val incomeProof = response.body()
            val mIncomeProof = IncomeProof()
            for(result in incomeProof!!){
                logD("SyncMasterIncomeProof", "Id - ${result.id}")
                logD("SyncMasterIncomeProof", "Name - ${result.name}")
                mIncomeProof.id = result.id
                mIncomeProof.name = result.name
                database.incomeProofDao().insert(mIncomeProof)
            }

            logD("SyncMasterIncomeProof", "IncomeProof sync successfully")

        }else {
            return response.message()
        }
        return message
    }
}