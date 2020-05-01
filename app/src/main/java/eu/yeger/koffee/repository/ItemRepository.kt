package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ItemRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    private val _state = MutableLiveData<RepositoryState>(RepositoryState.Idle)
    val state: LiveData<RepositoryState> = _state

    val items = database.itemDao.getAllAsLiveData()

    fun getItemById(id: String?): LiveData<Item?> {
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
}
