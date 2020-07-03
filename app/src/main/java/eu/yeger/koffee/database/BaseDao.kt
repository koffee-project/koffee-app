package eu.yeger.koffee.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy

/**
 * Base [Dao](https://developer.android.com/reference/androidx/room/Dao) that supports [insert] and [insertAll] operation.
 *
 * @author Jan Müller
 */
interface BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entities: T)
}
