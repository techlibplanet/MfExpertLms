package net.rmitsolutions.mfexpert.lms.dependency.modules

import dagger.Module
import dagger.Provides
import net.rmitsolutions.mfexpert.lms.dependency.modules.HttpModule
import net.rmitsolutions.mfexpert.lms.dependency.scopes.ApplicationScope
import net.rmitsolutions.mfexpert.lms.network.IMasters
import net.rmitsolutions.mfexpert.lms.network.IUser
import retrofit2.Retrofit


/**
 * Created by Madhu on 24-Apr-2018.
 */
@Module(includes = arrayOf(HttpModule::class))
class NetworkApiModule {
    @Provides
    @ApplicationScope
    fun mastersService(retrofit: Retrofit): IMasters {
        return retrofit.create(IMasters::class.java)
    }

    @Provides
    @ApplicationScope
    fun tokenService(retrofit: Retrofit): IUser {
        return retrofit.create(IUser::class.java)
    }


}