package eu.yeger.koffee.ui.login

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.sourcedLiveData

/**
 * ViewModel for performing logins.
 *
 * @property adminRepository [AdminRepository] used for performing logins.
 * @property userId Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the user id.
 * @property password Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the password.
 * @property canLogin Indicates that a login is possible with the current input values.
 * @property loggedInAction Indicates that a login was successful.
 *
 * @author Jan Müller
 */
class LoginViewModel(private val adminRepository: AdminRepository) : CoroutineViewModel() {

    val userId = MutableLiveData("")

    val password = MutableLiveData("")

    val canLogin = sourcedLiveData(userId, password) {
        userId.value.isNullOrBlank().not() && password.value.isNullOrBlank().not()
    }

    val loggedInAction = SimpleAction()

    /**
     * Performs a login with the current user id and password.
     */
    fun login() {
        onViewModelScope {
            adminRepository.login(userId = userId.value!!, password = password.value!!)
            loggedInAction.activate()
        }
    }
}
