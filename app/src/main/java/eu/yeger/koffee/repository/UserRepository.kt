package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    fun getUserById(id: String?): LiveData<User?> {
        return database.userDao.getByIdAsLiveData(id)
    }

    suspend fun fetchUserById(id: String) {
        withContext(Dispatchers.IO) {
            val response = NetworkService.koffeeApi.getUserById(id)
            val user = response.data!!.asDomainModel()
            database.userDao.insert(user)
        }
    }
}
