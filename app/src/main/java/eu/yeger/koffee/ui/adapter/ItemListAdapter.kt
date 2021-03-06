package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import eu.yeger.koffee.databinding.CardItemBinding
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.ui.OnClickListener

/**
 * [GenericPagedListAdapter.ViewHolder] for [Item]s.
 *
 * @property binding The view binding used by this [GenericPagedListAdapter.ViewHolder].
 *
 * @author Jan Müller
 */
class ItemViewHolder(
    private val binding: CardItemBinding
) : GenericPagedListAdapter.ViewHolder<Item>(binding.root) {

    companion object Factory : GenericPagedListAdapter.ViewHolderFactory<Item> {
        override fun createViewHolder(parent: ViewGroup): GenericPagedListAdapter.ViewHolder<Item> {
            return ItemViewHolder(
                CardItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun bind(item: Item, onClickListener: OnClickListener<Item>) {
        binding.apply {
            this.item = item
            this.onClickListener = onClickListener
        }.executePendingBindings()
    }
}

/**
 * Utility function for creating a [GenericPagedListAdapter] for [Item]s.
 *
 * @param onClickListener [OnClickListener] for [Item]s.
 * @return The created [GenericPagedListAdapter].
 *
 * @author Jan Müller
 */
fun itemListAdapter(onClickListener: OnClickListener<Item>): GenericPagedListAdapter<Item> {
    return GenericPagedListAdapter(
        onClickListener,
        ItemViewHolder.Factory,
        itemCallback(
            isSame = { old, new -> old.id == new.id },
            isIdentical = { old, new -> old == new }
        )
    )
}
