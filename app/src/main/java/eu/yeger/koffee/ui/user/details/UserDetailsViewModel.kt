package eu.yeger.koffee.ui.user.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val isActiveUser: Boolean,
    private val userId: String?,
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val user = userRepository.getUserByIdAsLiveData(userId)

    val hasUser = user.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserId(userId)

    val canRefund = transactionRepository.getLastRefundableTransactionByUserId(userId)
        .map { isActiveUser && it != null }

    init {
        userId?.let {
            viewModelScope.launch {
                userRepository.fetchUserById(userId)
            }
            viewModelScope.launch {
                transactionRepository.fetchTransactionsByUserId(userId)
            }
        }
    }

    fun refundPurchase() {
        if (!isActiveUser || userId === null) return

        viewModelScope.launch {
            transactionRepository.run {
                refundPurchase(userId)
                fetchTransactionsByUserId(userId)
            }
            userRepository.fetchUserById(userId)
        }
    }
}
