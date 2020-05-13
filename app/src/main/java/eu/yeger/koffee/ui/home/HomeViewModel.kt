package eu.yeger.koffee.ui.home

import androidx.lifecycle.*
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userId: String?,
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _userSelectionRequiredAction = MutableLiveData(userId === null)
    val userSelectionRequiredAction: LiveData<Boolean> = _userSelectionRequiredAction

    init {
        userId?.let {
            viewModelScope.launch {
                userRepository.fetchUserById(userId)
                _userSelectionRequiredAction.value = userRepository.hasUserWithId(userId).not()
            }
            viewModelScope.launch {
                transactionRepository.fetchTransactionsByUserId(userId)
            }
        }
    }

    fun onUserSelectionRequiredActionHandled() {
        viewModelScope.launch {
            _userSelectionRequiredAction.value = false
        }
    }
}
