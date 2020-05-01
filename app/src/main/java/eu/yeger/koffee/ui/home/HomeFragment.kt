package eu.yeger.koffee.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        val userRepository = UserRepository(context)
        val transactionRepository = TransactionRepository(context)

        HomeViewModel.Factory(
            userId = userId,
            userRepository = userRepository,
            transactionRepository = transactionRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.viewModel = homeViewModel
        binding.transactionRecyclerView.adapter =
            TransactionListAdapter(OnClickListener { selectedTransaction ->
                // TODO
            })
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }
}
