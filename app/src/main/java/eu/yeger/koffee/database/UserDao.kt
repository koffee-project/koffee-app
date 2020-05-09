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
    fun insert(user: User)

    @Query("SELECT * FROM user WHERE id == :id")
    fun getById(id: String?): User?

    @Query("SELECT * FROM user WHERE id == :id")
    fun getByIdAsLiveData(id: String?): LiveData<User?>

    @Query("DELETE FROM user")
    fun deleteAll()
}
