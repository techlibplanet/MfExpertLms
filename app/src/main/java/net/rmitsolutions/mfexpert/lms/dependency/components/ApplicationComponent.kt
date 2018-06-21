package net.rmitsolutions.mfexpert.lms.dependency.components

import android.content.Context
import dagger.Component
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.dependency.modules.AppContextModule
import net.rmitsolutions.mfexpert.lms.dependency.modules.DatabaseModule
import net.rmitsolutions.mfexpert.lms.dependency.modules.NetworkApiModule
import net.rmitsolutions.mfexpert.lms.dependency.qualifiers.ApplicationContextQualifier
import net.rmitsolutions.mfexpert.lms.dependency.scopes.ApplicationScope
import net.rmitsolutions.mfexpert.lms.network.IMasters
import okhttp3.OkHttpClient


/**
 * Created by Madhu on 24-Apr-2018.
 */
@ApplicationScope
@Component(modules = arrayOf(AppContextModule::class, NetworkApiModule::class, DatabaseModule::class))
interface ApplicationComponent {
    @ApplicationContextQualifier
    fun getAppContext(): Context

    fun getOkHttpClient(): OkHttpClient
    fun getDatabase(): MfExpertLmsDatabase

    fun getMasterService(): IMasters

}