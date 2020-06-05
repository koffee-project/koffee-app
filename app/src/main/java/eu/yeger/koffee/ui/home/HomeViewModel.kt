package eu.yeger.koffee.ui.home

import androidx.lifecycle.asLiveData
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import java.io.File

class HomeViewModel(
    private val userId: String,
    private val userRepository: UserRepository,
    private val profileImageRepository: ProfileImageRepository
) : CoroutineViewModel() {

    val user = userRepository.getUserByIdFlow(userId).asLiveData()
    val profileImage = profileImageRepository.getProfileImageByUserIdAsFlow(userId).asLiveData()

    val editProfileImageAction = DataAction<Boolean>()

    fun activateEditProfileImageAction() {
        if (user.value == null) return
        editProfileImageAction.activateWith(profileImage.value != null)
    }

    fun uploadProfileImage(image: File) {
        onViewModelScope {
            profileImageRepository.uploadProfileImage(userId, image)
        }
    }

    fun deleteProfileImage() {
        onViewModelScope {
            profileImageRepository.deleteProfileImageByUserId(userId)
        }
    }
}