package eu.yeger.koffee.ui.home

import androidx.lifecycle.asLiveData
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel

class HomeViewModel(
    private val userId: String,
    private val userRepository: UserRepository,
    profileImageRepository: ProfileImageRepository
) : CoroutineViewModel() {

    val user = userRepository.getUserByIdFlow(userId).asLiveData()

    val profileImage = profileImageRepository.getProfileImageByUserIdAsFlow(userId).asLiveData()
}