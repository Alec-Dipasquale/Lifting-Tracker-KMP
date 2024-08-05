package com.squalec.liftingtracker.android

import android.app.Application
import com.squalec.liftingtracker.di.databaseModule
import com.squalec.liftingtracker.di.koinModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

             Timber.plant(Timber.DebugTree())

        startKoin {
            androidContext(this@MainApplication)
            modules(androidModule(), databaseModule)
        }
    }
}