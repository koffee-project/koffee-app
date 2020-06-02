package eu.yeger.koffee.ui.alternative.item.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.item.details.ItemDetailsViewModel

class AlternativeItemDetailsViewModel(
    itemId: String,
    userId: String,
    itemRepository: ItemRepository,
    transactionRepository: TransactionRepository,
    userRepository: UserRepository
) : ItemDetailsViewModel(
    itemId = itemId,
    userId = userId,
    itemRepository = itemRepository,
    transactionRepository = transactionRepository,
    userRepository = userRepository
) {

    override val canModify: LiveData<Boolean>
        get() = MutableLiveData(false)

    override fun activateEditItemAction() = Unit

    override fun activateDeleteItemAction() = Unit
}
