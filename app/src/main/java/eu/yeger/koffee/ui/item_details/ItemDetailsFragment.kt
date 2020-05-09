package eu.yeger.koffee.ui.item_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import eu.yeger.koffee.databinding.FragmentItemDetailsBinding
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.TransactionListAdapter
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences

class ItemDetailsFragment : Fragment() {

    private val itemDetailsViewModel: ItemDetailsViewModel by viewModels {
        val context = requireContext()

        ItemDetailsViewModel.Factory(
            itemId = ItemDetailsFragmentArgs.fromBundle(requireArguments()).itemId,
            userId = context.getUserIdFromSharedPreferences(),
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
        return FragmentItemDetailsBinding.inflate(inflater).apply {
            viewModel = itemDetailsViewModel
            transactionRecyclerView.adapter =
                TransactionListAdapter(OnClickListener { selectedTransaction ->
                    // TODO
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
