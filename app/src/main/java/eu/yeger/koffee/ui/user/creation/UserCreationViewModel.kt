package eu.yeger.koffee.ui.user.creation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.SuccessErrorViewModel
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

class UserCreationViewModel(
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository
) : SuccessErrorViewModel<String>() {

    val userId = MutableLiveData("")

    val userName = MutableLiveData("")

    val password = MutableLiveData("")

    val isAdmin = MutableLiveData(false)

    val canCreateUser = sourcedLiveData(userId, userName, password, isAdmin) {
        userId.value.isNullOrBlank().not() &&
                userName.value.isNullOrBlank().not() &&
                (isAdmin.value!!.not() || password.value.isNullOrBlank().not())
    }

    // TODO catch exceptions
    fun createUser() {
        viewModelScope.launch(exceptionHandler) {
            val jwt = adminRepository.getJWT()!!
            val userId = userId.value!!
            val actualPassword = when {
                password.value.isNullOrBlank() -> null
                else -> password.value
            }
            userRepository.createUser(
                userId = userId,
                userName = userName.value!!,
                password = actualPassword,
                isAdmin = isAdmin.value!!,
                jwt = jwt
            )
            _successAction.value = userId
        }
    }
}
