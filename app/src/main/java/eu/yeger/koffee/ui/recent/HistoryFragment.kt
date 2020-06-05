package eu.yeger.koffee.ui.recent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.yeger.koffee.databinding.FragmentHistoryBinding
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences
import eu.yeger.koffee.utility.viewModelFactories

class HistoryFragment : Fragment() {

    private val userId by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

    // TODO change
    private val historyViewModel: HistoryViewModel by viewModelFactories {
        HistoryViewModel(userId!!, TransactionRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        historyViewModel.apply {
            onErrorShowSnackbar()
        }

        return FragmentHistoryBinding.inflate(inflater).apply {
            historyViewModel = this@HistoryFragment.historyViewModel
            transactionRecyclerView.adapter = transactionListAdapter()
            lifecycleOwner = viewLifecycleOwner
        }.root
    }
}
