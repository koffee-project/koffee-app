package eu.yeger.koffee.database

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao : BaseDao<DatabaseTransaction> {

    @Query("SELECT * FROM databasetransaction WHERE userId == :userId ORDER BY timestamp DESC")
    fun getAllByUserIdAsFlow(userId: String?): Flow<List<DatabaseTransaction>>

    @Query("SELECT * FROM databasetransaction WHERE userId == :userId AND itemId == :itemId ORDER BY timestamp DESC")
    fun getAllByUserIdAndItemIdAsFlow(userId: String?, itemId: String): Flow<List<DatabaseTransaction>>

    @Query("SELECT * FROM (SELECT * FROM databasetransaction WHERE userId == :userId AND type != 'funding' ORDER BY timestamp DESC LIMIT 1) WHERE type == 'purchase'")
    fun getRefundableByUserIdAsFlow(userId: String?): Flow<DatabaseTransaction?>

    @Query("DELETE FROM databasetransaction")
    suspend fun deleteAll()

    @Query("DELETE FROM databasetransaction WHERE userId == :userId")
    suspend fun deleteByUserId(userId: String)
}
