package net.rmitsolutions.mfexpert.lms.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import net.rmitsolutions.mfexpert.lms.MfExpertApp
import net.rmitsolutions.mfexpert.lms.R
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.database.entities.*
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerInjectActivityComponent
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.logE
import net.rmitsolutions.mfexpert.lms.helpers.processRequest
import javax.inject.Inject

class SampleActivity : AppCompatActivity() {
    @Inject
    lateinit var database: MfExpertLmsDatabase
    internal lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        val toolbar = findViewById<Toolbar>(R.id.toolbar_actionbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            finishAfterTransition()
        }

        compositeDisposable = CompositeDisposable()

        val depComponent = DaggerInjectActivityComponent.builder()
                .applicationComponent(MfExpertApp.applicationComponent)
                .build()
        depComponent.injectSampleActivity(this)
    }

    fun saveData(view: View){
        var districtList = District()
        districtList.id = 123
        districtList.name = "Hyderabad"
        compositeDisposable.add(Single.fromCallable {
            database.runInTransaction {
                database.districtDao().insert(districtList)
            }
        }.processRequest(
                { saved ->
                    logD("District Saved Successfully !")
                },
                { err ->
                    logE("Error : $err")
                }
        ))



        var relationList = Relation()
        relationList.id = 123
        relationList.name = "Father"
        relationList.gender = 0

        compositeDisposable.add(Single.fromCallable {
            database.runInTransaction {
                database.relationDao().insert(relationList)
            }
        }.processRequest(
                { saved ->
                    logD("Relations saved successfully !")
                },
                { err ->
                    logE("Error : $err")
                }
        ))



        var occupationList = Occupation()
        occupationList.id = 123
        occupationList.name = "House Husband"
        compositeDisposable.add(Single.fromCallable {
            database.runInTransaction {
                database.occupationDao().insert(occupationList)
            }
        }.processRequest(
                { saved ->
                    logD("Occupation Saved Successfully !")
                },
                { err ->
                    logE("Error : $err")
                }
        ))



        var literacyList = Literacy()
        literacyList.id = 123
        literacyList.name = "Padhaku"
        compositeDisposable.add(Single.fromCallable {
            database.runInTransaction {
                database.literacyDao().insert(literacyList)
            }
        }.processRequest(
                { saved ->
                    logD("Literacy Saved Successfully !")
                },
                { err ->
                    logE("Error : $err")
                }
        ))



        var primaryKycList = PrimaryKYC()
        primaryKycList.id = 123
        primaryKycList.name = "Adhaar Card"
        compositeDisposable.add(Single.fromCallable {
            database.runInTransaction {
                database.primaryKycDao().insert(primaryKycList)
            }
        }.processRequest(
                { saved ->
                    logD("Primary KYC Saved Successfully !")
                },
                { err ->
                    logE("Error : $err")
                }
        ))



        var secondaryKycList = SecondaryKYC()
        secondaryKycList.id = 123
        secondaryKycList.name = "Voter Id Card"
        compositeDisposable.add(Single.fromCallable {
            database.runInTransaction {
                database.secondaryKycDao().insert(secondaryKycList)
            }
        }.processRequest(
                { saved ->
                    logD("Secondary Kyc Saved Successfully !")
                },
                { err ->
                    logE("Error : $err")
                }
        ))



        var loanPurposeList = LoanPurpose()
        loanPurposeList.id = 123
        loanPurposeList.name = "Loan Kha k Bhaag jana"
        compositeDisposable.add(Single.fromCallable {
            database.runInTransaction {
                database.loanPurposeDao().insert(loanPurposeList)
            }
        }.processRequest(
                { saved ->
                    logD("Loan Purpose Saved Successfully !")
                },
                { err ->
                    logE("Error : $err")
                }
        ))



    }

    fun getData(view: View){
        val district = database.districtDao().getAllDistrict()
        for (data in district){
            logD("District Id : ${data.id} District Name : ${data.name}")
        }

        val relation = database.relationDao().getAllRelation()
        var gender : String
        for (data in relation){
            gender = if (data.gender?.toInt() == 0){
                "Male"
            }else {
                "Female"
            }
            logD("Relation Id : ${data.id} Relation Name : ${data.name} Gender : $gender")
        }

        val occupation = database.occupationDao().getAllOccupation()
        for (data in occupation){
            logD("Occupation Id : ${data.id} Occupation Name : ${data.name}")
        }

        val literacy = database.literacyDao().getAllLiteracy()
        for (data in literacy){
            logD("Literacy Id : ${data.id} Literacy Name : ${data.name}")
        }

        val primaryKyc = database.primaryKycDao().getAllPrimaryKyc()
        for (data in primaryKyc){
            logD("PrimaryKyc Id : ${data.id} PrimaryKyc Name : ${data.name}")
        }

        val secondaryKyc = database.secondaryKycDao().getAllSecondaryKyc()
        for (data in secondaryKyc){
            logD("SecondaryKyc Id : ${data.id} SecondaryKyc Name : ${data.name}")
        }

        val loanPurpose = database.loanPurposeDao().getAllLoanPurpose()
        for (data in loanPurpose){
            logD("LoanPurpose Id : ${data.id} LoanPurpose Name : ${data.name}")
        }
    }
}
