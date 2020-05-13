package eu.yeger.koffee.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentHomeBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.TransactionListAdapter
import eu.yeger.koffee.utility.getUserIdFromSharedPreferencesIfNull
import eu.yeger.koffee.utility.viewModelFactories

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModelFactories {
        val argumentUserId = HomeFragmentArgs.fromBundle(requireArguments()).userId
        val userId = requireContext().getUserIdFromSharedPreferencesIfNull(argumentUserId)

        val context = requireContext()

        HomeViewModel(
            userId = userId,
            userRepository = UserRepository(context),
            transactionRepository = TransactionRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel.apply {
            userSelectionRequiredAction.observe(
                viewLifecycleOwner,
                Observer { userSelectionRequired ->
                    if (userSelectionRequired) {
                        showUserSelectionRequiredDialog()
                        onUserSelectionRequiredActionHandled()
                    }
                })
        }

        return FragmentHomeBinding.inflate(inflater).apply {
            viewModel = homeViewModel
            transactionRecyclerView.adapter =
                TransactionListAdapter(OnClickListener { selectedTransaction ->
                    when (selectedTransaction) {
                        is Transaction.Purchase -> selectedTransaction.itemId
                        is Transaction.Refund -> selectedTransaction.itemId
                        else -> null
                    }?.let { itemId ->
                        val action = HomeFragmentDirections.toItemDetails(itemId)
                        findNavController().navigate(action)
                    }
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun showUserSelectionRequiredDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.no_user_selected)
            .setPositiveButton(R.string.got_to_selection) { _, _ ->
                val action = HomeFragmentDirections.toUserList()
                findNavController().navigate(action)
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
