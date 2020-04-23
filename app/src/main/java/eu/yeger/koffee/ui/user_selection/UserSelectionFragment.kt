package eu.yeger.koffee.ui.user_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        binding = FragmentUserSelectionBinding.inflate(inflater)
        binding.viewModel = userSelectionViewModel
        binding.searchResultRecyclerView.adapter = UserEntryListAdapter()
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
