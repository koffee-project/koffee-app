package eu.yeger.koffee.ui.login

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class LoginViewModel(private val adminRepository: AdminRepository) : ViewModel() {

    val userId = MutableLiveData("")

    val password = MutableLiveData("")

    val canLogin = sourcedLiveData(userId, password) {
        userId.value.isNullOrBlank().not() && password.value.isNullOrBlank().not()
    }

    private val _loginErrorAction = MutableLiveData<String?>()
    val loginErrorAction: LiveData<String?> = _loginErrorAction

    private val _loginSuccessAction = MutableLiveData(false)
    val loginSuccessAction: LiveData<Boolean> = _loginSuccessAction

    private val loginExceptionHandler = CoroutineExceptionHandler { _, exception ->
        _loginErrorAction.postValue(exception.localizedMessage)
    }

    fun login() {
        viewModelScope.launch(loginExceptionHandler) {
            adminRepository.login(userId = userId.value!!, password = password.value!!)
            _loginSuccessAction.value = true
        }
    }

    fun onLoginErrorActionHandled() {
        viewModelScope.launch {
            _loginErrorAction.value = null
        }
    }

    fun onLoginSuccessActionHandled() {
        viewModelScope.launch {
            _loginSuccessAction.value = null
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
