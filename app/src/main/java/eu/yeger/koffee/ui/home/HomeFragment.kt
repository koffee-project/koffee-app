package eu.yeger.koffee.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.TransactionListAdapter
import eu.yeger.koffee.ui.user.details.UserDetailsViewModel
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences
import eu.yeger.koffee.utility.observeAction
import eu.yeger.koffee.utility.viewModelFactories

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModelFactories {
        val context = requireContext()
        HomeViewModel(
            userId = context.getUserIdFromSharedPreferences(),
            userRepository = UserRepository(context)
        )
    }

    private val userDetailsViewModel: UserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        UserDetailsViewModel(
            isActiveUser = true,
            userId = context.getUserIdFromSharedPreferences(),
            adminRepository = AdminRepository(context),
            transactionRepository = TransactionRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel.apply {
            observeAction(userSelectionRequiredAction) {
                showUserSelectionRequiredDialog()
            }
        }

        userDetailsViewModel.apply {
            observeAction(editUserAction) { userId ->
                val direction = HomeFragmentDirections.toUserEditing(userId)
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            viewModel = userDetailsViewModel
            transactionRecyclerView.adapter =
                TransactionListAdapter(OnClickListener { selectedTransaction ->
                    when (selectedTransaction) {
                        is Transaction.Purchase -> selectedTransaction.itemId
                        is Transaction.Refund -> selectedTransaction.itemId
                        else -> null
                    }?.let { itemId ->
                        val direction = HomeFragmentDirections.toItemDetails(itemId)
                        findNavController().navigate(direction)
                    }
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun showUserSelectionRequiredDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.no_user_selected)
            .setPositiveButton(R.string.got_to_selection) { _, _ ->
                val direction = HomeFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
