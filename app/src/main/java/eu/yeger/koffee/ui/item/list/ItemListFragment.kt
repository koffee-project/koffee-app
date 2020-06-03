package eu.yeger.koffee.ui.item.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.yeger.koffee.databinding.FragmentItemListBinding
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.itemListAdapter

abstract class ItemListFragment : Fragment() {

    protected abstract val itemListViewModel: ItemListViewModel

    protected abstract fun initializeViewModel()

    protected abstract fun onItemSelected(itemId: String)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        itemListViewModel.apply {
            initializeViewModel()
            onErrorShowSnackbar()
        }

        return FragmentItemListBinding.inflate(inflater).apply {
            viewModel = itemListViewModel
            itemRecyclerView.adapter = itemListAdapter(OnClickListener { selectedItem ->
                onItemSelected(selectedItem.id)
            })
            lifecycleOwner = viewLifecycleOwner
        }.root
    }

    override fun onResume() {
        itemListViewModel.refreshItems()
        super.onResume()
    }
}
