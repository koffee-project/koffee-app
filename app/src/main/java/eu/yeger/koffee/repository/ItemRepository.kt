package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.network.ApiItemDTO
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDomainModel
import eu.yeger.koffee.network.formatToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    private val _state = MutableLiveData<RepositoryState>(RepositoryState.Idle)
    val state: LiveData<RepositoryState> = _state

    val items = database.itemDao.getAllAsLiveData()

    suspend fun hasItemWithId(id: String?): Boolean {
        return withContext(Dispatchers.IO) {
            database.itemDao.getById(id) !== null
        }
    }

    suspend fun getItemById(id: String?): Item? {
        return withContext(Dispatchers.IO) {
            database.itemDao.getById(id)
        }
    }

    fun getItemByIdAsLiveData(id: String?): LiveData<Item?> {
        return database.itemDao.getByIdAsLiveData(id)
    }

    suspend fun refreshItems() {
        withContext(Dispatchers.IO) {
            try {
                _state.postValue(RepositoryState.Refreshing)
                val response = NetworkService.koffeeApi.getItems()
                val items = response.data.asDomainModel()
                database.itemDao.insertAll(*items.toTypedArray())
                _state.postValue(RepositoryState.Done)
            } catch (exception: Exception) {
                _state.postValue(RepositoryState.Error(exception))
            }
        }
    }

    suspend fun refreshItemById(itemId: String) {
        withContext(Dispatchers.IO) {
            try {
                _state.postValue(RepositoryState.Refreshing)
                val response = NetworkService.koffeeApi.getItemById(itemId)
                val item = response.data!!.asDomainModel()
                database.itemDao.insertAll(item)
                _state.postValue(RepositoryState.Done)
            } catch (exception: Exception) {
                _state.postValue(RepositoryState.Error(exception))
            }
        }
    }

    suspend fun createItem(
        itemId: String,
        itemName: String,
        itemPrice: Double,
        itemAmount: Int?,
        jwt: JWT
    ) {
        withContext(Dispatchers.IO) {
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
            val itemDTO = ApiItemDTO(
                id = itemId,
                name = itemName,
                price = itemPrice,
                amount = itemAmount
            )
            NetworkService.koffeeApi.updateItem(itemDTO, jwt.formatToken())
        }
    }

    suspend fun deleteItem(itemId: String, jwt: JWT) {
        withContext(Dispatchers.IO) {
            NetworkService.koffeeApi.deleteItem(itemId, jwt.formatToken())
            database.itemDao.deleteById(itemId)
        }
    }
}
