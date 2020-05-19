package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import eu.yeger.koffee.databinding.CardPurchaseTransactionBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.ui.OnClickListener
import java.util.*
import org.ocpsoft.prettytime.PrettyTime

class TransactionViewHolder(
    private val binding: CardPurchaseTransactionBinding
) : GenericPagedListAdapter.ViewHolder<Transaction>(binding.root) {

    companion object Factory : GenericPagedListAdapter.ViewHolderFactory<Transaction> {

        override fun createViewHolder(parent: ViewGroup): GenericPagedListAdapter.ViewHolder<Transaction> {
            return TransactionViewHolder(
                CardPurchaseTransactionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        val prettyTime = PrettyTime(Locale.getDefault())
        return prettyTime.format(Date(timestamp - 1000)) // subtract one second to prevent edge case issues
    }

    override fun bind(item: Transaction, onClickListener: OnClickListener<Transaction>) {
        binding.apply {
            timestamp = formatTimestamp(item.timestamp)
            this.transaction = item
            this.onClickListener = onClickListener
        }.executePendingBindings()
    }
}

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
