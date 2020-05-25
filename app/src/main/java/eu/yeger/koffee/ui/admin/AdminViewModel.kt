package eu.yeger.koffee.ui.admin

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.formatTimestamp

class AdminViewModel(
    private val adminRepository: AdminRepository,
    loginExpired: Boolean
) : CoroutineViewModel() {

    val loginRequiredAction = SimpleAction()

    val token = adminRepository.getJWTFlow().asLiveData()

    val tokenExpiration = token.map { it?.let { token -> formatTimestamp(token.expiration) } }

    init {
        if (loginExpired) {
            logout()
        }
    }

    fun refresh() {
        onViewModelScope {
            if (adminRepository.loginRequired()) {
                loginRequiredAction.activate()
            }
        }
    }

    fun logout() {
        onViewModelScope {
            adminRepository.logout()
            loginRequiredAction.activate()
        }
    }
}
