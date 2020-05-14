package eu.yeger.koffee.ui.login

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.utility.ActionLiveData
import eu.yeger.koffee.utility.sourcedLiveData

class LoginViewModel(private val adminRepository: AdminRepository) : CoroutineViewModel() {

    val userId = MutableLiveData("")

    val password = MutableLiveData("")

    val canLogin = sourcedLiveData(userId, password) {
        userId.value.isNullOrBlank().not() && password.value.isNullOrBlank().not()
    }

    val loggedInAction = ActionLiveData(false)

    fun login() {
        onViewModelScope {
            adminRepository.login(userId = userId.value!!, password = password.value!!)
            loggedInAction.trigger(true)
        }
    }
}
