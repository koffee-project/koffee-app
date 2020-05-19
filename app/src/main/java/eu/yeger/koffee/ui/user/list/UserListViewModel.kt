package eu.yeger.koffee.ui.user.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SearchViewModel
import eu.yeger.koffee.ui.SimpleAction

private const val PAGE_SIZE = 50

class UserListViewModel(
    private val userRepository: UserRepository,
    adminRepository: AdminRepository
) : SearchViewModel<User>(userRepository.getAllUser().toLiveData(PAGE_SIZE)) {

    val isAuthenticated = adminRepository.isAuthenticatedFlow().asLiveData()

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

    override fun getSource(filter: Filter): LiveData<PagedList<User>> {
        return userRepository.getFilteredUsers(filter).toLiveData(PAGE_SIZE)
    }
}
