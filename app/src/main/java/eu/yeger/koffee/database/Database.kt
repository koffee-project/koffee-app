package eu.yeger.koffee.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.domain.ProfileImage
import eu.yeger.koffee.domain.User

/**
 * The [Room](https://developer.android.com/jetpack/androidx/releases/room) database with all its [Dao](https://developer.android.com/reference/androidx/room/Dao)s.
 */
@Database(
    entities = [
        DatabaseTransaction::class,
        Item::class,
        JWT::class,
        ProfileImage::class,
        User::class
    ],
    version = 14,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class KoffeeDatabase : RoomDatabase() {

    /**
     * The database's [Item] [Dao](https://developer.android.com/reference/androidx/room/Dao).
     */
    abstract val itemDao: ItemDao

    /**
     * The database's [JWT] [Dao](https://developer.android.com/reference/androidx/room/Dao).
     */
    abstract val jwtDao: JWTDao

    /**
     * The database's [ProfileImage] [Dao](https://developer.android.com/reference/androidx/room/Dao).
     */
    abstract val profileImageDao: ProfileImageDao

    /**
     * The database's [DatabaseTransaction] [Dao](https://developer.android.com/reference/androidx/room/Dao).
     */
    abstract val transactionDao: TransactionDao

    /**
     * The database's [User] [Dao](https://developer.android.com/reference/androidx/room/Dao).
     */
    abstract val userDao: UserDao
}

private lateinit var instance: KoffeeDatabase

/**
 * Creates or returns this app's [KoffeeDatabase].
 *
 * @param context The context of the app.
 * @return The [KoffeeDatabase].
 *
 * @author Jan Müller
 */
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
