package eu.yeger.koffee.ui.settings

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.formatTimestamp

class SettingsViewModel(
    private val adminRepository: AdminRepository,
    loginExpired: Boolean
) : CoroutineViewModel() {

    val isAuthenticated = adminRepository.isAuthenticatedFlow().asLiveData()
    val token = adminRepository.getJWTFlow().asLiveData()
    val tokenExpiration = token.map { it?.let { token -> formatTimestamp(token.expiration) } }

    val selectUserAction = SimpleAction()
    val loginAction = SimpleAction()

    init {
        if (loginExpired) {
            logout()
        }
    }

    fun logout() {
        onViewModelScope {
            adminRepository.logout()
        }
    }

    fun activateSelectUserAction() = selectUserAction.activate()

    fun activateLoginAction() = loginAction.activate()
}
