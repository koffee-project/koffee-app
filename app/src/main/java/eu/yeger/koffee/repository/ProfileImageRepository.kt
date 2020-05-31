package eu.yeger.koffee.repository

import android.content.Context
import eu.yeger.koffee.database.KoffeeDatabase
import eu.yeger.koffee.database.getDatabase
import eu.yeger.koffee.domain.ProfileImage
import eu.yeger.koffee.network.NetworkService
import eu.yeger.koffee.network.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext

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
}
