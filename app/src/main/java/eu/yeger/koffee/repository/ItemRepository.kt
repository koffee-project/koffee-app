package eu.yeger.koffee.repository

import android.content.Context
import androidx.paging.DataSource
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.network.ApiItemDTO
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDomainModel
import eu.yeger.koffee.network.formatToken
import eu.yeger.koffee.utility.onNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

/**
 * Repository for [Item]s.
 *
 * @property database The [KoffeeDatabase] used by this repository.
 *
 * @author Jan Müller
 */
class ItemRepository(private val database: KoffeeDatabase) {

    /**
     * Utility constructor for improved readability.
     */
    constructor(context: Context) : this(getDatabase(context))

    /**
     * Returns all [Item]s from [KoffeeDatabase] as pages.
     *
     * @return The paging factory.
     */
    fun getAllItems(): DataSource.Factory<Int, Item> {
        return database.itemDao.getAllPaged()
    }

    /**
     * Returns filtered [Item]s from [KoffeeDatabase] as pages.
     *
     * @param filter The filter used for the query.
     *
     * @return The paging factory.
     */
    fun getFilteredItems(filter: Filter): DataSource.Factory<Int, Item> {
        return database.itemDao.getFilteredPaged(filter.nameFragment)
    }

    /**
     * Checks if [KoffeeDatabase] has the [Item] with the given id.
     *
     * @param id The id of the [Item].
     * @return true if [KoffeeDatabase] contains the [Item].
     */
    suspend fun hasItemWithId(id: String?): Boolean {
        return getItemById(id) !== null
    }

    /**
     * Returns the [Item] with the given id from [KoffeeDatabase].
     *
     * @param id The id of the [Item].
     * @return The [Item].
     */
    suspend fun getItemById(id: String?): Item? {
        return withContext(Dispatchers.IO) {
            database.itemDao.getById(id)
        }
    }

    /**
     * Returns a distinct Flow of the [Item] with the given id from [KoffeeDatabase].
     *
     * @param id The id of the [Item].
     * @return The Flow.
     */
    fun getItemByIdFlow(id: String?): Flow<Item?> {
        return database.itemDao.getByIdAsFlow(id)
            .distinctUntilChanged()
    }

    /**
     * Fetches all [Item]s from [NetworkService] and inserts them into [KoffeeDatabase].
     */
    suspend fun fetchItems() {
        withContext(Dispatchers.IO) {
            val response = NetworkService.koffeeApi.getItems()
            val items = response.asDomainModel()
            database.itemDao.upsertAll(items)
        }
    }

    /**
     * Fetches the [Item] with the given id from [NetworkService] and inserts it into [KoffeeDatabase].
     * Purges this [Item] from [KoffeeDatabase] if it no longer exists.
     *
     * @param itemId The id of the [Item] to be fetched.
     */
    suspend fun fetchItemById(itemId: String) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeItemById(itemId) }) {
                val response = NetworkService.koffeeApi.getItemById(itemId)
                val item = response.asDomainModel()
                database.itemDao.insertAll(item)
            }
        }
    }

    /**
     * Requests the creation of an [Item] with the given data.
     *
     * @param itemId The id of the [Item] to be created.
     * @param itemName The name of the [Item] to be created.
     * @param itemPrice The price of the [Item] to be created.
     * @param itemAmount The amount of the [Item] to be created.
     * @param jwt The authentication token.
     */
    suspend fun createItem(
        itemId: String?,
        itemName: String,
        itemPrice: Double,
        itemAmount: Int?,
        jwt: JWT
    ): String {
        return withContext(Dispatchers.IO) {
            val itemDTO = ApiItemDTO(
                id = itemId,
                name = itemName,
                price = itemPrice,
                amount = itemAmount
            )
            NetworkService.koffeeApi.createItem(itemDTO, jwt.formatToken())
        }
    }

    /**
     * Requests an update of the [Item] with the given data.
     *
     * @param itemId The id of the [Item] to be updated.
     * @param itemName The name of the [Item] to be updated.
     * @param itemPrice The price of the [Item] to be updated.
     * @param itemAmount The amount of the [Item] to be updated.
     * @param jwt The authentication token.
     */
    suspend fun updateItem(
        itemId: String,
        itemName: String,
        itemPrice: Double,
        itemAmount: Int?,
        jwt: JWT
    ) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeItemById(itemId) }) {
                val itemDTO = ApiItemDTO(
                    id = itemId,
                    name = itemName,
                    price = itemPrice,
                    amount = itemAmount
                )
                NetworkService.koffeeApi.updateItem(itemDTO, jwt.formatToken())
            }
        }
    }

    /**
     * Requests the deletion of the [Item] with the given id.
     * Purges this [Item] from [KoffeeDatabase] if it no longer exists.
     *
     * @param itemId The id of the [Item] to be deleted.
     * @param jwt The authentication token.
     */
    suspend fun deleteItem(itemId: String, jwt: JWT) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeItemById(itemId) }) {
                NetworkService.koffeeApi.deleteItem(itemId, jwt.formatToken())
                database.itemDao.deleteById(itemId)
            }
        }
    }
}
