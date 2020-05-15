package eu.yeger.koffee.ui.user.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.Action
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.mediatedLiveData
import eu.yeger.koffee.utility.sourcedLiveData

class UserListViewModel(
    private val userRepository: UserRepository,
    adminRepository: AdminRepository
) : CoroutineViewModel() {

    val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    private val users = userRepository.getUsersAsLiveData()

    private val _isBusy: MutableLiveData<Boolean> = mediatedLiveData {
        addSource(users) { userEntries: List<User>? ->
            value = userEntries?.size ?: 0 == 0 || value ?: false
        }
        value = true
    }
    val isBusy: LiveData<Boolean> = _isBusy

    val searchQuery = MutableLiveData<String>()

    val filteredUsers = sourcedLiveData(users, searchQuery) {
        UserRepository.Filter(query = searchQuery.value ?: "")
    }.switchMap { filter ->
        _isBusy.value = true
        userRepository.filteredUsers(filter)
    }
    val onFilteredUsersApplied = Runnable { _isBusy.postValue(false) }

    val hasResults: LiveData<Boolean> = sourcedLiveData(filteredUsers) {
        filteredUsers.value?.size ?: 0 > 0
    }
    val hasNoResults: LiveData<Boolean> = sourcedLiveData(filteredUsers, isBusy) {
        filteredUsers.value?.size == 0 && !(isBusy.value ?: false)
    }

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val createUserAction = SimpleAction()
    val userSelectedAction = Action<Pair<Boolean, User>>()

    init {
        refreshUsers()
    }

    fun refreshUsers() {
        onViewModelScope {
            _refreshing.value = true
            userRepository.fetchUsers()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    fun triggerCreateUserAction() = createUserAction.activate()

    fun triggerUserSelectedAction(user: User) =
        userSelectedAction.activateWith((isAuthenticated.value ?: false) to user)
}
