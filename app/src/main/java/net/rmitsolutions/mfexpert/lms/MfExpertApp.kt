package net.rmitsolutions.mfexpert.lms

import android.app.Application
import net.rmitsolutions.mfexpert.lms.dependency.components.ApplicationComponent
import net.rmitsolutions.mfexpert.lms.dependency.components.DaggerApplicationComponent
import net.rmitsolutions.mfexpert.lms.dependency.modules.AppContextModule
import net.rmitsolutions.mfexpert.lms.network.AuthorizedApiService
import net.rmitsolutions.mfexpert.lms.network.TokenService
import retrofit2.Retrofit
import timber.log.Timber
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class MfExpertApp : Application() {
    companion object {
        lateinit var applicationComponent: ApplicationComponent
        lateinit var retrofit : Retrofit
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        applicationComponent = DaggerApplicationComponent.builder()
                .appContextModule(AppContextModule(applicationContext))
                .build()

        //set default font
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Raleway_Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        )
    }

    fun setRetrofit(accessToken: String) {
        retrofit = AuthorizedApiService.buildRetrofit(accessToken)
    }
}