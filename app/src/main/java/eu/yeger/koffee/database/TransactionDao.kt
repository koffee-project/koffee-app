package eu.yeger.koffee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg transactions: DatabaseTransaction)

    @Query("SELECT * FROM databasetransaction WHERE userId == :userId ORDER BY timestamp DESC")
    fun getAllByUserIdAsLiveData(userId: String?): LiveData<List<DatabaseTransaction>>

    @Query("SELECT * FROM databasetransaction WHERE userId == :userId AND itemId == :itemId ORDER BY timestamp DESC")
    fun getAllByUserIdAndItemIdAsLiveData(userId: String?, itemId: String): LiveData<List<DatabaseTransaction>>

    @Query("DELETE FROM databasetransaction")
    fun deleteAll()
}
