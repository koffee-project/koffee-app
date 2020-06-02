package eu.yeger.koffee.ui.user.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.paging.toLiveData
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction
import java.io.File

private const val PAGE_SIZE = 50

abstract class BaseUserDetailsViewModel(
    private val isActiveUser: Boolean,
    private val userId: String?,
    private val profileImageRepository: ProfileImageRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    val user = userRepository.getUserByIdFlow(userId).asLiveData()
    val hasUser = user.map { it !== null }

    val profileImage = profileImageRepository.getProfileImageByUserIdAsFlow(userId).asLiveData()
    val canEditProfileImage = user.map { isActiveUser && it !== null }
    val canDeleteProfileImage = profileImage.map { isActiveUser && it !== null }

    val transactions = transactionRepository.getTransactionsByUserId(userId).toLiveData(PAGE_SIZE)
    val hasTransactions = transactions.map { it.isNotEmpty() }

    abstract val canModify: LiveData<Boolean>
    abstract val canDelete: LiveData<Boolean>

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val editProfileImageAction = SimpleAction()
    val deleteProfileImageAction = SimpleAction()
    val userNotFoundAction = SimpleAction()

    fun refreshUser() {
        if (userId == null) {
            userNotFoundAction.activate()
            return
        }

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

    fun uploadProfileImage(image: File) {
        userId?.let {
            onViewModelScope {
                profileImageRepository.uploadProfileImage(userId, image)
            }
        }
    }

    fun deleteProfileImage() {
        userId?.let {
            onViewModelScope {
                profileImageRepository.deleteProfileImageByUserId(userId)
            }
        }
    }

    fun activateEditProfileImageAction() = editProfileImageAction.activate()

    fun activateDeleteProfileImageAction() = deleteProfileImageAction.activate()

    abstract fun activateEditUserAction()

    abstract fun activateCreditUserAction()

    abstract fun activateDeleteUserAction()
}
