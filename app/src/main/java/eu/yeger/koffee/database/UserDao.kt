package eu.yeger.koffee.database

import androidx.room.*
import eu.yeger.koffee.domain.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg user: User)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    @Query("SELECT * FROM user WHERE id == :id")
    suspend fun getById(id: String?): User?

    @Query("SELECT * FROM user ORDER BY name ASC")
    fun getAllAsFlow(): Flow<List<User>>

    @Query("SELECT * FROM user WHERE id == :id")
    fun getByIdAsFlow(id: String?): Flow<User?>

    @Query("SELECT * FROM user WHERE name LIKE :nameFilter ORDER BY name ASC")
    fun getFilteredAsFlow(nameFilter: String): Flow<List<User>>

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("DELETE FROM user WHERE id == :id")
    suspend fun deleteById(id: String)

    @Transaction
    suspend fun updateUsers(users: Collection<User>) {
        deleteAll()
        insertAll(*users.toTypedArray())
    }
}
