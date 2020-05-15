package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.yeger.koffee.databinding.CardItemBinding
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.ui.OnClickListener

class ItemListAdapter(private val onClickListener: OnClickListener<Item>) :
    ListAdapter<Item, ItemListAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: CardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds an [Item] to the CardItemBinding.
         *
         * @param item The [Item] that will be bound to the CardItemBinding.
         */
        fun bind(item: Item) {
            binding.item = item
            binding.onClickListener = onClickListener
            binding.executePendingBindings()
        }
    }

    /**
     * [DiffUtil.ItemCallback](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil.ItemCallback) for [Item]s.
     *
     * @author Jan Müller
     */
    companion object DiffCallback : DiffUtil.ItemCallback<Item>() {

        /**
         * Checks if two [Item]s have the same id.
         *
         * @param oldItem The old [Item].
         * @param newItem The new [Item].
         * @return true if both [Item]s have the same id or false otherwise.
         */
        override fun areItemsTheSame(oldItem: Item, newItem: Item) = oldItem.id == newItem.id

        /**
         * Checks if two [Item]s are equal.
         *
         * @param oldItem The old [Item].
         * @param newItem The new [Item].
         * @return true if both [Item]s are equal or false otherwise.
         */
        override fun areContentsTheSame(oldItem: Item, newItem: Item) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item: Item = getItem(position)
        holder.bind(item)
    }
}
