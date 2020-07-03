package eu.yeger.koffee.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import eu.yeger.koffee.domain.JWT
import kotlinx.coroutines.flow.Flow

/**
 * [Dao](https://developer.android.com/reference/androidx/room/Dao) that manages [JWT]s in the database.
 *
 * @author Jan Müller
 */
@Dao
interface JWTDao : BaseDao<JWT> {

    @Query("SELECT * FROM jwt LIMIT 1")
    suspend fun get(): JWT?

    @Query("SELECT * FROM jwt LIMIT 1")
    fun getAsFlow(): Flow<JWT?>

    @Query("DELETE FROM jwt")
    suspend fun deleteAll()

    @Transaction
    suspend fun upsert(jwt: JWT) {
        deleteAll()
        insert(jwt)
    }
}
