package eu.yeger.koffee.ui.user.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserDetailsBinding
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.TransactionListAdapter
import eu.yeger.koffee.utility.*

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
            observeAction(editUserAction) { userId ->
                val direction = UserDetailsFragmentDirections.toUserEditing(userId)
                findNavController().navigate(direction)
            }

            observeAction(deleteUserAction) { userId ->
                showDeleteDialog(userId) {
                    userDetailsViewModel.deleteUser()
                }
            }

            observeAction(userDeletedAction) {
                requireActivity().showSnackbar(getString(R.string.user_deletion_success))
                val direction = UserDetailsFragmentDirections.toUserList()
                findNavController().navigate(direction)
            }

            onErrorShowSnackbar()
        }

        return FragmentUserDetailsBinding.inflate(inflater).apply {
            viewModel = userDetailsViewModel
            transactionRecyclerView.adapter = TransactionListAdapter(OnClickListener { /*ingore*/ })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
