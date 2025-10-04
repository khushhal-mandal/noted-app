package org.khushhal.noted.util

import androidx.room.ConstructedBy
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.room.Upsert
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

@Entity(tableName = "user")
data class UserEntity (
    @PrimaryKey val key: Int,
    val email: String? = null,
    val password: String,
    var token: String
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Upsert
    suspend fun upsertUser(user: UserEntity)

    @Query("DELETE FROM user")
    suspend fun clearUser()
}

@Database(entities = [UserEntity::class], version = 1, exportSchema = true)
@ConstructedBy(AppDatabaseConstructor::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    companion object {
        const val DATABASE_NAME = "my_room.db"
    }
}

@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object AppDatabaseConstructor : RoomDatabaseConstructor<AppDatabase> {
    override fun initialize(): AppDatabase
}

expect class DatabaseFactory {
    fun createDatabase(): RoomDatabase.Builder<AppDatabase>
}

fun getDatabase(builder: RoomDatabase.Builder<AppDatabase>): AppDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .fallbackToDestructiveMigration(true)
        .build()
}


class UserRepository(private val dao: UserDao) {
    suspend fun saveUser(email: String, password: String, token: String) {
        val user = UserEntity(
            key = 1, // always 1 so we only store one user
            email = email,
            password = password,
            token = token
        )
        dao.upsertUser(user)
    }

    suspend fun getUser(): UserEntity? {
        return dao.getUser()
    }

    suspend fun clearUser() {
        dao.clearUser()
    }
}


class UserPreferences {
}