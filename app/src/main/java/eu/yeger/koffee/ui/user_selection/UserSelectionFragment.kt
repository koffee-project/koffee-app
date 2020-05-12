package eu.yeger.koffee.ui.user_selection

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
import eu.yeger.koffee.databinding.FragmentUserSelectionBinding
import eu.yeger.koffee.domain.UserEntry
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserEntryRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.UserEntryListAdapter
import eu.yeger.koffee.utility.saveUserIdToSharedPreferences
import eu.yeger.koffee.utility.showRefreshResultSnackbar

class UserSelectionFragment : Fragment() {

    private val userSelectionViewModel: UserSelectionViewModel by viewModels {
        val context = requireContext()
        UserSelectionViewModel.Factory(
            adminRepository = AdminRepository(context),
            userEntryRepository = UserEntryRepository(context)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        userSelectionViewModel.apply {
            refreshResultAction.observe(viewLifecycleOwner, Observer { state ->
                state?.let {
                    requireActivity().showRefreshResultSnackbar(
                        repositoryState = state,
                        successText = R.string.user_refresh_success,
                        errorTextFormat = R.string.user_refresh_error_format
                    )
                    onRefreshResultActionHandled()
                }
            })

            createUserAction.observe(viewLifecycleOwner, Observer { createUser ->
                if (createUser) {
                    val action = UserSelectionFragmentDirections.toUserCreation()
                    findNavController().navigate(action)
                    onCreateUserActionHandled()
                }
            })
        }

        return FragmentUserSelectionBinding.inflate(inflater).apply {
            viewModel = userSelectionViewModel
            searchResultRecyclerView.adapter =
                UserEntryListAdapter(OnClickListener { selectedUserEntry ->
                    showUserSelectionDialog(selectedUserEntry)
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    private fun showUserSelectionDialog(userEntry: UserEntry) {
        val message =
            getString(R.string.set_active_user_format).format(userEntry.name, userEntry.id)
        AlertDialog.Builder(requireContext())
            .setMessage(message)
            .setPositiveButton(R.string.set_as_active_user) { _, _ ->
                setActiveUser(userEntry)
            }
            .setNegativeButton(R.string.cancel) { _, _ -> /*ignore*/ }
            .create()
            .show()
    }

    private fun setActiveUser(userEntry: UserEntry) {
        requireContext().saveUserIdToSharedPreferences(userId = userEntry.id)
        val action = UserSelectionFragmentDirections.toHome()
        action.userId = userEntry.id
        findNavController().navigate(action)
    }
}
