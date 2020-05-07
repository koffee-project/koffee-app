package eu.yeger.koffee.ui.item_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import kotlinx.coroutines.launch

class ItemDetailsViewModel(
    private val itemId: String,
    private val userId: String?,
    private val itemRepository: ItemRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val user = userRepository.getUserById(userId)

    val hasUser = user.map { it != null }

    val item = itemRepository.getItemById(itemId)

    val hasItem = item.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserIdAndItemId(userId, itemId)

    val hasTransactions = transactions.map { it.isNotEmpty() }

    val canRefund = transactionRepository.getLastRefundableTransactionByUserId(userId).map { it?.itemId == itemId }

    fun buyItem() {
        userId?.let {
            viewModelScope.launch {
                transactionRepository.buyItem(userId, itemId, 1)
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
            }
        }
    }

    class Factory(
        private val itemId: String,
        private val userId: String?,
        private val itemRepository: ItemRepository,
        private val transactionRepository: TransactionRepository,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ItemDetailsViewModel(
                    itemId = itemId,
                    userId = userId,
                    itemRepository = itemRepository,
                    transactionRepository = transactionRepository,
                    userRepository = userRepository
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}