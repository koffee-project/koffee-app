package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.DataSource
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.asDomainModel
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.network.ApiPurchaseRequest
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDatabaseModel
import eu.yeger.koffee.utility.onNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TransactionRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    fun getTransactionsByUserIdPaged(userId: String?): DataSource.Factory<Int, Transaction> {
        return database.transactionDao.getAllByUserIdPaged(userId)
            .map { it.asDomainModel() }
    }

    fun getTransactionsByUserIdAndItemIdPaged(
        userId: String?,
        itemId: String
    ): DataSource.Factory<Int, Transaction> {
        return database.transactionDao.getAllByUserIdAndItemIdPaged(
            userId = userId,
            itemId = itemId
        ).map { it.asDomainModel() }
    }

    fun getLastRefundableTransactionByUserIdAsLiveData(userId: String?): LiveData<Transaction.Purchase?> {
        return database.transactionDao.getRefundableByUserIdAsFlow(userId)
            .distinctUntilChanged()
            .map {
                when (val transaction = it?.asDomainModel()) {
                    is Transaction.Purchase -> transaction
                    else -> null
                }
            }
            .asLiveData()
    }

    suspend fun fetchTransactionsByUserId(userId: String) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeUserById(userId) }) {
                val response = NetworkService.koffeeApi.getTransactionForUser(userId)
                val transactions = response.asDatabaseModel(userId)
                database.transactionDao.apply {
                    deleteAll()
                    insertAll(*transactions.toTypedArray())
                }
            }
        }
    }

    suspend fun buyItem(userId: String, itemId: String, amount: Int) {
        withContext(Dispatchers.IO) {
            // Disabled because uncertainty of userId or itemId do not exist
            // onNotFound({ database.purgeUserById(userId) }) {
            val purchaseRequest = ApiPurchaseRequest(itemId, amount)
            NetworkService.koffeeApi.purchaseItem(userId, purchaseRequest)
            // }
        }
    }

    suspend fun refundPurchase(userId: String) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeUserById(userId) }) {
                NetworkService.koffeeApi.refundPurchase(userId)
            }
        }
    }
}
