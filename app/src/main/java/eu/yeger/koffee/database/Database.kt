package eu.yeger.koffee.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import eu.yeger.koffee.domain.UserListEntry

@Database(
    entities = [UserListEntry::class],
    version = 1,
    exportSchema = false
)
abstract class KoffeeDatabase : RoomDatabase() {

    abstract val userListEntryDao: UserListEntryDao
}

private lateinit var instance: KoffeeDatabase

fun getDatabase(context: Context): KoffeeDatabase {
    synchronized(KoffeeDatabase::class.java) {
        if (!::instance.isInitialized) {
            instance = Room
                .databaseBuilder(
                    context.applicationContext,
                    KoffeeDatabase::class.java,
                    "KoffeeDatabase"
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return instance
}
