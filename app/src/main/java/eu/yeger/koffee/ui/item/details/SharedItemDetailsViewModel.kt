package eu.yeger.koffee.ui.item.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository

/**
 * [ItemDetailsViewModel] for the multi user mode.
 *
 * @property canModify Always contains false.
 * @param itemId The id of the item.
 * @param userId The id of the user.
 * @param itemRepository [ItemRepository] for accessing and refreshing item data.
 * @param transactionRepository [TransactionRepository] for accessing and refreshing transactions.
 * @param userRepository [UserRepository] for accessing user data.
 *
 * @author Jan Müller
 */
class SharedItemDetailsViewModel(
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

    /**
     * Ignored.
     */
    override fun activateEditItemAction() = Unit

    /**
     * Ignored.
     */
    override fun activateDeleteItemAction() = Unit
}
