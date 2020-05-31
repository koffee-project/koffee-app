package eu.yeger.koffee.network

import android.util.Base64
import eu.yeger.koffee.domain.ProfileImage

class ApiProfileImage(
    val id: String,
    val encodedImage: String,
    val timestamp: Long
)

fun ApiProfileImage.asDomainModel() = ProfileImage(
    userId = id,
    bytes = Base64.decode(encodedImage, Base64.DEFAULT),
    timestamp = timestamp
)
