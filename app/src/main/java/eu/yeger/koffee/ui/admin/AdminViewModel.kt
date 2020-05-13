package eu.yeger.koffee.ui.admin

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import kotlinx.coroutines.launch

class AdminViewModel(private val adminRepository: AdminRepository) : ViewModel() {

    private val _loginRequiredAction = MutableLiveData(false)
    val loginRequiredAction: LiveData<Boolean> = _loginRequiredAction

    init {
        viewModelScope.launch {
            _loginRequiredAction.value = adminRepository.loginRequired()
        }
    }

    fun onLoginRequiredActionHandled() {
        viewModelScope.launch {
            _loginRequiredAction.value = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            adminRepository.logout()
            _loginRequiredAction.value = true
        }
    }
}
