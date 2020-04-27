package eu.yeger.koffee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.yeger.koffee.domain.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg transactions: Transaction)

    @Query("SELECT * FROM `transaction` WHERE userId == :userId")
    fun getAllByUserIdAsLiveData(userId: String?): LiveData<List<Transaction>>
}
