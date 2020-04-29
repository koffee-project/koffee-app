package eu.yeger.koffee.ui.item

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.getUserIdFromSharedPreferencesIfNull

class ItemFragment : Fragment() {

    private val itemViewModel: ItemViewModel by viewModels {
        val itemFragmentArgs = ItemFragmentArgs.fromBundle(requireArguments())
        val userId = requireContext().getUserIdFromSharedPreferencesIfNull(itemFragmentArgs.userId)
        val itemId = itemFragmentArgs.itemId

        val context = requireContext()
        val itemRepository = ItemRepository(context)
        val userRepository = UserRepository(context)
        val transactionRepository = TransactionRepository(context)

        ItemViewModel.Factory(
            itemId = itemId,
            userId = userId,
            itemRepository = itemRepository,
            userRepository = userRepository,
            transactionRepository = transactionRepository
        )
    }
}
