package eu.yeger.koffee.repository

import android.content.Context
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.ProfileImage
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDomainModel
import eu.yeger.koffee.utility.onNotFound
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

/**
 * Repository for [ProfileImage]s.
 *
 * @property database The [KoffeeDatabase] used by this repository.
 *
 * @author Jan Müller
 */
class ProfileImageRepository(private val database: KoffeeDatabase) {

    /**
     * Utility constructor for improved readability.
     */
    constructor(context: Context) : this(getDatabase(context))

    /**
     * Returns the [ProfileImage] with the given [User.id] from [KoffeeDatabase].
     *
     * @param userId The id of the [User].
     * @return The [ProfileImage].
     */
    suspend fun getProfileImageByUserId(userId: String?): ProfileImage? {
        return withContext(Dispatchers.IO) {
            database.profileImageDao.getById(userId)
        }
    }

    /**
     * Returns a distinct Flow of the [ProfileImage] with the given [User.id] from [KoffeeDatabase].
     *
     * @param userId The [User.id].
     * @return The [ProfileImage].
     */
    fun getProfileImageByUserIdAsFlow(userId: String?): Flow<ProfileImage?> {
        return database.profileImageDao.getByIdAsFlow(userId)
            .distinctUntilChanged()
    }

    /**
     * Fetches the [ProfileImage] with the given [User.id] from [NetworkService] and inserts it into [KoffeeDatabase].
     * Purges this [ProfileImage] from [KoffeeDatabase] if it no longer exists.
     *
     * @param userId The [User.id].
     */
    suspend fun fetchProfileImageByUserId(userId: String) {
        withContext(Dispatchers.IO) {
            onNotFound({ deleteProfileImageByUserId(userId) }, rethrow = false) {
                database.profileImageDao.run {
                    getById(userId)?.let { currentImage ->
                        val newTimestamp = NetworkService.koffeeApi.getProfileImageTimestamp(userId)
                        if (currentImage.timestamp >= newTimestamp) return@withContext // no new image
                    }

                    val newImage = NetworkService.koffeeApi.getProfileImage(userId)

                    insert(newImage.asDomainModel())
                }
            }
        }
    }

    /**
     * Uploads an image for the given [User.id] to [NetworkService] and inserts it into [KoffeeDatabase].
     *
     * @param userId The [User.id].
     * @param image The image [File] to be uploaded.
     */
    suspend fun uploadProfileImage(userId: String, image: File) {
        withContext(Dispatchers.IO) {
            val body = RequestBody.create(MediaType.parse("image/*"), image)
            val part = MultipartBody.Part.createFormData("image", image.name, body)
            NetworkService.koffeeApi.uploadProfileImage(userId, part)
            fetchProfileImageByUserId(userId)
        }
    }

    /**
     * Requests the deletion of the [ProfileImage] with the given [User.id].
     *
     * @param userId The [User.id].
     */
    suspend fun deleteProfileImageByUserId(userId: String) {
        withContext(Dispatchers.IO) {
            try {
                NetworkService.koffeeApi.deleteProfilePicture(userId)
            } catch (exception: HttpException) {
                if (exception.code() != 404) throw exception
            }
            database.profileImageDao.deleteByUserId(userId)
        }
    }

    /**
     * Deletes all local [ProfileImage]s except the one with the given [User.id].
     *
     * @param userId The [User.id] of the [ProfileImage] that will be kept.
     */
    suspend fun deleteAllImagesExceptWithUserId(userId: String?) {
        withContext(Dispatchers.IO) {
            database.profileImageDao.deleteAllExceptWithUserId(userId)
        }
    }
}
