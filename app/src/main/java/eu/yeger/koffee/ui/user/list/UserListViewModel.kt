package eu.yeger.koffee.ui.user.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import eu.yeger.koffee.database.Filter
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.SearchViewModel

private const val PAGE_SIZE = 50

abstract class UserListViewModel(
    private val userRepository: UserRepository
) : SearchViewModel<User>(userRepository.getAllUser().toLiveData(PAGE_SIZE)) {

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    abstract val isAuthenticated: LiveData<Boolean>

    abstract fun activateUserSelectedAction(user: User)

    abstract fun activateCreateUserAction()

    fun refreshUsers() {
        onViewModelScope {
            _refreshing.value = true
            userRepository.fetchUsers()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    override fun getSource(filter: Filter): LiveData<PagedList<User>> {
        return userRepository.getFilteredUsers(filter).toLiveData(PAGE_SIZE)
    }
}
