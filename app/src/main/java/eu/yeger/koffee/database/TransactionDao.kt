package eu.yeger.koffee.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import eu.yeger.koffee.domain.PurchaseStatistic
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao : BaseDao<DatabaseTransaction> {

    @Query("SELECT * FROM databasetransaction WHERE userId == :userId ORDER BY timestamp DESC")
    fun getAllByUserIdPaged(userId: String?): DataSource.Factory<Int, DatabaseTransaction>

    @Query("SELECT * FROM databasetransaction WHERE userId == :userId AND itemId == :itemId ORDER BY timestamp DESC")
    fun getAllByUserIdAndItemIdPaged(userId: String?, itemId: String): DataSource.Factory<Int, DatabaseTransaction>

    @Query("SELECT itemId, itemName, SUM(amount) as amount FROM databasetransaction WHERE userId == :userId AND type != 'Funding' GROUP BY itemId ORDER BY itemName ASC")
    suspend fun getPurchaseStats(userId: String?): List<PurchaseStatistic>

    @Query("SELECT * FROM (SELECT * FROM databasetransaction WHERE userId == :userId AND type != 'Funding' ORDER BY timestamp DESC LIMIT 1) WHERE type == 'Purchase'")
    fun getRefundableByUserIdAsFlow(userId: String?): Flow<DatabaseTransaction?>

    @Query("DELETE FROM databasetransaction")
    suspend fun deleteAll()

    @Query("DELETE FROM databasetransaction WHERE userId != :userId")
    suspend fun deleteAllExceptWithUserId(userId: String?)

    @Query("DELETE FROM databasetransaction WHERE userId == :userId")
    suspend fun deleteByUserId(userId: String)
}
