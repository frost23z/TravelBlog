package me.zayedbinhasan.travelblog

import android.app.Application
import me.zayedbinhasan.travelblog.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.androix.startup.KoinStartup
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.koinConfiguration

@OptIn(KoinExperimentalAPI::class)
class TravelBlogApplication : Application(), KoinStartup {
    override fun onKoinStartup() = koinConfiguration {
        androidContext(this@TravelBlogApplication)
        modules(appModule)
    }
}