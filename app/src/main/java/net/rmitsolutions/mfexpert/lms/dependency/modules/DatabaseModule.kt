package net.rmitsolutions.mfexpert.lms.dependency.modules

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import net.rmitsolutions.mfexpert.lms.database.MfExpertLmsDatabase
import net.rmitsolutions.mfexpert.lms.dependency.qualifiers.ApplicationContextQualifier
import net.rmitsolutions.mfexpert.lms.dependency.scopes.ApplicationScope

/**
 * Created by Madhu on 24-Apr-2018.
 */
@Module(includes = arrayOf(AppContextModule::class))
class DatabaseModule {
    @Provides
    @ApplicationScope
    fun mfExpertLmsDatabase(@ApplicationContextQualifier context: Context): MfExpertLmsDatabase {
        return Room.databaseBuilder(context, MfExpertLmsDatabase::class.java, "mfexpert-lms-db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }
}