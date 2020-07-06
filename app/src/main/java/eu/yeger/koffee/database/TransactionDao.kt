package eu.yeger.koffee.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.PurchaseStatistic
import eu.yeger.koffee.domain.User
import kotlinx.coroutines.flow.Flow

/**
 * [Dao](https://developer.android.com/reference/androidx/room/Dao) that manages [DatabaseTransaction]s.
 *
 * @author Jan Müller
 */
@Dao
interface TransactionDao : BaseDao<DatabaseTransaction> {

    /**
     * Returns all [DatabaseTransaction]s with the given [User] id as a paging factory.
     *
     * @return The paging factory.
     */
    @Query("SELECT * FROM databasetransaction WHERE userId == :userId ORDER BY timestamp DESC")
    fun getAllByUserIdPaged(userId: String?): DataSource.Factory<Int, DatabaseTransaction>

    /**
     * Returns all [DatabaseTransaction]s with the given [User] id and [Item] id as a paging factory.
     *
     * @return The paging factory.
     */
    @Query("SELECT * FROM databasetransaction WHERE userId == :userId AND itemId == :itemId ORDER BY timestamp DESC")
    fun getAllByUserIdAndItemIdPaged(userId: String?, itemId: String): DataSource.Factory<Int, DatabaseTransaction>

    @Query("SELECT itemId, itemName, SUM(amount) as amount, MAX(timestamp) as lastPurchaseTimestamp FROM databasetransaction WHERE userId == :userId AND type == 'Purchase' GROUP BY itemId ORDER BY itemName ASC")
    suspend fun getPurchaseStats(userId: String?): List<PurchaseStatistic>

    @Query("SELECT * FROM (SELECT * FROM databasetransaction WHERE userId == :userId AND type != 'Funding' ORDER BY timestamp DESC LIMIT 1) WHERE type == 'Purchase'")
    fun getRefundableByUserIdAsFlow(userId: String?): Flow<DatabaseTransaction?>

    /**
     * Deletes all [DatabaseTransaction]s.
     */
    @Query("DELETE FROM databasetransaction")
    suspend fun deleteAll()

    /**
     * Deletes all [DatabaseTransaction]s except ones with the given [User] id.
     *
     * @param userId The [User]'s id of the [DatabaseTransaction]s to be kept.
     */
    @Query("DELETE FROM databasetransaction WHERE userId != :userId")
    suspend fun deleteAllExceptWithUserId(userId: String?)

    /**
     * Deletes the [DatabaseTransaction] with the given [User] id.
     *
     * @param userId The [User]'s id of the [DatabaseTransaction]s to be deleted.
     */
    @Query("DELETE FROM databasetransaction WHERE userId == :userId")
    suspend fun deleteByUserId(userId: String)
}
