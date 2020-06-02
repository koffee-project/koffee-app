package eu.yeger.koffee.ui.user.details

import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.sourcedLiveData

class MainUserDetailsViewModel(
    private val isActiveUser: Boolean,
    private val userId: String?,
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository,
    profileImageRepository: ProfileImageRepository,
    transactionRepository: TransactionRepository
) : UserDetailsViewModel(
    userId = userId,
    profileImageRepository = profileImageRepository,
    transactionRepository = transactionRepository,
    userRepository = userRepository
) {

    private val isAuthenticated = adminRepository.isAuthenticatedFlow().asLiveData()

    override val canModify = sourcedLiveData(isAuthenticated, hasUser) {
        isAuthenticated.value == true && hasUser.value == true
    }

    override val canDelete = canModify.map { !isActiveUser && it }

    override val showItemsButton = false

    val editUserAction = DataAction<String>()
    val creditUserAction = DataAction<String>()
    val deleteUserAction = DataAction<String>()
    val userDeletedAction = SimpleAction()

    fun deleteUser() {
        userId?.let {
            onViewModelScope {
                val jwt = adminRepository.getJWT()!!
                userRepository.deleteUser(userId, jwt)
                userDeletedAction.activate()
            }
        }
    }

    override fun activateShowItemsAction() = Unit

    override fun activateEditUserAction() = editUserAction.activateWith(user.value?.id)

    override fun activateCreditUserAction() = creditUserAction.activateWith(user.value?.id)

    override fun activateDeleteUserAction() = deleteUserAction.activateWith(user.value?.id)
}
