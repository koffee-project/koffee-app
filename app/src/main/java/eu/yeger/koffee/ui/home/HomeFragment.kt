package eu.yeger.koffee.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import eu.yeger.koffee.databinding.FragmentHomeBinding
import eu.yeger.koffee.repository.UserEntryRepository
import eu.yeger.koffee.utility.SharedPreferencesKeys
import eu.yeger.koffee.utility.sharedPreferences

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels {
        val userId =
            when (val argumentUserId = HomeFragmentArgs.fromBundle(requireArguments()).userId) {
                null -> requireContext().sharedPreferences.getString(
                    SharedPreferencesKeys.activeUserId,
                    null
                ) // use active userid if no explicit id was passed
                else -> argumentUserId // use argument id otherwise
            }
        val userEntryRepository = UserEntryRepository(requireContext())

        HomeViewModel.Factory(
            userId = userId,
            userEntryRepository = userEntryRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.viewModel = homeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
