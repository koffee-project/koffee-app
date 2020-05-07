package eu.yeger.koffee.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userId: String?,
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val user = userRepository.getUserById(userId)

    val hasUser = user.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserId(userId)

    val canRefund = transactionRepository.getLastRefundableTransactionByUserId(userId).map { it != null }

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

    class Factory(
        private val userId: String?,
        private val userRepository: UserRepository,
        private val transactionRepository: TransactionRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(userId, userRepository, transactionRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    private object TransactionComparator : Comparator<Transaction> {

        override fun compare(first: Transaction, second: Transaction): Int {
            return when {
                first.timestamp > second.timestamp -> 1 // First transaction is newer
                first.timestamp == second.timestamp -> when {
                    second is Transaction.Refund -> -1 // Second transaction is more important
                    first is Transaction.Refund -> 1 // First transaction is more important
                    else -> 0 // Equal
                }
                first.timestamp < second.timestamp -> -1 // Second transaction is newer
                else -> 0 // Equal
            }
        }
    }
}
