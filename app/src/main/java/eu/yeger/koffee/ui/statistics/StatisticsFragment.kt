package eu.yeger.koffee.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import eu.yeger.koffee.databinding.FragmentStatisticsBinding
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.PurchaseStatisticListAdapter
import eu.yeger.koffee.utility.getUserIdFromSharedPreferences
import eu.yeger.koffee.utility.viewModelFactories

class StatisticsFragment : Fragment() {

    private val userId by lazy {
        requireContext().getUserIdFromSharedPreferences()
    }

    private val statisticsViewModel: StatisticsViewModel by viewModelFactories {
        StatisticsViewModel(userId!!, TransactionRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        statisticsViewModel.apply {
            onErrorShowSnackbar()
        }

        return FragmentStatisticsBinding.inflate(inflater).apply {
            statisticsViewModel = this@StatisticsFragment.statisticsViewModel
            statisticsRecyclerView.adapter = PurchaseStatisticListAdapter(OnClickListener { statistic ->
                val direction = StatisticsFragmentDirections.toItemDetails(statistic.itemId)
                findNavController().navigate(direction)
            })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        statisticsViewModel.refresh()
        super.onResume()
    }
}
