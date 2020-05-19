package eu.yeger.koffee.database

import androidx.paging.DataSource
import androidx.room.*
import eu.yeger.koffee.domain.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao : BaseDao<User> {

    @Query("SELECT * FROM user ORDER BY name ASC")
    fun getAllPaged(): DataSource.Factory<Int, User>

    @Query("SELECT * FROM user WHERE id == :id")
    suspend fun getById(id: String?): User?

    @Query("SELECT * FROM user WHERE id == :id")
    fun getByIdAsFlow(id: String?): Flow<User?>

    @Query("SELECT * FROM user WHERE name LIKE :nameFilter ORDER BY name ASC")
    fun getFilteredPaged(nameFilter: String): DataSource.Factory<Int, User>

    @Query("DELETE FROM user")
    suspend fun deleteAll()

    @Query("DELETE FROM user WHERE id == :id")
    suspend fun deleteById(id: String)

    @Transaction
    suspend fun upsertAll(users: Collection<User>) {
        deleteAll()
        insertAll(*users.toTypedArray())
    }
}
