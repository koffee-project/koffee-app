package eu.yeger.koffee.ui.user.details

import android.app.Activity
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.paging.toLiveData
import com.github.dhaval2404.imagepicker.ImagePicker
import eu.yeger.koffee.R
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.showSnackbar
import java.io.File

private const val PAGE_SIZE = 50

/**
 * [CoroutineViewModel] for accessing user details.
 *
 * @property userId The id of the user.
 * @property profileImageRepository [ProfileImageRepository] for accessing profile images.
 * @property transactionRepository [TransactionRepository] for accessing transactions.
 * @property userRepository [UserRepository] for accessing user data.
 * @property user A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the user's profile image.
 * @property hasUser Indicates that the user is non-null.
 * @property profileImage A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the user.
 * @property canEditProfileImage Indicates that editing the profile image is possible.
 * @property canDeleteProfileImage Indicates that deleting the profile image is possible.
 * @property transactions A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the transaction for this item and user.
 * @property hasTransactions Indicates that the transactions are not empty.
 * @property canModify Indicates tha modifying the profile image is possible. Abstract.
 * @property canDelete Indicates tha deleting the profile image is possible. Abstract.
 * @property refreshing Indicates that a refresh is in progress
 * @property userNotFoundAction [SimpleAction] that is activated when the user has been deleted.
 * @property showItemsButton Deprecated.
 *
 * @author Jan Müller
 */
abstract class UserDetailsViewModel(
    private val userId: String?,
    private val profileImageRepository: ProfileImageRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    val user = userRepository.getUserByIdFlow(userId).asLiveData()
    val hasUser = user.map { it !== null }

    val profileImage = profileImageRepository.getProfileImageByUserIdAsFlow(userId).asLiveData()
    val canEditProfileImage = user.map { it !== null }
    val canDeleteProfileImage = profileImage.map { it !== null }

    val transactions = transactionRepository.getTransactionsByUserId(userId).toLiveData(PAGE_SIZE)
    val hasTransactions = transactions.map { it.isNotEmpty() }

    abstract val canModify: LiveData<Boolean>
    abstract val canDelete: LiveData<Boolean>

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    private val editProfileImageAction = SimpleAction()
    private val deleteProfileImageAction = SimpleAction()
    val userNotFoundAction = SimpleAction()

    @Deprecated(message = "Use HomeFragment for regular usage instead.")
    abstract val showItemsButton: Boolean

    /**
     * Refreshes user data.
     */
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

    /**
     * Uploads the profile image.
     *
     * @param image The file containing the image.
     */
    private fun uploadProfileImage(image: File) {
        userId?.let {
            onViewModelScope {
                profileImageRepository.uploadProfileImage(userId, image)
            }
        }
    }

    /**
     * Requests deletion of the image.
     */
    private fun deleteProfileImage() {
        userId?.let {
            onViewModelScope {
                profileImageRepository.deleteProfileImageByUserId(userId)
            }
        }
    }

    /**
     * Requests editing of the profile image.
     */
    fun activateEditProfileImageAction() = editProfileImageAction.activate()

    /**
     * Requests deletion of the profile image.
     */
    fun activateDeleteProfileImageAction() = deleteProfileImageAction.activate()

    /**
     * Deprecated.
     */
    @Deprecated(message = "Use HomeFragment for regular usage instead.")
    abstract fun activateShowItemsAction()

    /**
     * Requests editing of the user.
     */
    abstract fun activateEditUserAction()

    /**
     * Requests crediting of the user.
     */
    abstract fun activateCreditUserAction()

    /**
     * Requests deletion of the user.
     */
    abstract fun activateDeleteUserAction()

    /**
     * Observes both actions related to the profile image.
     */
    fun Fragment.observeProfileImageActions() {
        observeAction(editProfileImageAction) {
            ImagePicker.with(this)
                .compress(8 * 1024) // Limit size to 8MB
                .start { resultCode, data ->
                    when (resultCode) {
                        Activity.RESULT_OK -> {
                            val image = ImagePicker.getFile(data)!!
                            uploadProfileImage(image)
                        }
                        ImagePicker.RESULT_ERROR -> requireActivity().showSnackbar(
                            ImagePicker.getError(data)
                        )
                    }
                }
        }

        observeAction(deleteProfileImageAction) {
            AlertDialog.Builder(requireContext())
                .setMessage(R.string.delete_profile_image_confirmation)
                .setPositiveButton(R.string.delete) { _, _ ->
                    deleteProfileImage()
                }
                .setNegativeButton(R.string.cancel) { _, _ -> /*ignore*/ }
                .create()
                .show()
        }
    }
}
