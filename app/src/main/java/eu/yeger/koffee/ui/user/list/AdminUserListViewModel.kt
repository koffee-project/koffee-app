package eu.yeger.koffee.ui.user.list

import androidx.lifecycle.asLiveData
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction

/**
 * [UserListViewModel] for user management.
 *
 * @property isAuthenticated ndicates that the user is authenticated.
 * @property createUserAction [SimpleAction] that is activated when the creation of a user is requested.
 * @property userSelectedAction [DataAction] that is activated when a user has been selected.
 * @param adminRepository [AdminRepository] for accessing authentication tokens.
 * @param userRepository [UserRepository] for accessing, filtering and refreshing users.
 *
 * @author Jan Müller
 */
class AdminUserListViewModel(
    adminRepository: AdminRepository,
    userRepository: UserRepository
) : UserListViewModel(userRepository) {
    override val isAuthenticated = adminRepository.isAuthenticatedFlow().asLiveData()

    val createUserAction = SimpleAction()
    val userSelectedAction = DataAction<User>()

    /**
     * Requests the creation of a users.
     */
    override fun activateCreateUserAction() = createUserAction.activate()

    /**
     * Requests the creation of a users.
     */
    override fun activateUserSelectedAction(user: User) = userSelectedAction.activateWith(user)
}
