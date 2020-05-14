package eu.yeger.koffee.ui.admin

import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction

class AdminViewModel(private val adminRepository: AdminRepository) : CoroutineViewModel() {

    val loginRequiredAction = SimpleAction()

    init {
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
