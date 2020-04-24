package eu.yeger.koffee.ui.user_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import eu.yeger.koffee.R
import eu.yeger.koffee.databinding.FragmentUserSelectionBinding
import eu.yeger.koffee.repository.UserEntryRepository

class UserSelectionFragment : Fragment() {

    private val userSelectionViewModel: UserSelectionViewModel by viewModels {
        val userEntryRepository = UserEntryRepository(requireContext())
        UserSelectionViewModel.Factory(userEntryRepository)
    }

    private lateinit var binding: FragmentUserSelectionBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        userSelectionViewModel.refreshResultAction.observe(viewLifecycleOwner, Observer { state ->
            state?.let {
                showRefreshResultSnackbar(state)
                userSelectionViewModel.onRefreshResultActionHandled()
            }
        })

        binding = FragmentUserSelectionBinding.inflate(inflater)
        binding.viewModel = userSelectionViewModel
        binding.searchResultRecyclerView.adapter = UserEntryListAdapter()
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    private fun showRefreshResultSnackbar(state: UserEntryRepository.State) {
        val message = when (state) {
            is UserEntryRepository.State.Done -> getString(R.string.user_refresh_success)
            is UserEntryRepository.State.Error -> getString(R.string.user_refresh_error_format).format(state.exception.message ?: "Unknown")
            else -> return // impossible
        }
        Snackbar.make(
            requireView(),
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }
}
