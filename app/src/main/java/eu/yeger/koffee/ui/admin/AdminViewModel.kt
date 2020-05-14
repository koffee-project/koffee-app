package eu.yeger.koffee.ui.admin

import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.utility.ActionLiveData

class AdminViewModel(private val adminRepository: AdminRepository) : CoroutineViewModel() {

    val loginRequiredAction = ActionLiveData(false)

    init {
        onViewModelScope {
            loginRequiredAction.trigger(adminRepository.loginRequired())
        }
    }

    fun logout() {
        onViewModelScope {
            adminRepository.logout()
            loginRequiredAction.trigger(true)
        }
    }
}
