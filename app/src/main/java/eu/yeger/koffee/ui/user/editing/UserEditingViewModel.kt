package eu.yeger.koffee.ui.user.editing

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.SuccessViewModel
import eu.yeger.koffee.utility.sourcedLiveData

class UserEditingViewModel(
    val userId: String,
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository
) : SuccessViewModel<String>() {

    val userName = MutableLiveData("")

    val userPassword = MutableLiveData("")

    val isAdmin = MutableLiveData(false)

    val canUpdateUser = sourcedLiveData(userName, userPassword, isAdmin) {
        userName.value.isNullOrBlank().not() &&
                (isAdmin.value!!.not() ||
                userPassword.value.isNullOrBlank().not() && userPassword.value!!.length >= 8)
    }

    init {
        launchOnViewModelScope {
            userRepository.getUserById(userId)?.run {
                userName.value = name
            }
        }
    }

    fun updateUser() {
        launchOnViewModelScope {
            val jwt = adminRepository.getJWT()!!
            val actualPassword = when {
                userPassword.value.isNullOrBlank() -> null
                else -> userPassword.value
            }
            userRepository.updateUser(
                userId = userId,
                userName = userName.value!!,
                password = actualPassword,
                isAdmin = isAdmin.value!!,
                jwt = jwt
            )
            setSuccessResult(userId)
        }
    }
}
