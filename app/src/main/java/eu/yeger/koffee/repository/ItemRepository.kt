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

class ItemRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    fun getAllItems(): DataSource.Factory<Int, Item> {
        return database.itemDao.getAllPaged()
    }

    fun getFilteredItems(filter: Filter): DataSource.Factory<Int, Item> {
        return database.itemDao.getFilteredPaged(filter.nameFragment)
    }

    suspend fun hasItemWithId(id: String?): Boolean {
        return getItemById(id) !== null
    }

    suspend fun getItemById(id: String?): Item? {
        return withContext(Dispatchers.IO) {
            database.itemDao.getById(id)
        }
    }

    fun getItemByIdFlow(id: String?): Flow<Item?> {
        return database.itemDao.getByIdAsFlow(id)
            .distinctUntilChanged()
    }

    suspend fun fetchItems() {
        withContext(Dispatchers.IO) {
            val response = NetworkService.koffeeApi.getItems()
            val items = response.asDomainModel()
            database.itemDao.upsertAll(items)
        }
    }

    suspend fun fetchItemById(itemId: String) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeItemById(itemId) }) {
                val response = NetworkService.koffeeApi.getItemById(itemId)
                val item = response!!.asDomainModel()
                database.itemDao.insertAll(item)
            }
        }
    }

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

    suspend fun deleteItem(itemId: String, jwt: JWT) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeItemById(itemId) }) {
                NetworkService.koffeeApi.deleteItem(itemId, jwt.formatToken())
                database.itemDao.deleteById(itemId)
            }
        }
    }
}
