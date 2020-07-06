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

/**
 * [UserDetailsViewModel] for user management.
 *
 * @property isActiveUser Indicates if this is the active user. Blocks deletion if true.
 * @property userId The id of the user.
 * @property adminRepository [AdminRepository] for accessing authentication tokens.
 * @property userRepository [UserRepository] for accessing user data.
 * @property canModify Indicates tha modifying the profile image is possible.
 * @property canDelete Indicates tha deleting the profile image is possible.
 * @property showItemsButton Deprecated.
 * @property editUserAction [DataAction] that is activated when editing of the user is requested.
 * @property creditUserAction [DataAction] that is activated when crediting of the user is requested.
 * @property deleteUserAction [DataAction] that is activated when deletion of the user is requested.
 * @property userDeletedAction [SimpleAction] that is activated when the user has been deleted.
 * @param profileImageRepository [ProfileImageRepository] for accessing profile images.
 * @param transactionRepository [TransactionRepository] for accessing transactions.
 *
 * @author Jan Müller
 */
class AdminUserDetailsViewModel(
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

    /**
     * Deletes the user.
     */
    fun deleteUser() {
        userId?.let {
            onViewModelScope {
                val jwt = adminRepository.getJWT()!!
                userRepository.deleteUser(userId, jwt)
                userDeletedAction.activate()
            }
        }
    }

    /**
     * Ignored.
     */
    override fun activateShowItemsAction() = Unit

    /**
     * Requests editing of the user.
     */
    override fun activateEditUserAction() = editUserAction.activateWith(user.value?.id)

    /**
     * Requests crediting of the user.
     */
    override fun activateCreditUserAction() = creditUserAction.activateWith(user.value?.id)

    /**
     * Requests deletion of the user.
     */
    override fun activateDeleteUserAction() = deleteUserAction.activateWith(user.value?.id)
}
