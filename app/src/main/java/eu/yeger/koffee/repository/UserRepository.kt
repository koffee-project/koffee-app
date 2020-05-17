package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.JWT
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.network.*
import eu.yeger.koffee.utility.onNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

class UserRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    fun getUsersAsLiveData(): LiveData<List<User>> {
        return database.userDao.getAllAsFlow()
            .distinctUntilChanged()
            .asLiveData()
    }

    fun getFilteredUsersAsLiveData(filter: Filter): LiveData<List<User>> {
        return database.userDao.getFilteredAsFlow(filter.nameFragment)
            .distinctUntilChanged()
            .asLiveData()
    }

    suspend fun fetchUsers() {
        withContext(Dispatchers.IO) {
            val response = NetworkService.koffeeApi.getUsers()
            val userEntries = response.map(ApiUserEntry::asDomainModel)
            database.userDao.updateUsers(userEntries)
        }
    }

    suspend fun fetchUserById(id: String) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeUserById(id) }) {
                val response = NetworkService.koffeeApi.getUserById(id)
                val user = response.asDomainModel()
                database.userDao.insert(user)
            }
        }
    }

    suspend fun hasUserWithId(id: String?): Boolean {
        return withContext(Dispatchers.IO) {
            database.userDao.getById(id) !== null
        }
    }

    suspend fun getUserById(id: String?): User? {
        return withContext(Dispatchers.IO) {
            database.userDao.getById(id)
        }
    }

    fun getUserByIdAsLiveData(id: String?): LiveData<User?> {
        return database.userDao.getByIdAsFlow(id)
            .distinctUntilChanged()
            .asLiveData()
    }

    suspend fun createUser(
        userId: String,
        userName: String,
        password: String?,
        isAdmin: Boolean,
        jwt: JWT
    ) {
        withContext(Dispatchers.IO) {
            val userDTO = ApiUserDTO(
                id = userId,
                name = userName,
                password = password,
                isAdmin = isAdmin
            )
            NetworkService.koffeeApi.createUser(userDTO, jwt.formatToken())
        }
    }

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
