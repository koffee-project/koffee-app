package eu.yeger.koffee.repository

import android.content.Context
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.ProfileImage
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDomainModel
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileImageRepository(private val database: KoffeeDatabase) {

    constructor(context: Context) : this(getDatabase(context))

    fun getProfileImageByUserId(userId: String?): Flow<ProfileImage?> {
        return database.profileImageDao.getByIdAsFlow(userId)
            .distinctUntilChanged()
    }

    suspend fun fetchProfileImageByUserId(userId: String) {
        withContext(Dispatchers.IO) {
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

    suspend fun deleteAllImagesExceptWithUserId(userId: String?) {
        withContext(Dispatchers.IO) {
            database.profileImageDao.deleteAllExceptWithUserId(userId)
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
}
