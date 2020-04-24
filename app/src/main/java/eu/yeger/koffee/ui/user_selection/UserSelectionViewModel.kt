package eu.yeger.koffee.ui.user_selection

import androidx.lifecycle.*
import eu.yeger.koffee.domain.UserEntry
import eu.yeger.koffee.repository.UserEntryRepository
import eu.yeger.koffee.utility.mediatedLiveData
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

class UserSelectionViewModel(
    private val userEntryRepository: UserEntryRepository
) : ViewModel() {

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

    val refreshing = userEntryRepository.state.map { it is UserEntryRepository.State.Refreshing }

    val refreshResultAction = userEntryRepository.state.map { state ->
        when (state) {
            is UserEntryRepository.State.Done -> state
            is UserEntryRepository.State.Error -> state
            else -> null
        }
    }

    init {
        refreshUsers()
    }

    fun refreshUsers() {
        viewModelScope.launch {
            userEntryRepository.refreshUsers()
        }
    }

    fun onRefreshResultActionHandled() {
        viewModelScope.launch {
            (refreshResultAction as MutableLiveData).value = null
        }
    }

    class Factory(
        private val userEntryRepository: UserEntryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserSelectionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserSelectionViewModel(userEntryRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
