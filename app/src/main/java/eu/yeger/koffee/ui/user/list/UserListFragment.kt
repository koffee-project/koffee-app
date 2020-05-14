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
import eu.yeger.koffee.domain.UserEntry
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserEntryRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.UserEntryListAdapter
import eu.yeger.koffee.utility.*

class UserListFragment : Fragment() {

    private val userListViewModel: UserListViewModel by viewModelFactories {
        val context = requireContext()
        UserListViewModel(
            adminRepository = AdminRepository(context),
            userEntryRepository = UserEntryRepository(context)
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

            observeAction(userEntrySelectedAction) { pair ->
                val (isAuthenticated, userEntry) = pair
                val canView =
                    isAuthenticated && requireContext().getUserIdFromSharedPreferences() != userEntry.id
                showUserSelectionDialog(userEntry, canView)
            }

            onErrorShowSnackbar { error ->
                getString(R.string.user_refresh_error_format, error.localizedMessage)
            }
        }

        return FragmentUserListBinding.inflate(inflater).apply {
            viewModel = userListViewModel
            searchResultRecyclerView.adapter =
                UserEntryListAdapter(OnClickListener { selectedUserEntry ->
                    userListViewModel.triggerUserEntrySelectedAction(selectedUserEntry)
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun showUserSelectionDialog(userEntry: UserEntry, canView: Boolean) {
        val message = getString(R.string.set_active_user_format, userEntry.name, userEntry.id)
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.set_as_active_user) { _, _ ->
                requireContext().saveUserIdToSharedPreferences(userId = userEntry.id)
                val direction = UserListFragmentDirections.toHome()
                findNavController().navigate(direction)
            }.apply {
                if (canView)
                    setNeutralButton(R.string.view_user_as_admin) { _, _ ->
                        val direction = UserListFragmentDirections.toUserDetails(userEntry.id)
                        findNavController().navigate(direction)
                    }
            }
            .create()
            .show()
    }
}
