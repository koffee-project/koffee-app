package eu.yeger.koffee.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import eu.yeger.koffee.domain.JWT
import kotlinx.coroutines.flow.Flow

/**
 * [Dao](https://developer.android.com/reference/androidx/room/Dao) that manages [JWT]s in the database.
 * Should only contain one [JWT] at a time.
 *
 * @author Jan Müller
 */
@Dao
interface JWTDao : BaseDao<JWT> {

    /**
     * Returns the [JWT].
     *
     * @return The [JWT].
     */
    @Query("SELECT * FROM jwt LIMIT 1")
    suspend fun get(): JWT?

    /**
     * Returns a Flow of the [JWT].
     *
     * @return The Flow.
     */
    @Query("SELECT * FROM jwt LIMIT 1")
    fun getAsFlow(): Flow<JWT?>

    /**
     * Deletes all [JWT]s.
     */
    @Query("DELETE FROM jwt")
    suspend fun deleteAll()

    /**
     * Clears the database and then inserts a single [JWT]s into the database.
     *
     * @param jwt The [JWT] to be inserted.
     */
    @Transaction
    suspend fun upsert(jwt: JWT) {
        deleteAll()
        insert(jwt)
    }
}
