package eu.yeger.koffee.ui.login

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.SuccessErrorViewModel
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

class LoginViewModel(private val adminRepository: AdminRepository) : SuccessErrorViewModel<Boolean>() {

    val userId = MutableLiveData("")

    val password = MutableLiveData("")

    val canLogin = sourcedLiveData(userId, password) {
        userId.value.isNullOrBlank().not() && password.value.isNullOrBlank().not()
    }

    fun login() {
        viewModelScope.launch(exceptionHandler) {
            adminRepository.login(userId = userId.value!!, password = password.value!!)
            _successAction.value = true
        }
    }

    class Factory(
        private val adminRepository: AdminRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return LoginViewModel(adminRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
