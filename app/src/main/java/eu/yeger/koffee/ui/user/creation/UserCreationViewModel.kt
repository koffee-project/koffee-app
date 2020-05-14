package eu.yeger.koffee.ui.user.creation

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.Action
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.utility.sourcedLiveData

class UserCreationViewModel(
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    val userId = MutableLiveData("")

    val userName = MutableLiveData("")

    val userPassword = MutableLiveData("")

    val isAdmin = MutableLiveData(false)

    val canCreateUser = sourcedLiveData(userId, userName, userPassword, isAdmin) {
        userId.value.isNullOrBlank().not() &&
                userName.value.isNullOrBlank().not() &&
                (isAdmin.value!!.not() ||
                userPassword.value.isNullOrBlank().not() && userPassword.value!!.length >= 8)
    }

    val userCreatedAction = Action<String?>()

    fun createUser() {
        onViewModelScope {
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
            userCreatedAction.activate(userId)
        }
    }
}
