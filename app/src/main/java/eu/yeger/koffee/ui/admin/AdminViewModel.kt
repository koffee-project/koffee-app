package eu.yeger.koffee.ui.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.CoroutineViewModel

class AdminViewModel(private val adminRepository: AdminRepository) : CoroutineViewModel() {

    private val _loginRequiredAction = MutableLiveData(false)
    val loginRequiredAction: LiveData<Boolean> = _loginRequiredAction

    init {
        launchOnViewModelScope {
            _loginRequiredAction.value = adminRepository.loginRequired()
        }
    }

    fun logout() {
        launchOnViewModelScope {
            adminRepository.logout()
            _loginRequiredAction.value = true
        }
    }

    fun onLoginRequiredActionHandled() = _loginRequiredAction.postValue(false)
}
