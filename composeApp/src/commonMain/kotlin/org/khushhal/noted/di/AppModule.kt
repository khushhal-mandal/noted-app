package org.khushhal.noted.di

import org.khushhal.noted.data.remote.AuthApi
import org.khushhal.noted.data.remote.NotesApi
import org.khushhal.noted.data.remote.network.HttpClientFactory
import org.khushhal.noted.data.repository.AuthRepositoryImpl
import org.khushhal.noted.data.repository.NotesRepositoryImpl
import org.khushhal.noted.domain.repository.AuthRepository
import org.khushhal.noted.domain.repository.NotesRepository
import org.khushhal.noted.presentation.auth.AuthScreenModel
import org.khushhal.noted.presentation.notes.NotesScreenModel
import org.khushhal.noted.util.UserRepository
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

expect val platformModule: Module
val sharedModule = module {
    single { UserRepository(get()) }

    // HttpClient singleton
    single { HttpClientFactory.client }

    // APIs
    single { AuthApi(get()) }
    single { NotesApi(get()) } // NotesApi uses CurrentUser internally, no parameter needed

    // Repositories
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<NotesRepository> { NotesRepositoryImpl(get()) }

    // ViewModels
    factory { NotesScreenModel(get(), get()) }
    factory { AuthScreenModel(get(), get()) }
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, platformModule)
    }
}
