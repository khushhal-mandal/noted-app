package org.khushhal.noted.di

import org.khushhal.noted.util.AppDatabase
import org.khushhal.noted.util.DatabaseFactory
import org.khushhal.noted.util.UserDao
import org.khushhal.noted.util.getDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule = module {
    single<AppDatabase> {
        val factory = DatabaseFactory()
        val builder = factory.createDatabase()
        getDatabase(builder)
    }
    single<UserDao> { get<AppDatabase>().userDao() }
}