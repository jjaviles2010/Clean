package com.fiap18Mob.clean

import android.app.Application
import com.facebook.stetho.Stetho
import com.fiap18Mob.clean.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    companion object {
        lateinit var instance: MyApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    viewModelModule,
                    uiModule,
                    networkModule,
                    repositoryModule,
                    dbModule
                )
            )
        }
    }
}