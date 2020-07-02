package eu.yeger.koffee.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.databinding.FragmentHistoryBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.transactionListAdapter
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences
import eu.yeger.koffee.utility.viewModelFactories

class HistoryFragment : Fragment() {

    private val userId by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

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
            transactionRecyclerView.adapter =
                transactionListAdapter(OnClickListener { transaction ->
                    val direction = when (transaction) {
                        is Transaction.Purchase -> HistoryFragmentDirections.toItemDetails(transaction.itemId)
                        is Transaction.Refund -> HistoryFragmentDirections.toItemDetails(transaction.itemId)
                        is Transaction.Funding -> HistoryFragmentDirections.toHome()
                    }
                    findNavController().navigate(direction)
                })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        historyViewModel.refresh()
        super.onResume()
    }
}
