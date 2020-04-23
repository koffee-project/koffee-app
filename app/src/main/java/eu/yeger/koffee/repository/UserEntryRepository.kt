package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.UserEntry
import eu.yeger.koffee.network.ApiUserEntry
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserEntryRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    val users = database.userEntryDao.getAllAsLiveData()

    class Filter(query: String) {
        val nameFragment = "%$query%"
    }

    fun filteredUsers(filter: Filter): LiveData<List<UserEntry>> {
        return database.userEntryDao.getFilteredAsLiveData(filter.nameFragment)
    }

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            val apiResponse = NetworkService.koffeeApi.getUsers()
            val userEntries = apiResponse.data.map(ApiUserEntry::asDomainModel)
            database.userEntryDao.apply {
                deleteAll()
                insertAll(*userEntries.toTypedArray())
            }
        }
    }
}
