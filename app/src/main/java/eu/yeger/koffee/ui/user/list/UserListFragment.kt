package eu.yeger.koffee.ui.user.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserListBinding
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.UserListAdapter
import eu.yeger.koffee.utility.*

class UserListFragment : Fragment() {

    private val userListViewModel: UserListViewModel by viewModelFactories {
        val context = requireContext()
        UserListViewModel(
            adminRepository = AdminRepository(context),
            userRepository = UserRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userListViewModel.apply {
            observeAction(createUserAction) {
                val direction = UserListFragmentDirections.toUserCreation()
                findNavController().navigate(direction)
            }

            observeAction(userSelectedAction) { pair ->
                val (isAuthenticated, user) = pair
                val canView =
                    isAuthenticated && requireContext().getUserIdFromSharedPreferences() != user.id
                showUserSelectionDialog(user, canView)
            }

            onErrorShowSnackbar()
        }

        return FragmentUserListBinding.inflate(inflater).apply {
            viewModel = userListViewModel
            searchResultRecyclerView.adapter =
                UserListAdapter(OnClickListener { selectedUser ->
                    userListViewModel.triggerUserSelectedAction(selectedUser)
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun showUserSelectionDialog(user: User, canView: Boolean) {
        val message = getString(R.string.set_active_user_format, user.name, user.id)
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.set_as_active_user) { _, _ ->
                requireContext().saveUserIdToSharedPreferences(userId = user.id)
                val direction = UserListFragmentDirections.toHome()
                findNavController().navigate(direction)
            }.apply {
                if (canView)
                    setNeutralButton(R.string.view_user_as_admin) { _, _ ->
                        val direction = UserListFragmentDirections.toUserDetails(user.id)
                        findNavController().navigate(direction)
                    }
            }
            .create()
            .show()
    }
}
