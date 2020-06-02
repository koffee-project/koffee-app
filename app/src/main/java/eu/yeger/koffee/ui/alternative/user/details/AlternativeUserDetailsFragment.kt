package eu.yeger.koffee.ui.alternative.user.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.ui.user.details.BaseUserDetailsFragment
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class AlternativeUserDetailsFragment : BaseUserDetailsFragment() {

    private val userId by lazy {
        AlternativeUserDetailsFragmentArgs.fromBundle(requireArguments()).userId
    }

    override val userDetailsViewModel: AlternativeUserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        AlternativeUserDetailsViewModel(
            userId = userId,
            profileImageRepository = ProfileImageRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userDetailsViewModel.apply {
            observeAction(userNotFoundAction) {
                showUserNotFoundDialog()
            }

            onErrorShowSnackbar()
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            userDetailsViewModel = this@AlternativeUserDetailsFragment.userDetailsViewModel
            transactionRecyclerView.adapter =
                transactionListAdapter(OnClickListener { selectedTransaction ->
                    when (selectedTransaction) {
                        is Transaction.Purchase -> selectedTransaction.itemId
                        is Transaction.Refund -> selectedTransaction.itemId
                        else -> null
                    }?.let { itemId ->
                        val direction =
                            AlternativeUserDetailsFragmentDirections.toAltItemDetails(itemId)
                        findNavController().navigate(direction)
                    }
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
