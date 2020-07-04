package eu.yeger.koffee.ui.item.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import eu.yeger.koffee.databinding.FragmentItemListBinding
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.itemListAdapter

/**
 * Abstract [Fragment](https://developer.android.com/jetpack/androidx/releases/fragment) for the item list screen.
 * Supports searching.
 *
 * @property itemListViewModel The [ItemListViewModel] used for accessing the item list.
 *
 * @author Jan Müller
 */
abstract class ItemListFragment : Fragment() {

    protected abstract val itemListViewModel: ItemListViewModel

    /**
     * Called when the [ItemListViewModel] is initialized.
     */
    protected abstract fun initializeViewModel()

    /**
     * Called when an item has been selected.
     *
     * @param itemId The id of the selected item.
     */
    protected abstract fun onItemSelected(itemId: String)

    /**
     * Inflates and initializes the layout.
     *
     * @param inflater Used for layout inflation.
     * @param container Unused.
     * @param savedInstanceState Unused.
     * @return The item list view.
     */
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

    /**
     * Refreshes the [ItemListViewModel].
     */
    override fun onResume() {
        itemListViewModel.refreshItems()
        super.onResume()
    }
}
