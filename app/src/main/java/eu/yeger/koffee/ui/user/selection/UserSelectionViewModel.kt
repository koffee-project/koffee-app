package eu.yeger.koffee.ui.user.selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.user.list.UserListViewModel

/**
 * [UserListViewModel] for the single user mode's user selection.
 *
 * @property isAuthenticated Always contains false.
 * @property userSelectedAction [DataAction] that is activated when a user has been selected. Contains the user.
 * @param userRepository [UserRepository] for accessing, filtering and refreshing users.
 *
 * @author Jan Müller
 */
class UserSelectionViewModel(userRepository: UserRepository) : UserListViewModel(userRepository) {
    override val isAuthenticated: LiveData<Boolean>
        get() = MutableLiveData(false)

    val userSelectedAction = DataAction<User>()

    /**
     * Requests the selection of the given user.
     *
     * @param user The user to be selected.
     */
    override fun activateUserSelectedAction(user: User) = userSelectedAction.activateWith(user)

    /**
     * Ignored.
     */
    override fun activateCreateUserAction() = Unit
}
