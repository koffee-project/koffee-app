package eu.yeger.koffee.ui.user.editing

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.Action
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.utility.nullIfBlank
import eu.yeger.koffee.utility.sourcedLiveData

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

    val userUpdatedAction = Action<String>()

    init {
        onViewModelScope {
            userRepository.getUserById(userId)?.run {
                userName.value = name
            }
        }
    }

    fun updateUser() {
        onViewModelScope {
            val jwt = adminRepository.getJWT()!!
            userRepository.updateUser(
                userId = userId,
                userName = userName.value!!,
                password = userPassword.value.nullIfBlank(),
                isAdmin = isAdmin.value!!,
                jwt = jwt
            )
            userUpdatedAction.activateWith(userId)
        }
    }
}
