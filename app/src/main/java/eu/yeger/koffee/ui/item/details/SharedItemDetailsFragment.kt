package eu.yeger.koffee.ui.item.details

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.viewModelFactories

class SharedItemDetailsFragment : ItemDetailsFragment() {

    override val itemId: String by lazy {
        SharedItemDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).itemId
    }

    override val userId: String by lazy {
        SharedItemDetailsFragmentArgs.fromBundle(
            requireArguments()
        ).userId
    }

    override val itemDetailsViewModel: ItemDetailsViewModel by viewModelFactories {
        val context = requireContext()
        SharedItemDetailsViewModel(
            itemId = itemId,
            userId = userId,
            itemRepository = ItemRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun initializeViewModel() = Unit

    override fun onNotFoundConfirmed() {
        findNavController().navigateUp()
    }
}
