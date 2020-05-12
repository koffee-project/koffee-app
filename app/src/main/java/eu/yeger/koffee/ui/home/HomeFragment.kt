package eu.yeger.koffee.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentHomeBinding
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.TransactionListAdapter
import eu.yeger.koffee.utility.getUserIdFromSharedPreferencesIfNull

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels {
        val argumentUserId = HomeFragmentArgs.fromBundle(requireArguments()).userId
        val userId = requireContext().getUserIdFromSharedPreferencesIfNull(argumentUserId)

        val context = requireContext()

        HomeViewModel.Factory(
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
            userSelectionRequiredAction.observe(viewLifecycleOwner, Observer { userSelectionRequired ->
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
                    // TODO
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun showUserSelectionRequiredDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.no_user_selected)
            .setPositiveButton(R.string.got_to_selection) { _, _ ->
                val action = HomeFragmentDirections.toUserSelection()
                findNavController().navigate(action)
            }
            .setCancelable(false)
            .create()
            .show()
    }
}
