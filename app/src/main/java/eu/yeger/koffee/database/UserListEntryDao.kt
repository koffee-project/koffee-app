package eu.yeger.koffee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.yeger.koffee.domain.UserListEntry

@Dao
interface UserListEntryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg userListEntries: UserListEntry)

    @Query("SELECT * FROM userlistentry")
    fun getAll(): List<UserListEntry>

    @Query("SELECT * FROM userlistentry")
    fun getAllAsLiveData(): LiveData<List<UserListEntry>>
}
