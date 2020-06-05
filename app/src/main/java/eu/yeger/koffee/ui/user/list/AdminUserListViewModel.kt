package eu.yeger.koffee.ui.user.list

import androidx.lifecycle.asLiveData
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction

class AdminUserListViewModel(
    adminRepository: AdminRepository,
    userRepository: UserRepository
) : UserListViewModel(userRepository) {
    override val isAuthenticated = adminRepository.isAuthenticatedFlow().asLiveData()

    val createUserAction = SimpleAction()
    val userSelectedAction = DataAction<Pair<Boolean, User>>()

    override fun activateCreateUserAction() = createUserAction.activate()

    override fun activateUserSelectedAction(user: User) =
        userSelectedAction.activateWith((isAuthenticated.value ?: false) to user)
}
