package com.example.hotpot

import com.example.hotpot.di.appModule
import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class HotpotApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@HotpotApplication)
            modules(appModule)
            printLogger(org.koin.core.logger.Level.DEBUG)
        }

    }
}