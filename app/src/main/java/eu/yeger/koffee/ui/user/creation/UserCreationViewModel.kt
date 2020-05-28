package eu.yeger.koffee.ui.user.creation

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.utility.nullIfBlank
import eu.yeger.koffee.utility.sourcedLiveData

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
