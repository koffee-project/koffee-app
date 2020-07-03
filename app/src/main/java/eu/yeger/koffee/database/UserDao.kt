package eu.yeger.koffee.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import eu.yeger.koffee.domain.User
import kotlinx.coroutines.flow.Flow

/**
 * [Dao](https://developer.android.com/reference/androidx/room/Dao) that manages [User]s in the database.
 *
 * @author Jan Müller
 */
@Dao
interface UserDao : BaseDao<User> {

    /**
     * Returns all [User]s from the database as a paging factory.
     *
     * @return The paging factory.
     */
    @Query("SELECT * FROM user ORDER BY name ASC")
    fun getAllPaged(): DataSource.Factory<Int, User>

    /**
     * Returns the [User] with the given id.
     *
     * @return The [User].
     */
    @Query("SELECT * FROM user WHERE id == :id")
    suspend fun getById(id: String?): User?

    /**
     * Returns a Flow of the [User] with the given id.
     *
     * @return The Flow.
     */
    @Query("SELECT * FROM user WHERE id == :id")
    fun getByIdAsFlow(id: String?): Flow<User?>

    /**
     * Returns filtered [User]s from the database as a paging factory.
     *
     * @param nameFilter The filter of the query.
     *
     * @return The paging factory.
     */
    @Query("SELECT * FROM user WHERE name LIKE :nameFilter ORDER BY name ASC")
    fun getFilteredPaged(nameFilter: String): DataSource.Factory<Int, User>

    /**
     * Deletes all [User]s from the database.
     */
    @Query("DELETE FROM user")
    suspend fun deleteAll()

    /**
     * Deletes the [User] with the given id from the database.
     *
     * @param id The id of the [User] to be deleted.
     */
    @Query("DELETE FROM user WHERE id == :id")
    suspend fun deleteById(id: String)

    /**
     * Clears the database and then inserts multiple [User]s into the database.
     *
     * @param users The [User]s to be inserted.
     */
    @Transaction
    suspend fun upsertAll(users: Collection<User>) {
        deleteAll()
        insertAll(*users.toTypedArray())
    }
}
