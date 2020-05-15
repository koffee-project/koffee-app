package eu.yeger.koffee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.yeger.koffee.domain.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

    @Query("SELECT * FROM user WHERE id == :id")
    fun getById(id: String?): User?

    @Query("SELECT * FROM user ORDER BY name ASC")
    fun getAllAsLiveData(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE id == :id")
    fun getByIdAsLiveData(id: String?): LiveData<User?>

    @Query("SELECT * FROM user WHERE name LIKE :nameFilter ORDER BY name ASC")
    fun getFilteredAsLiveData(nameFilter: String): LiveData<List<User>>

    @Query("DELETE FROM user")
    fun deleteAll()

    @Query("DELETE FROM user WHERE id == :id")
    fun deleteById(id: String)
}
