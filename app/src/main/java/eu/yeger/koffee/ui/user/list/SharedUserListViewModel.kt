package eu.yeger.koffee.ui.user.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction

/**
 * [UserListViewModel] for the multi user mode.
 *
 * @property isAuthenticated Always contains false.
 * @property userSelectedAction [DataAction] that is activated when a user has been selected.
 * @param userRepository [UserRepository] for accessing, filtering and refreshing users.
 *
 * @author Jan Müller
 */
class SharedUserListViewModel(
    userRepository: UserRepository
) : UserListViewModel(userRepository) {
    override val isAuthenticated: LiveData<Boolean>
        get() = MutableLiveData(false)

    val userSelectedAction = DataAction<String>()

    /**
     * Requests the selection of the given user.
     *
     * @param user The user to be selected.
     */
    override fun activateUserSelectedAction(user: User) = userSelectedAction.activateWith(user.id)

    /**
     * Ignored.
     */
    override fun activateCreateUserAction() = Unit
}
