package eu.yeger.koffee.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userId: String?,
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val user = userRepository.getUserById(userId)

    val transactions = transactionRepository.getTransactionsByUserId(userId)

    val hasUser = user.map { it != null }

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
}
