package eu.yeger.koffee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.yeger.koffee.domain.JWT

@Dao
interface JWTDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(jwt: JWT)

    @Query("SELECT * FROM jwt LIMIT 1")
    fun get(): JWT?

    @Query("SELECT * FROM jwt LIMIT 1")
    fun getAsLiveData(): LiveData<JWT?>

    @Query("DELETE FROM jwt")
    fun deleteAll()
}
