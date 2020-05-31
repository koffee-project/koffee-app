package eu.yeger.koffee.repository

import android.content.Context
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.ProfileImage
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

class ProfileImageRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    suspend fun getProfileImageByUserId(userId: String?): ProfileImage? {
        return withContext(Dispatchers.IO) {
            database.profileImageDao.getById(userId)
        }
    }

    fun getProfileImageByUserIdAsFlow(userId: String?): Flow<ProfileImage?> {
        return database.profileImageDao.getByIdAsFlow(userId)
            .distinctUntilChanged()
    }

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

    suspend fun uploadProfileImage(userId: String, image: File) {
        withContext(Dispatchers.IO) {
            val imagePart = MultipartBody.Part.create(
                RequestBody.create(
                    MediaType.parse("image/*"),
                    image.readBytes()
                )
            )
            val body = RequestBody.create(MediaType.parse("image/*"), image)
            val part = MultipartBody.Part.createFormData("image", image.name, body)
            NetworkService.koffeeApi.uploadProfileImage(userId, part)
            fetchProfileImageByUserId(userId)
        }
    }

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

    suspend fun deleteAllImagesExceptWithUserId(userId: String?) {
        withContext(Dispatchers.IO) {
            database.profileImageDao.deleteAllExceptWithUserId(userId)
        }
    }
}
