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

    class Factory(
        private val adminRepository: AdminRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdminViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AdminViewModel(adminRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}
