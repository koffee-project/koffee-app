package eu.yeger.koffee.repository

import android.content.Context
import androidx.paging.DataSource
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.network.*
import eu.yeger.koffee.utility.onNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

/**
 * Repository for [User]s.
 *
 * @property database The [KoffeeDatabase] used by this repository.
 *
 * @author Jan Müller
 */
class UserRepository(private val database: KoffeeDatabase) {

    /**
     * Utility constructor for improved readability.
     */
    constructor(context: Context) : this(getDatabase(context))

    /**
     * Returns all [User]s from [KoffeeDatabase] as pages.
     *
     * @return The paging factory.
     */
    fun getAllUser(): DataSource.Factory<Int, User> {
        return database.userDao.getAllPaged()
    }

    /**
     * Returns filtered [User]s from [KoffeeDatabase] as pages.
     *
     * @param filter The filter used for the query.
     *
     * @return The paging factory.
     */
    fun getFilteredUsers(filter: Filter): DataSource.Factory<Int, User> {
        return database.userDao.getFilteredPaged(filter.nameFragment)
    }

    /**
     * Checks if [KoffeeDatabase] has the [User] with the given id.
     *
     * @param id The id of the [User].
     * @return true if [KoffeeDatabase] contains the [User].
     */
    suspend fun hasUserWithId(id: String?): Boolean {
        return withContext(Dispatchers.IO) {
            database.userDao.getById(id) !== null
        }
    }

    /**
     * Returns the [User] with the given id from [KoffeeDatabase].
     *
     * @param id The id of the [User].
     * @return The [User].
     */
    suspend fun getUserById(id: String?): User? {
        return withContext(Dispatchers.IO) {
            database.userDao.getById(id)
        }
    }

    /**
     * Returns a distinct Flow of the [User] with the given id from [KoffeeDatabase].
     *
     * @param id The id of the [User].
     * @return The Flow.
     */
    fun getUserByIdFlow(id: String?): Flow<User?> {
        return database.userDao.getByIdAsFlow(id)
            .distinctUntilChanged()
    }

    /**
     * Fetches all [User]s from [NetworkService] and inserts them into [KoffeeDatabase].
     */
    suspend fun fetchUsers() {
        withContext(Dispatchers.IO) {
            val response = NetworkService.koffeeApi.getUsers()
            val userEntries = response.map(ApiUserEntry::asDomainModel)
            database.userDao.upsertAll(userEntries)
        }
    }

    /**
     * Fetches the [User] with the given id from [NetworkService] and inserts it into [KoffeeDatabase].
     * Purges this [User] from [KoffeeDatabase] if it no longer exists.
     *
     * @param id The id of the [User] to be fetched.
     */
    suspend fun fetchUserById(id: String) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeUserById(id) }) {
                val response = NetworkService.koffeeApi.getUserById(id)
                val user = response.asDomainModel()
                database.userDao.insert(user)
            }
        }
    }

    /**
     * Requests the creation of a [User] with the given data.
     *
     * @param userId The id of the [User] to be updated.
     * @param userName The name of the [User] to be updated.
     * @param password The password of the [User] to be updated.
     * @param isAdmin The admin privileges of the [User] to be updated.
     * @param jwt The authentication token.
     */
    suspend fun createUser(
        userId: String?,
        userName: String,
        password: String?,
        isAdmin: Boolean,
        jwt: JWT
    ): String {
        return withContext(Dispatchers.IO) {
            val userDTO = ApiUserDTO(
                id = userId,
                name = userName,
                password = password,
                isAdmin = isAdmin
            )
            NetworkService.koffeeApi.createUser(userDTO, jwt.formatToken())
        }
    }

    /**
     * Requests an update of the [User] with the given data.
     *
     * @param userId The id of the [User] to be updated.
     * @param userName The name of the [User] to be updated.
     * @param password The password of the [User] to be updated.
     * @param isAdmin The admin privileges of the [User] to be updated.
     * @param jwt The authentication token.
     */
    suspend fun updateUser(
        userId: String,
        userName: String,
        password: String?,
        isAdmin: Boolean,
        jwt: JWT
    ) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeUserById(userId) }) {
                val userDTO = ApiUserDTO(
                    id = userId,
                    name = userName,
                    password = password,
                    isAdmin = isAdmin
                )
                NetworkService.koffeeApi.updateUser(userDTO, jwt.formatToken())
            }
        }
    }

    /**
     * Requests the crediting of the [User] with the given id.
     * Purges this [User] from [KoffeeDatabase] if it no longer exists.
     *
     * @param userId The id of the [User] to be credited.
     * @param amount The amount to be credited.
     * @param jwt The authentication token.
     */
    suspend fun creditUser(userId: String, amount: Double, jwt: JWT) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeUserById(userId) }) {
                val fundingRequest = ApiFundingRequest(amount)
                NetworkService.koffeeApi.creditUser(userId, fundingRequest, jwt.formatToken())
            }
        }
    }

    /**
     * Requests the deletion of the [User] with the given id.
     * Purges this [User] from [KoffeeDatabase] if it no longer exists.
     *
     * @param userId The id of the [User] to be deleted.
     * @param jwt The authentication token.
     */
    suspend fun deleteUser(userId: String, jwt: JWT) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeUserById(userId) }) {
                NetworkService.koffeeApi.deleteUser(userId, jwt.formatToken())
                database.userDao.deleteById(userId)
                database.transactionDao.deleteByUserId(userId)
            }
        }
    }
}
