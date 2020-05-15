package eu.yeger.koffee.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.domain.User

@Database(
    entities = [
        DatabaseTransaction::class,
        Item::class,
        JWT::class,
        User::class
    ],
    version = 10,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KoffeeDatabase : RoomDatabase() {

    abstract val itemDao: ItemDao

    abstract val jwtDao: JWTDao

    abstract val transactionDao: TransactionDao

    abstract val userDao: UserDao
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
