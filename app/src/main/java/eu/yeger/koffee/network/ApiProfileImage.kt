package eu.yeger.koffee.network

import android.util.Base64
import eu.yeger.koffee.domain.ProfileImage

/**
 * API class for [ProfileImage]s.
 *
 * @property encodedImage The encoded bytes of the image.
 * @property timestamp The timestamp of the image's upload date.
 *
 * @author Jan Müller
 */
class ApiProfileImage(
    val encodedImage: String,
    val timestamp: Long
)

/**
 * Extension method for transforming [ApiProfileImage]s into [ProfileImage]s.
 *
 * @receiver The source [ProfileImage].
 * @param userId The id of the user this profile image belongs to.
 * @return The [ProfileImage].
 *
 * @author Jan Müller
 */
fun ApiProfileImage.asDomainModel(userId: String) = ProfileImage(
    userId = userId,
    bytes = Base64.decode(encodedImage, Base64.DEFAULT),
    timestamp = timestamp
)
