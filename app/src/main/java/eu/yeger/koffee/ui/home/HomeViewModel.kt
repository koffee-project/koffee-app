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

/**
 * [CoroutineViewModel] for accessing user data and modifying profile images.
 *
 * @property userId The id of the user.
 * @property profileImageRepository [ProfileImageRepository] for modifying profile images.
 * @property transactionRepository [TransactionRepository] for refreshing transactions.
 * @property userRepository [UserRepository] for accessing and refreshing user data.
 * @property userNotFoundAction [SimpleAction] that is activated when the user has been deleted.
 * @property refreshing Indicates that a refresh is in progress.
 * @property user A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the user.
 * @property profileImage A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the user's profile image.
 * @property editProfileImageAction [DataAction] that is activated when editing of the profile image is requested. Contains true if the profile image can be deleted.
 *
 * @author Jan Müller
 */
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

    /**
     * Requests editing of the profile image.
     */
    fun activateEditProfileImageAction() {
        if (user.value == null) return
        editProfileImageAction.activateWith(profileImage.value != null)
    }

    /**
     * Uploads a new profile image.
     *
     * @param image The file containing the new image.
     */
    fun uploadProfileImage(image: File) {
        onViewModelScope {
            profileImageRepository.uploadProfileImage(userId, image)
        }
    }

    /**
     * Deletes the current profile image.
     */
    fun deleteProfileImage() {
        onViewModelScope {
            profileImageRepository.deleteProfileImageByUserId(userId)
        }
    }

    /**
     * Fetches new user data from the API and checks if the user has been deleted.
     */
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
