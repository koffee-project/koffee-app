package eu.yeger.koffee.ui.user.creation

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.utility.nullIfBlank
import eu.yeger.koffee.utility.sourcedLiveData

/**
 * ViewModel for creating users.
 *
 * @property adminRepository [AdminRepository] for accessing authentication tokens.
 * @property userRepository [UserRepository] for creating the user.
 * @property userId Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the user id.
 * @property userName Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the user name.
 * @property userPassword Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the user password.
 * @property isAdmin Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the admin status.
 * @property canCreateUser Indicates that creating a user is possible with the current input values.
 * @property userCreatedAction [DataAction] that is activated when the user has been created.
 *
 * @author Jan Müller
 */
class UserCreationViewModel(
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    val userId = MutableLiveData("")

    val userName = MutableLiveData("")

    val userPassword = MutableLiveData("")

    val isAdmin = MutableLiveData(false)

    val canCreateUser = sourcedLiveData(userName, userPassword, isAdmin) {
        userName.value.isNullOrBlank().not() &&
                (isAdmin.value!!.not() ||
                        userPassword.value.isNullOrBlank().not() &&
                        userPassword.value!!.length >= 8)
    }

    val userCreatedAction = DataAction<String>()

    /**
     * Creates a user with the current id, name, password and admin status.
     */
    fun createUser() {
        onViewModelScope {
            val jwt = adminRepository.getJWT()!!
            val userId = userRepository.createUser(
                userId = userId.value?.nullIfBlank(),
                userName = userName.value!!,
                password = userPassword.value.nullIfBlank(),
                isAdmin = isAdmin.value!!,
                jwt = jwt
            )
            userCreatedAction.activateWith(userId)
        }
    }
}
