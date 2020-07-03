package eu.yeger.koffee.database

import androidx.room.Dao
import androidx.room.Query
import eu.yeger.koffee.domain.ProfileImage
import kotlinx.coroutines.flow.Flow

/**
 * [Dao](https://developer.android.com/reference/androidx/room/Dao) that manages [ProfileImage]s in the database.
 *
 * @author Jan Müller
 */
@Dao
interface ProfileImageDao : BaseDao<ProfileImage> {

    @Query("SELECT * FROM profileimage WHERE userId == :userId")
    suspend fun getById(userId: String?): ProfileImage?

    @Query("SELECT * FROM profileimage WHERE userId == :userId")
    fun getByIdAsFlow(userId: String?): Flow<ProfileImage?>

    @Query("DELETE FROM profileimage WHERE userId == :userId")
    suspend fun deleteByUserId(userId: String?)

    @Query("DELETE FROM profileimage WHERE userId != :userId")
    suspend fun deleteAllExceptWithUserId(userId: String?)
}
