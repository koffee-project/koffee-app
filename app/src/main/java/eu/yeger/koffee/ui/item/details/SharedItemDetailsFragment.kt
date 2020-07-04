package eu.yeger.koffee.ui.item.details

import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.utility.viewModelFactories

/**
 * [ItemDetailsFragment] for the multi user mode.
 *
 * @property itemId The id of the item from the arguments.
 * @property userId The id of the user from the arguments.
 * @property itemDetailsViewModel The [SharedItemDetailsViewModel] used for accessing item information.
 *
 * @author Jan Müller
 */
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

    override val itemDetailsViewModel: SharedItemDetailsViewModel by viewModelFactories {
        val context = requireContext()
        SharedItemDetailsViewModel(
            itemId = itemId,
            userId = userId,
            itemRepository = ItemRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    /**
     * No additional initialization.
     */
    override fun initializeViewModel() = Unit

    /**
     * Navigates up.
     */
    override fun onNotFoundConfirmed() {
        findNavController().navigateUp()
    }
}
