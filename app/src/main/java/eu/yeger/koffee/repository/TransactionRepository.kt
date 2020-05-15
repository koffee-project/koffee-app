package eu.yeger.koffee.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.asDomainModel
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.network.ApiPurchaseRequest
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDatabaseModel
import eu.yeger.koffee.utility.onNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    fun getTransactionsByUserId(userId: String?): LiveData<List<Transaction>> {
        return database.transactionDao.getAllByUserIdAsLiveData(userId).map { it.asDomainModel() }
    }

    fun getTransactionsByUserIdAndItemId(
        userId: String?,
        itemId: String
    ): LiveData<List<Transaction>> {
        return database.transactionDao.getAllByUserIdAndItemIdAsLiveData(
            userId = userId,
            itemId = itemId
        ).map { it.asDomainModel() }
    }

    fun getLastRefundableTransactionByUserId(userId: String?): LiveData<Transaction.Purchase?> {
        return database.transactionDao.getAllByUserIdAsLiveData(userId).map { transactions ->
            val lastTransaction = transactions
                .firstOrNull { it.type !== Transaction.Type.funding }
                ?.asDomainModel()
            when {
                lastTransaction == null -> null // no transaction found
                lastTransaction is Transaction.Refund -> null // purchase already refunded
                System.currentTimeMillis() - lastTransaction.timestamp >= 60_000 -> null // purchase too old
                lastTransaction is Transaction.Purchase -> lastTransaction // refund possible
                else -> null // impossible
            }
        }
    }

    suspend fun fetchTransactionsByUserId(userId: String) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.transactionDao.deleteByUserId(userId) }) {
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
            val purchaseRequest = ApiPurchaseRequest(itemId, amount)
            NetworkService.koffeeApi.purchaseItem(userId, purchaseRequest)
        }
    }

    suspend fun refundPurchase(userId: String) {
        withContext(Dispatchers.IO) {
            NetworkService.koffeeApi.refundPurchase(userId)
        }
    }
}
