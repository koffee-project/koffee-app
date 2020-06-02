package eu.yeger.koffee.ui.alternative.item.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.databinding.FragmentItemDetailsBinding
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.ui.item.details.ItemDetailsFragment
import eu.yeger.koffee.ui.item.details.ItemDetailsViewModel
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class AlternativeItemDetailsFragment : ItemDetailsFragment() {

    override val itemId: String by lazy {
        AlternativeItemDetailsFragmentArgs.fromBundle(requireArguments()).itemId
    }

    override val userId: String by lazy {
        AlternativeItemDetailsFragmentArgs.fromBundle(requireArguments()).userId
    }

    override val itemDetailsViewModel: ItemDetailsViewModel by viewModelFactories {
        val context = requireContext()
        AlternativeItemDetailsViewModel(
            itemId = itemId,
            userId = userId,
            itemRepository = ItemRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemDetailsViewModel.apply {
            observeAction(itemNotFoundAction) {
                showItemNotFoundDialog()
            }

            onErrorShowSnackbar()
        }

        return FragmentItemDetailsBinding.inflate(inflater).apply {
            itemDetailsViewModel = this@AlternativeItemDetailsFragment.itemDetailsViewModel
            refundViewModel = this@AlternativeItemDetailsFragment.refundViewModel
            transactionRecyclerView.adapter = transactionListAdapter()
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onNotFoundConfirmed() {
        findNavController().navigateUp()
    }
}
