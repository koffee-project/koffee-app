package eu.yeger.koffee.ui.user.creation

import androidx.lifecycle.MutableLiveData
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

    val userPassword = MutableLiveData("")

    val isAdmin = MutableLiveData(false)

    val canCreateUser = sourcedLiveData(userId, userName, userPassword, isAdmin) {
        userId.value.isNullOrBlank().not()
                && userName.value.isNullOrBlank().not()
                && (isAdmin.value!!.not()
                || userPassword.value.isNullOrBlank().not() && userPassword.value!!.length >= 8)
    }

    fun createUser() {
        viewModelScope.launch(exceptionHandler) {
            val jwt = adminRepository.getJWT()!!
            val userId = userId.value!!
            val actualPassword = when {
                userPassword.value.isNullOrBlank() -> null
                else -> userPassword.value
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
