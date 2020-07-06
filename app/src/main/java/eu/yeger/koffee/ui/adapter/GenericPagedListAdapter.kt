package eu.yeger.koffee.ui.adapter

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.ui.adapter.GenericPagedListAdapter.ViewHolder
import eu.yeger.koffee.ui.adapter.GenericPagedListAdapter.ViewHolderFactory

/**
 * Generic ListAdapter for PagesLists.
 *
 * @param T The type of the elements.
 * @property onClickListener [OnClickListener] for individual elements.
 * @property viewHolderFactory [ViewHolderFactory] for [ViewHolder]s.
 * @constructor
 * Initializes the list adapter.
 *
 * @param diffCallback The diff callback used by the superclass.
 *
 * @author Jan Müller
 */
class GenericPagedListAdapter<T>(
    private val onClickListener: OnClickListener<T>,
    private val viewHolderFactory: ViewHolderFactory<T>,
    diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, ViewHolder<T>>(diffCallback) {

    interface ViewHolderFactory<T> {
        fun createViewHolder(parent: ViewGroup): ViewHolder<T>
    }

    abstract class ViewHolder<T>(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun bind(item: T, onClickListener: OnClickListener<T>)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return viewHolderFactory.createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, position: Int) {
        when (val item = getItem(position)) {
            null -> Unit
            else -> holder.bind(item, onClickListener)
        }
    }
}
