package eu.yeger.koffee.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import eu.yeger.koffee.domain.Item

@Dao
interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg items: Item)

    @Query("SELECT * FROM item")
    fun getAllAsLiveData(): LiveData<List<Item>>

    @Query("SELECT * FROM item WHERE id == :id")
    fun getByIdAsLiveData(id: String?): LiveData<Item?>
}
