package eu.yeger.koffee.ui.settings

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.formatTimestamp

/**
 * ViewModel for various options like navigating to user selection, launching the shared mode as well as admin tools.
 *
 * @property adminRepository [AdminRepository] for admin related actions.
 * @property isAuthenticated Indicates that a user is currently authenticated.
 * @property token A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the current token.
 * @property tokenExpiration A [LiveData](https://developer.android.com/reference/androidx/lifecycle/LiveData) that contains the formatted expiration timestamp of the token.
 * @property selectUserAction [SimpleAction] that is activated when the launch of the user selection activity is requested.
 * @property launchSharedActivityAction [SimpleAction] that is activated when the launch of the multi user mode activity is requested.
 * @property loginAction [SimpleAction] that is activated when a login is requested.
 * @property manageItemsAction [SimpleAction] that is activated when navigation to the item management is requested.
 * @property manageUsersAction [SimpleAction] that is activated when navigation to the user management is requested.
 * @constructor
 * Performs a logout if the previous session expired.
 *
 * @param loginExpired Indicates that a previous login expired.
 *
 * @author Jan Müller
 */
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

    /**
     * Performs a logout.
     */
    fun logout() {
        onViewModelScope {
            adminRepository.logout()
        }
    }

    /**
     * Requests the launch of the user selection activity.
     */
    fun activateSelectUserAction() = selectUserAction.activate()

    /**
     * Requests the launch of the multi user mode activity.
     */
    fun activateLaunchSharedActivityAction() = launchSharedActivityAction.activate()

    /**
     * Requests navigation to the login screen.
     */
    fun activateLoginAction() = loginAction.activate()

    /**
     * Requests navigation to the item management screen.
     */
    fun activateManageItemsAction() = manageItemsAction.activate()

    /**
     * Requests navigation to the user management screen.
     */
    fun activateManageUsersAction() = manageUsersAction.activate()
}
