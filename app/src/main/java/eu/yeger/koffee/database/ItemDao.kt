package eu.yeger.koffee.database

import androidx.paging.DataSource
import androidx.room.*
import eu.yeger.koffee.domain.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao : BaseDao<Item> {

    @Query("SELECT * FROM item ORDER BY name ASC")
    fun getAllPaged(): DataSource.Factory<Int, Item>

    @Query("SELECT * FROM item WHERE id == :id")
    suspend fun getById(id: String?): Item?

    @Query("SELECT * FROM item WHERE id == :id")
    fun getByIdAsFlow(id: String?): Flow<Item?>

    @Query("SELECT * FROM item WHERE name LIKE :nameFilter ORDER BY name ASC")
    fun getFilteredPaged(nameFilter: String): DataSource.Factory<Int, Item>

    @Query("DELETE FROM item")
    suspend fun deleteAll()

    @Query("DELETE FROM item WHERE id == :id")
    suspend fun deleteById(id: String)

    @Transaction
    suspend fun upsertAll(items: Collection<Item>) {
        deleteAll()
        insertAll(*items.toTypedArray())
    }
}
