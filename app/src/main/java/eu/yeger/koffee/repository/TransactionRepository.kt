package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.asDomainModel
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    fun getTransactionsByUserId(userId: String?): LiveData<List<Transaction>> {
        return database.transactionDao.getAllByUserIdAsLiveData(userId).map { it.asDomainModel() }
    }

    suspend fun fetchTransactionsByUserId(userId: String) {
        withContext(Dispatchers.IO) {
            val response = NetworkService.koffeeApi.getTransactionForUser(userId)
            val transactions = response.data!!.asDatabaseModel(userId)
            database.transactionDao.apply {
                deleteAll()
                insertAll(*transactions.toTypedArray())
            }
        }
    }
}
