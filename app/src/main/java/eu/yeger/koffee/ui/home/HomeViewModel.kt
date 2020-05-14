package eu.yeger.koffee.ui.home

import androidx.lifecycle.*
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel

class HomeViewModel(
    private val userId: String?,
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository
) : CoroutineViewModel() {

    private val _userSelectionRequiredAction = MutableLiveData(userId === null)
    val userSelectionRequiredAction: LiveData<Boolean> = _userSelectionRequiredAction

    init {
        userId?.let {
            onViewModelScope {
                userRepository.fetchUserById(userId)
                _userSelectionRequiredAction.value = userRepository.hasUserWithId(userId).not()
            }
            onViewModelScope {
                transactionRepository.fetchTransactionsByUserId(userId)
            }
        }
    }

    fun onUserSelectionRequiredActionHandled() = _userSelectionRequiredAction.postValue(false)
}
