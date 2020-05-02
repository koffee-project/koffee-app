package eu.yeger.koffee.ui.item_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import kotlinx.coroutines.launch

class ItemDetailsViewModel(
    private val itemId: String,
    private val userId: String?,
    private val itemRepository: ItemRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    val hasUser = userId != null

    val item = itemRepository.getItemById(itemId)

    val hasItem = item.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserIdAndItemId(userId, itemId)

    val hasTransactions = transactions.map { it.isNotEmpty() }

    fun buyItem() {
        viewModelScope.launch {
            transactionRepository.buyItem(userId!!, itemId, 1) // TODO enable buying multiple at once?
            transactionRepository.fetchTransactionsByUserId(userId)
        }
    }

    class Factory(
        private val itemId: String,
        private val userId: String?,
        private val itemRepository: ItemRepository,
        private val transactionRepository: TransactionRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemDetailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ItemDetailsViewModel(itemId = itemId, userId = userId, itemRepository = itemRepository, transactionRepository = transactionRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
