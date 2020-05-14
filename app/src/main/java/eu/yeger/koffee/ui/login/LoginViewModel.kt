package eu.yeger.koffee.ui.login

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.SuccessViewModel
import eu.yeger.koffee.utility.sourcedLiveData

class LoginViewModel(private val adminRepository: AdminRepository) : SuccessViewModel<Boolean>() {

    val userId = MutableLiveData("")

    val password = MutableLiveData("")

    val canLogin = sourcedLiveData(userId, password) {
        userId.value.isNullOrBlank().not() && password.value.isNullOrBlank().not()
    }

    fun login() {
        onViewModelScope {
            adminRepository.login(userId = userId.value!!, password = password.value!!)
            setSuccessResult(true)
        }
    }
}
