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

    val user = userRepository.getUserByIdAsLiveData(userId)

    val hasUser = user.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserId(userId)

    val canRefund =
        transactionRepository.getLastRefundableTransactionByUserId(userId).map { it != null }

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

    fun refundPurchase() {
        userId?.let {
            viewModelScope.launch {
                transactionRepository.run {
                    refundPurchase(userId)
                    fetchTransactionsByUserId(userId)
                }
                userRepository.fetchUserById(userId)
            }
        }
    }

    fun onUserSelectionRequiredActionHandled() {
        viewModelScope.launch {
            _userSelectionRequiredAction.value = false
        }
    }
}
