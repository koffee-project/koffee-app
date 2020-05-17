package eu.yeger.koffee.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.yeger.koffee.domain.JWT
import kotlinx.coroutines.flow.Flow

@Dao
interface JWTDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(jwt: JWT)

    @Query("SELECT * FROM jwt LIMIT 1")
    suspend fun get(): JWT?

    @Query("SELECT * FROM jwt LIMIT 1")
    fun getAsFlow(): Flow<JWT?>

    @Query("DELETE FROM jwt")
    suspend fun deleteAll()
}
