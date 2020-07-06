package eu.yeger.koffee.ui.user.editing

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.utility.nullIfBlank
import eu.yeger.koffee.utility.sourcedLiveData

/**
 * ViewModel for updating users.
 *
 * @property userId The id of the user that is being edited.
 * @property adminRepository [AdminRepository] for accessing authentication tokens.
 * @property userRepository [UserRepository] for updating the user.
 * @property userName Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the user name.
 * @property userPassword Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the user password.
 * @property isAdmin Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the admin status.
 * @property canUpdateUser Indicates that updating a user is possible with the current input values.
 * @property userUpdatedAction [DataAction] that is activated when the user has been updated.
 *
 * @author Jan Müller
 */
class UserEditingViewModel(
    val userId: String,
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    val userName = MutableLiveData("")

    val userPassword = MutableLiveData("")

    val isAdmin = MutableLiveData(false)

    val canUpdateUser = sourcedLiveData(userName, userPassword, isAdmin) {
        userName.value.isNullOrBlank().not() &&
                (isAdmin.value!!.not() ||
                userPassword.value.isNullOrBlank().not() && userPassword.value!!.length >= 8)
    }

    val userUpdatedAction = DataAction<String>()

    init {
        onViewModelScope {
            userRepository.getUserById(userId)?.run {
                userName.value = name
            }
        }
    }

    /**
     * Updates the name, password and admin status of the user with the given id.
     */
    fun updateUser() {
        onViewModelScope {
            adminRepository.getJWT()?.let {
                userRepository.updateUser(
                    userId = userId,
                    userName = userName.value!!,
                    password = userPassword.value.nullIfBlank(),
                    isAdmin = isAdmin.value!!,
                    jwt = it
                )
                userUpdatedAction.activateWith(userId)
            }
        }
    }
}
