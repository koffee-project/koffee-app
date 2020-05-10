package eu.yeger.koffee.ui.user_creation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

class UserCreationViewModel(
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val userId = MutableLiveData("")

    val userName = MutableLiveData("")

    val password = MutableLiveData("")

    val isAdmin = MutableLiveData(false)

    val canCreateUser = sourcedLiveData(userId, userName, password, isAdmin) {
        userId.value.isNullOrBlank().not()
                && userName.value.isNullOrBlank().not()
                && (isAdmin.value!!.not() || password.value.isNullOrBlank().not())
    }

    // TODO catch exceptions
    fun createUser() {
        viewModelScope.launch {
            val jwt = adminRepository.getJWT()!!
            val actualPassword = when {
                password.value.isNullOrBlank() -> null
                else -> password.value
            }
            userRepository.createUser(
                userId = userId.value!!,
                userName = userName.value!!,
                password = actualPassword,
                isAdmin = isAdmin.value!!,
                jwt = jwt
            )
        }
    }

    class Factory(
        private val adminRepository: AdminRepository,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserCreationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserCreationViewModel(adminRepository, userRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}