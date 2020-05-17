package eu.yeger.koffee.ui.user.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SearchViewModel
import eu.yeger.koffee.ui.SimpleAction

class UserListViewModel(
    private val userRepository: UserRepository,
    adminRepository: AdminRepository
) : SearchViewModel<User>(userRepository.getUsersAsLiveData()) {

    val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val createUserAction = SimpleAction()
    val userSelectedAction = DataAction<Pair<Boolean, User>>()

    fun refreshUsers() {
        onViewModelScope {
            _refreshing.value = true
            userRepository.fetchUsers()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    fun activateCreateUserAction() = createUserAction.activate()

    fun activateUserSelectedAction(user: User) =
        userSelectedAction.activateWith((isAuthenticated.value ?: false) to user)

    override fun getSource(filter: Filter) = userRepository.getFilteredUsersAsLiveData(filter)
}
