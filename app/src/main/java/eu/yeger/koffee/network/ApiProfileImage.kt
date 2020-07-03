package eu.yeger.koffee.network

import android.util.Base64
import eu.yeger.koffee.domain.ProfileImage
import eu.yeger.koffee.domain.User

/**
 * API class for [ProfileImage]s.
 *
 * @property id The id of the image. Corresponds to a [User] id.
 * @property encodedImage The encoded bytes of the image.
 * @property timestamp The timestamp of the image's upload date.
 *
 * @author Jan Müller
 */
class ApiProfileImage(
    val id: String,
    val encodedImage: String,
    val timestamp: Long
)

/**
 * Extension method for transforming [ApiProfileImage]s into [ProfileImage]s.
 *
 * @receiver The source [ProfileImage].
 * @return The [ProfileImage].
 *
 * @author Jan Müller
 */
fun ApiProfileImage.asDomainModel() = ProfileImage(
    userId = id,
    bytes = Base64.decode(encodedImage, Base64.DEFAULT),
    timestamp = timestamp
)
