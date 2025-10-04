package org.khushhal.noted.util

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

actual class DatabaseFactory(var ctx: Context) {
    actual fun createDatabase(): RoomDatabase.Builder<AppDatabase> {
        val appContext = ctx.applicationContext
        val dbFile = appContext.getDatabasePath("my_room.db")
        return Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}