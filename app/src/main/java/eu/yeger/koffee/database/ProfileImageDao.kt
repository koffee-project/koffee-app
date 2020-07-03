package eu.yeger.koffee.database

import androidx.room.Dao
import androidx.room.Query
import eu.yeger.koffee.domain.ProfileImage
import eu.yeger.koffee.domain.User
import kotlinx.coroutines.flow.Flow

/**
 * [Dao](https://developer.android.com/reference/androidx/room/Dao) that manages [ProfileImage]s in the database.
 *
 * @author Jan Müller
 */
@Dao
interface ProfileImageDao : BaseDao<ProfileImage> {

    /**
     * Returns the [ProfileImage] with the given id.
     *
     * @return The [ProfileImage].
     */
    @Query("SELECT * FROM profileimage WHERE userId == :userId")
    suspend fun getById(userId: String?): ProfileImage?

    /**
     * Returns a Flow of the [ProfileImage] with the given id.
     *
     * @return The Flow.
     */
    @Query("SELECT * FROM profileimage WHERE userId == :userId")
    fun getByIdAsFlow(userId: String?): Flow<ProfileImage?>

    /**
     * Deletes the [ProfileImage] with the given [User] id.
     *
     * @param userId The [User]'s id of the [ProfileImage] to be deleted.
     */
    @Query("DELETE FROM profileimage WHERE userId == :userId")
    suspend fun deleteByUserId(userId: String?)

    /**
     * Deletes all [ProfileImage]s except the one with the given [User] id.
     *
     * @param userId The [User]'s id of the [ProfileImage] to be kept.
     */
    @Query("DELETE FROM profileimage WHERE userId != :userId")
    suspend fun deleteAllExceptWithUserId(userId: String?)
}
