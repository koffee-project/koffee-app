package eu.yeger.koffee.repository

import android.content.Context
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.network.ApiCredentials
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class AdminRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    suspend fun getJWT(): JWT? {
        return withContext(Dispatchers.IO) {
            database.jwtDao.get()
        }
    }

    fun getJWTFlow(): Flow<JWT?> {
        return database.jwtDao.getAsFlow().distinctUntilChanged()
    }

    fun isAuthenticatedFlow(): Flow<Boolean> {
        return database.jwtDao.getAsFlow()
            .distinctUntilChanged()
            .map { it !== null }
    }

    suspend fun loginRequired(): Boolean {
        return withContext(Dispatchers.IO) {
            database.jwtDao.get() === null
        }
    }

    suspend fun login(userId: String, password: String) {
        withContext(Dispatchers.IO) {
            val credentials = ApiCredentials(id = userId, password = password)
            val apiToken = NetworkService.koffeeApi.login(credentials)
            val jwt = apiToken.asDomainModel(userId)
            database.jwtDao.upsert(jwt)
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            database.jwtDao.deleteAll()
        }
    }
}
