package eu.yeger.koffee.ui.user.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.TransactionListAdapter
import eu.yeger.koffee.ui.onError
import eu.yeger.koffee.utility.showDeleteDialog
import eu.yeger.koffee.utility.showSnackbar
import eu.yeger.koffee.utility.viewModelFactories

class UserDetailsFragment : Fragment() {

    private val userDetailsViewModel: UserDetailsViewModel by viewModelFactories {
        val context = requireContext()
        val userId = UserDetailsFragmentArgs.fromBundle(requireArguments()).userId
        UserDetailsViewModel(
            isActiveUser = false,
            userId = userId,
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
        userDetailsViewModel.apply {
            editUserAction.observe(viewLifecycleOwner, Observer { itemId ->
                itemId?.let {
                    // TODO
                    // val action = UserDetailsFragmentDirections.toUserEditing()
                    // findNavController().navigate(action)
                    onEditUserActionHandled()
                }
            })

            deleteUserAction.observe(viewLifecycleOwner, Observer { userId ->
                userId?.let {
                    showDeleteDialog(userId) {
                        userDetailsViewModel.deleteUser()
                    }
                    onDeleteUserActionHandled()
                }
            })

            userDeletedAction.observe(viewLifecycleOwner, Observer { itemDeleted ->
                if (itemDeleted) {
                    findNavController().navigateUp()
                    onUserDeletedActionHandled()
                }
            })

            onError(this@UserDetailsFragment) { error ->
                requireActivity().showSnackbar(error)
            }
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            viewModel = userDetailsViewModel
            transactionRecyclerView.adapter = TransactionListAdapter(OnClickListener { /*ingore*/ })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
