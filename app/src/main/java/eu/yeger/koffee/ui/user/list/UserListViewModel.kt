package eu.yeger.koffee.ui.user.list

import androidx.lifecycle.*
import eu.yeger.koffee.domain.UserEntry
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserEntryRepository
import eu.yeger.koffee.ui.SuccessErrorViewModel
import eu.yeger.koffee.utility.mediatedLiveData
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

class UserListViewModel(
    private val userEntryRepository: UserEntryRepository,
    adminRepository: AdminRepository
) : SuccessErrorViewModel<String>() {

    val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    private val users = userEntryRepository.users

    private val _isBusy: MutableLiveData<Boolean> = mediatedLiveData {
        addSource(users) { userEntries: List<UserEntry>? ->
            value = userEntries?.size ?: 0 == 0 || value ?: false
        }
        value = true
    }
    val isBusy: LiveData<Boolean> = _isBusy

    val searchQuery = MutableLiveData<String>()

    val filteredUsers = sourcedLiveData(users, searchQuery) {
        UserEntryRepository.Filter(query = searchQuery.value ?: "")
    }.switchMap { filter ->
        _isBusy.value = true
        userEntryRepository.filteredUsers(filter)
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

    private val _createUserAction = MutableLiveData(false)
    val createUserAction: LiveData<Boolean> = _createUserAction

    private val _userEntrySelectedAction = MutableLiveData<Pair<Boolean, UserEntry>>(null)
    val userEntrySelectedAction: LiveData<Pair<Boolean, UserEntry>> = _userEntrySelectedAction

    init {
        refreshUsers()
    }

    fun refreshUsers() {
        viewModelScope.launch(exceptionHandler) {
            _refreshing.value = true
            userEntryRepository.refreshUsers()
        }.invokeOnCompletion {
            _refreshing.postValue(false)
        }
    }

    fun triggerCreateUserAction() {
        viewModelScope.launch {
            _createUserAction.value = true
        }
    }

    fun onCreateUserActionHandled() {
        viewModelScope.launch {
            _createUserAction.value = false
        }
    }

    fun triggerUserEntrySelectedAction(userEntry: UserEntry) {
        viewModelScope.launch {
            _userEntrySelectedAction.value = (isAuthenticated.value ?: false) to userEntry
        }
    }

    fun onUserEntrySelectedActionHandled() {
        viewModelScope.launch {
            _userEntrySelectedAction.value = null
        }
    }
}
