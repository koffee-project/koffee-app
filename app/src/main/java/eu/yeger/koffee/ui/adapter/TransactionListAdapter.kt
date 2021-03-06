package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import eu.yeger.koffee.databinding.CardTransactionBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.utility.formatTimestamp

/**
 * [GenericPagedListAdapter.ViewHolder] for [Transaction]s.
 *
 * @property binding The view binding used by this [GenericPagedListAdapter.ViewHolder].
 *
 * @author Jan Müller
 */
class TransactionViewHolder(
    private val binding: CardTransactionBinding
) : GenericPagedListAdapter.ViewHolder<Transaction>(binding.root) {

    companion object Factory : GenericPagedListAdapter.ViewHolderFactory<Transaction> {

        override fun createViewHolder(parent: ViewGroup): GenericPagedListAdapter.ViewHolder<Transaction> {
            return TransactionViewHolder(
                CardTransactionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun bind(item: Transaction, onClickListener: OnClickListener<Transaction>) {
        binding.apply {
            timestamp = formatTimestamp(item.timestamp)
            this.transaction = item
            this.onClickListener = onClickListener
        }.executePendingBindings()
    }
}

/**
 * Utility function for creating a [GenericPagedListAdapter] for [Transaction]s.
 *
 * @param onClickListener [OnClickListener] for [Transaction]s.
 * @return The created [GenericPagedListAdapter].
 *
 * @author Jan Müller
 */
fun transactionListAdapter(
    onClickListener: OnClickListener<Transaction> = OnClickListener { }
): GenericPagedListAdapter<Transaction> {
    return GenericPagedListAdapter(
        onClickListener,
        TransactionViewHolder.Factory,
        itemCallback(
            isSame = { old, new -> old == new },
            isIdentical = { old, new -> old == new }
        )
    )
}
