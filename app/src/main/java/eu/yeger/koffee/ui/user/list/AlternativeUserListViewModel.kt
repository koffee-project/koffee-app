package eu.yeger.koffee.ui.user.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction

class AlternativeUserListViewModel(
    userRepository: UserRepository
) : UserListViewModel(userRepository) {

    val userSelectedAction = DataAction<String>()

    override val showAlternativeActivityButton = false

    override val isAuthenticated: LiveData<Boolean>
        get() = MutableLiveData(false)

    override fun activateLaunchAlternativeActivityAction() = Unit

    override fun activateUserSelectedAction(user: User) = userSelectedAction.activateWith(user.id)

    override fun activateCreateUserAction() = Unit
}