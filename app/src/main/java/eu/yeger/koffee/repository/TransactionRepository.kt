package eu.yeger.koffee.repository

import android.content.Context
import androidx.paging.DataSource
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.asDomainModel
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.PurchaseStatistic
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.network.ApiPurchaseRequest
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDatabaseModel
import eu.yeger.koffee.utility.onNotFound
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * Repository for [Transaction]s.
 *
 * @property database The [KoffeeDatabase] used by this repository.
 *
 * @author Jan Müller
 */
class TransactionRepository(private val database: KoffeeDatabase) {

    /**
     * Utility constructor for improved readability.
     */
    constructor(context: Context) : this(getDatabase(context))

    /**
     * Returns all [Transaction] with the given [User.id] from [KoffeeDatabase] as pages.
     *
     * @param userId The [User.id].
     * @return The paging factory.
     */
    fun getTransactionsByUserId(userId: String?): DataSource.Factory<Int, Transaction> {
        return database.transactionDao.getAllByUserIdPaged(userId)
            .map { it.asDomainModel() }
    }

    /**
     * Returns all [Transaction] with the given [User.id] and [Item.id] from [KoffeeDatabase] as pages.
     *
     * @param userId The [User.id].
     * @param itemId The [Item.id].
     * @return The paging factory.
     */
    fun getTransactionsByUserIdAndItemId(
        userId: String?,
        itemId: String
    ): DataSource.Factory<Int, Transaction> {
        return database.transactionDao.getAllByUserIdAndItemIdPaged(
            userId = userId,
            itemId = itemId
        ).map { it.asDomainModel() }
    }

    /**
     * Returns a Flow of the last refundable [Transaction.Purchase] with the given [User.id] from [KoffeeDatabase].
     *
     * @param userId The [User.id].
     * @return The Flow.
     */
    fun getLastRefundableTransactionByUserIdFlow(userId: String?): Flow<Transaction.Purchase?> {
        return database.transactionDao.getRefundableByUserIdAsFlow(userId)
            .distinctUntilChanged()
            .map {
                when (val transaction = it?.asDomainModel()) {
                    is Transaction.Purchase -> transaction
                    else -> null
                }
            }
    }

    /**
     * Returns a [List] of all [PurchaseStatistic]s for the given [User.id].
     *
     * @param userId The [User.id].
     * @return The Flow.
     */
    suspend fun getPurchaseStatsByUserId(userId: String?): List<PurchaseStatistic> {
        return withContext(Dispatchers.IO) {
            database.transactionDao.getPurchaseStats(userId)
        }
    }

    /**
     * Fetches all [Transaction]s for the given [User.id] from [NetworkService] and inserts them into [KoffeeDatabase].
     * Purges the [User] from [KoffeeDatabase] if it no longer exists.
     *
     * @param userId The [User.id].
     */
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

    /**
     * Requests a purchase for the given data.
     *
     * @param userId The id of the [User] making the purchase.
     * @param itemId The id of the [Item] being purchased.
     * @param amount The amount purchased.
     */
    suspend fun buyItem(userId: String, itemId: String, amount: Int) {
        withContext(Dispatchers.IO) {
            // Disabled because uncertainty of userId or itemId not existing
            // onNotFound({ database.purgeUserById(userId) }) {
            val purchaseRequest = ApiPurchaseRequest(itemId, amount)
            NetworkService.koffeeApi.purchaseItem(userId, purchaseRequest)
            // }
        }
    }

    /**
     * Requests a refund for the given data.
     *
     * @param userId The id of the [User] making the refund.
     */
    suspend fun refundPurchase(userId: String) {
        withContext(Dispatchers.IO) {
            onNotFound({ database.purgeUserById(userId) }) {
                NetworkService.koffeeApi.refundPurchase(userId)
            }
        }
    }

    /**
     * Deletes all local [Transaction]s except ones with the given [User.id].
     *
     * @param userId The [User.id] of the [Transaction]s that will be kept.
     */
    suspend fun deleteAllTransactionsExceptWithUserId(userId: String?) {
        withContext(Dispatchers.IO) {
            database.transactionDao.deleteAllExceptWithUserId(userId)
        }
    }
}
