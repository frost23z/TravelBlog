package me.zayedbinhasan.travelblog.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import me.zayedbinhasan.travelblog.navigation.DefaultNavigator
import me.zayedbinhasan.travelblog.navigation.Destination
import me.zayedbinhasan.travelblog.navigation.Navigator
import me.zayedbinhasan.travelblog.preference.PreferencesManager
import me.zayedbinhasan.travelblog.ui.screen.login.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<Navigator> { DefaultNavigator(startDestination = Destination.AuthDestination) }
    single { PreferencesManager(dataStore = get()) }
    singleOf(::DefaultNavigator)
    viewModelOf(::LoginViewModel)
    singleOf(::provideDataStore)

}

fun provideDataStore(context: Context): DataStore<Preferences> = PreferenceDataStoreFactory.create {
    context.preferencesDataStoreFile("dataStore")
}