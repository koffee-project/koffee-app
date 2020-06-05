package eu.yeger.koffee.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction
import java.io.File

class HomeViewModel(
    private val userId: String,
    private val profileImageRepository: ProfileImageRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    val userNotFoundAction = SimpleAction()

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

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

    fun refreshUser() {
        onViewModelScope {
            _refreshing.value = true
            userRepository.fetchUserById(userId)
        }.invokeOnCompletion {
            _refreshing.postValue(false)
            onViewModelScope {
                if (!userRepository.hasUserWithId(userId)) {
                    userNotFoundAction.activate()
                }
            }
        }
        onViewModelScope {
            transactionRepository.fetchTransactionsByUserId(userId)
        }
        onViewModelScope {
            profileImageRepository.fetchProfileImageByUserId(userId)
        }
    }
}
