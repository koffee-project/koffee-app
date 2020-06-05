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
    val launchSharedActivityAction = SimpleAction()
    val loginAction = SimpleAction()
    val manageItemsAction = SimpleAction()
    val manageUsersAction = SimpleAction()

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

    fun activateLaunchSharedActivityAction() = launchSharedActivityAction.activate()

    fun activateLoginAction() = loginAction.activate()

    fun activateManageItemsAction() = manageItemsAction.activate()

    fun activateManageUsersAction() = manageUsersAction.activate()
}
