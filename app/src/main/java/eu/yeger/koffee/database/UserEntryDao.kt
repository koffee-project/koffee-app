package eu.yeger.koffee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.yeger.koffee.domain.UserEntry

@Dao
interface UserEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg userEntries: UserEntry)

    @Query("SELECT * FROM userentry ORDER BY name ASC")
    fun getAll(): List<UserEntry>

    @Query("SELECT * FROM userentry ORDER BY name ASC")
    fun getAllAsLiveData(): LiveData<List<UserEntry>>

    @Query("SELECT * FROM userentry WHERE name LIKE :nameFilter ORDER BY name ASC")
    fun getFilteredAsLiveData(nameFilter: String): LiveData<List<UserEntry>>

    @Query("DELETE FROM userentry")
    fun deleteAll()

    @Query("DELETE FROM userentry WHERE id == :id")
    fun deleteById(id: String)
}
