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

/**
 * Abstract [SearchViewModel] for accessing available users.
 *
 * @property userRepository [UserRepository] for accessing, filtering and refreshing users.
 * @property refreshing Indicates that a refresh is in progress.
 * @property isAuthenticated Indicates that a user is authenticated. Abstract.
 *
 * @author Jan Müller
 */
abstract class UserListViewModel(
    private val userRepository: UserRepository
) : SearchViewModel<User>(userRepository.getAllUser().toLiveData(PAGE_SIZE)) {

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    abstract val isAuthenticated: LiveData<Boolean>

    /**
     * Requests the selection of a users.
     */
    abstract fun activateUserSelectedAction(user: User)

    /**
     * Requests the creation of users.
     */
    abstract fun activateCreateUserAction()

    /**
     * Refreshes the available users.
     */
    fun refreshUsers() {
        onViewModelScope {
            _refreshing.value = true
            userRepository.fetchUsers()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    /**
     * Get the users for the given filter.
     *
     * @param filter The filter derived from the search query.
     * @return The filtered users.
     */
    override fun getSource(filter: Filter): LiveData<PagedList<User>> {
        return userRepository.getFilteredUsers(filter).toLiveData(PAGE_SIZE)
    }
}
