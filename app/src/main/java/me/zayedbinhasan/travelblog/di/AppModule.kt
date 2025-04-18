package me.zayedbinhasan.travelblog.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.AndroidClientEngine
import io.ktor.client.engine.android.AndroidEngineConfig
import me.zayedbinhasan.travelblog.data.local.LocalDatabase
import me.zayedbinhasan.travelblog.data.remote.RemoteHttpClient
import me.zayedbinhasan.travelblog.data.remote.createHttpClient
import me.zayedbinhasan.travelblog.data.repository.RepositoryImpl
import me.zayedbinhasan.travelblog.domain.repository.Repository
import me.zayedbinhasan.travelblog.navigation.DefaultNavigator
import me.zayedbinhasan.travelblog.navigation.Destination
import me.zayedbinhasan.travelblog.navigation.Navigator
import me.zayedbinhasan.travelblog.preference.PreferencesManager
import me.zayedbinhasan.travelblog.ui.screen.list.ListViewModel
import me.zayedbinhasan.travelblog.ui.screen.login.LoginViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<Navigator> { DefaultNavigator(startDestination = Destination.AuthDestination) }

    single { PreferencesManager(dataStore = get()) }
    singleOf(::provideDataStore)

    single<LocalDatabase> { LocalDatabase.getDatabase(androidContext()) }
    single<RemoteHttpClient> { RemoteHttpClient(createHttpClient(AndroidClientEngine(AndroidEngineConfig()))) }

    singleOf(::RepositoryImpl) { bind<Repository>() }

    viewModelOf(::LoginViewModel)
    viewModelOf(::ListViewModel)
}

fun provideDataStore(context: Context): DataStore<Preferences> = PreferenceDataStoreFactory.create {
    context.preferencesDataStoreFile("dataStore")
}