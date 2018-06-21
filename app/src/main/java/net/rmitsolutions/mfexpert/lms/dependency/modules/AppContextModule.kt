package net.rmitsolutions.mfexpert.lms.dependency.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import net.rmitsolutions.mfexpert.lms.dependency.qualifiers.ApplicationContextQualifier
import net.rmitsolutions.mfexpert.lms.dependency.scopes.ApplicationScope

/**
 * Created by Madhu on 24-Apr-2018.
 */

@Module
class AppContextModule(@ApplicationContextQualifier val context: Context) {
    @Provides
    @ApplicationScope
    @ApplicationContextQualifier
    fun provideContext(): Context {
        return context
    }
}