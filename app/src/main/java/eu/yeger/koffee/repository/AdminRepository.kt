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

/**
 * Repository for admin-related information.
 *
 * @property database The [KoffeeDatabase] used by this repository.
 *
 * @author Jan Müller
 */
class AdminRepository(private val database: KoffeeDatabase) {

    /**
     * Utility constructor for improved readability.
     */
    constructor(context: Context) : this(getDatabase(context))

    /**
     * Returns the [JWT] stored in [KoffeeDatabase].
     *
     * @return The [JWT].
     */
    suspend fun getJWT(): JWT? {
        return withContext(Dispatchers.IO) {
            database.jwtDao.get()
        }
    }

    /**
     * Returns a distinct Flow of the [JWT] stored in [KoffeeDatabase].
     *
     * @return The Flow.
     */
    fun getJWTFlow(): Flow<JWT?> {
        return database.jwtDao.getAsFlow().distinctUntilChanged()
    }

    /**
     * Returns a distinct Flow that indicates if a [JWT] is stored in [KoffeeDatabase].
     *
     * @return The Flow.
     */
    fun isAuthenticatedFlow(): Flow<Boolean> {
        return database.jwtDao.getAsFlow()
            .distinctUntilChanged()
            .map { it !== null }
    }

    /**
     * Checks if the current login has expired.
     *
     * @return true if the login has expired.
     */
    suspend fun loginHasExpired(): Boolean {
        val token = getJWT() ?: return false
        return token.expiration <= System.currentTimeMillis()
    }

    /**
     * Performs a login using the provided credentials and stores the resulting [JWT] in [KoffeeDatabase].
     *
     * @param userId The user id used for the login.
     * @param password The password used for the login.
     */
    suspend fun login(userId: String, password: String) {
        withContext(Dispatchers.IO) {
            val credentials = ApiCredentials(id = userId, password = password)
            val apiToken = NetworkService.koffeeApi.login(credentials)
            val jwt = apiToken.asDomainModel(userId)
            database.jwtDao.upsert(jwt)
        }
    }

    /**
     * Performs a logout by deleting the [JWT] from [KoffeeDatabase].
     */
    suspend fun logout() {
        withContext(Dispatchers.IO) {
            database.jwtDao.deleteAll()
        }
    }
}
