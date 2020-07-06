package eu.yeger.koffee.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import eu.yeger.koffee.domain.Item
import kotlinx.coroutines.flow.Flow

/**
 * [Dao](https://developer.android.com/reference/androidx/room/Dao) that manages [Item]s in the database.
 *
 * @author Jan Müller
 */
@Dao
interface ItemDao : BaseDao<Item> {

    /**
     * Returns all [Item]s as a paging factory.
     *
     * @return The paging factory.
     */
    @Query("SELECT * FROM item ORDER BY name ASC")
    fun getAllPaged(): DataSource.Factory<Int, Item>

    /**
     * Returns the [Item] with the given id.
     *
     * @return The [Item].
     */
    @Query("SELECT * FROM item WHERE id == :id")
    suspend fun getById(id: String?): Item?

    /**
     * Returns a Flow of the [Item] with the given id.
     *
     * @return The Flow.
     */
    @Query("SELECT * FROM item WHERE id == :id")
    fun getByIdAsFlow(id: String?): Flow<Item?>

    /**
     * Returns filtered [Item]s as a paging factory.
     *
     * @param nameFilter The filter of the query.
     *
     * @return The paging factory.
     */
    @Query("SELECT * FROM item WHERE name LIKE :nameFilter ORDER BY name ASC")
    fun getFilteredPaged(nameFilter: String): DataSource.Factory<Int, Item>

    /**
     * Deletes all [Item]s.
     */
    @Query("DELETE FROM item")
    suspend fun deleteAll()

    /**
     * Deletes the [Item] with the given id.
     *
     * @param id The id of the [Item] to be deleted.
     */
    @Query("DELETE FROM item WHERE id == :id")
    suspend fun deleteById(id: String)

    /**
     * Clears the database and then inserts multiple [Item]s into the database.
     *
     * @param items The [Item]s to be inserted.
     */
    @Transaction
    suspend fun upsertAll(items: Collection<Item>) {
        deleteAll()
        insertAll(*items.toTypedArray())
    }
}
