package com.fiap18Mob.clean

import android.app.Application
import com.facebook.stetho.Stetho
import com.fiap18Mob.clean.di.networkModule
import com.fiap18Mob.clean.di.repositoryModule
import com.fiap18Mob.clean.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    viewModelModule,
                    networkModule,
                    repositoryModule
                )
            )
        }
    }
}