package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.network.ApiCredentials
import eu.yeger.koffee.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AdminRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    suspend fun getJWT(): JWT? {
        return withContext(Dispatchers.IO) {
            database.jwtDao.get()
        }
    }

    fun isAuthenticatedAsLiveData(): LiveData<Boolean> = database.jwtDao.getAsLiveData().map { it !== null }

    suspend fun loginRequired(): Boolean {
        return withContext(Dispatchers.IO) {
            database.jwtDao.get() === null
        }
    }

    suspend fun login(userId: String, password: String) {
        withContext(Dispatchers.IO) {
            val credentials = ApiCredentials(id = userId, password = password)
            val token = NetworkService.koffeeApi.login(credentials)
            val jwt = JWT(userId = userId, token = token)
            database.jwtDao.run {
                deleteAll()
                insert(jwt)
            }
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            database.jwtDao.deleteAll()
        }
    }
}
