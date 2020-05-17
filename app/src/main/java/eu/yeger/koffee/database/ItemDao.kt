package eu.yeger.koffee.database

import androidx.room.*
import eu.yeger.koffee.domain.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao : BaseDao<Item> {

    @Query("SELECT * FROM item ORDER BY name ASC")
    fun getAllAsFlow(): Flow<List<Item>>

    @Query("SELECT * FROM item WHERE id == :id")
    suspend fun getById(id: String?): Item?

    @Query("SELECT * FROM item WHERE id == :id")
    fun getByIdAsFlow(id: String?): Flow<Item?>

    @Query("SELECT * FROM item WHERE name LIKE :nameFilter ORDER BY name ASC")
    fun getFilteredAsFlow(nameFilter: String): Flow<List<Item>>

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
