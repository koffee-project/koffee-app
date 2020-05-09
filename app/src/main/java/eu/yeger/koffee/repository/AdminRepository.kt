package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.network.ApiCredentials
import eu.yeger.koffee.network.NetworkService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class AdminRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    suspend fun loginRequired(): Boolean {
        return withContext(Dispatchers.IO) {
            database.jwtDao.get() === null
        }
    }

    fun getJWTTokenAsLiveData(): LiveData<JWT?> = database.jwtDao.getAsLiveData()

    suspend fun login(userId: String, password: String) {
        withContext(Dispatchers.IO) {
            val credentials = ApiCredentials(id = userId, password = password)
            val response = NetworkService.koffeeApi.login(credentials)
            val jwt = JWT(userId = userId, token = response.data)
            Timber.d("Received JWT: $jwt")
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
