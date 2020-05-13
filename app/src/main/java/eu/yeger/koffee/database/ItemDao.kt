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

    @Query("SELECT * FROM item ORDER BY name ASC")
    fun getAllAsLiveData(): LiveData<List<Item>>

    @Query("SELECT * FROM item WHERE id == :id")
    fun getByIdAsLiveData(id: String?): LiveData<Item?>

    @Query("SELECT * FROM item WHERE id == :id")
    fun getById(id: String?): Item?

    @Query("DELETE FROM item WHERE id == :id")
    fun deleteById(id: String)
}
