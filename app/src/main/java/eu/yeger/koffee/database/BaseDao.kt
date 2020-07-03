package eu.yeger.koffee.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy

/**
 * Base [Dao](https://developer.android.com/reference/androidx/room/Dao) that supports [insert] and [insertAll] operation.
 *
 * @author Jan Müller
 */
interface BaseDao<T> {

    /**
     * Inserts a single entity into the database. Replaces conflicting entities.
     *
     * @param entity The entity to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    /**
     * Inserts multiple entities into the database. Replaces conflicting entities.
     *
     * @param entities The entities to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entities: T)
}
