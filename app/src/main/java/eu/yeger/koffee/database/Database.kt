package eu.yeger.koffee.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.domain.UserEntry

@Database(
    entities = [UserEntry::class, User::class, DatabaseTransaction::class, Item::class],
    version = 8,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KoffeeDatabase : RoomDatabase() {

    abstract val itemDao: ItemDao

    abstract val userDao: UserDao

    abstract val userEntryDao: UserEntryDao

    abstract val transactionDao: TransactionDao
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
