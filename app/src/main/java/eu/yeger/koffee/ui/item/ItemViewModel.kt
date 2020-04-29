package eu.yeger.koffee.ui.item

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.home.HomeViewModel

class ItemViewModel(
    itemId: String,
    userId: String?,
    itemRepository: ItemRepository,
    userRepository: UserRepository,
    transactionRepository: TransactionRepository
) : ViewModel() {

    class Factory(
        private val itemId: String,
        private val userId: String?,
        private val itemRepository: ItemRepository,
        private val userRepository: UserRepository,
        private val transactionRepository: TransactionRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ItemViewModel(itemId, userId, itemRepository, userRepository, transactionRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
