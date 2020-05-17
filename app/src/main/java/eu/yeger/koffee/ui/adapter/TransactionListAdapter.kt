package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eu.yeger.koffee.databinding.CardPurchaseTransactionBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.ui.OnClickListener
import java.util.*
import org.ocpsoft.prettytime.PrettyTime
import timber.log.Timber

class TransactionListAdapter(
    private val onClickListener: OnClickListener<Transaction>
) : PagedListAdapter<Transaction, TransactionListAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private val binding: CardPurchaseTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        private fun formatTimestamp(timestamp: Long): String {
            val prettyTime = PrettyTime(Locale.getDefault())
            return prettyTime.format(Date(timestamp - 1000)) // subtract one second to prevent edge case issues
        }

        fun bind(transaction: Transaction, onClickListener: OnClickListener<Transaction>) {
            binding.apply {
                timestamp = formatTimestamp(transaction.timestamp)
                this.transaction = transaction
                this.onClickListener = onClickListener
            }.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Transaction>() {

        /**
         * Checks if two [Transaction]s have the same id.
         *
         * @param oldItem The old [Transaction].
         * @param newItem The new [Transaction].
         * @return true if both [Transaction]s have the same id or false otherwise.
         */
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem == newItem

        /**
         * Checks if two [Transaction]s are equal.
         *
         * @param oldItem The old [Transaction].
         * @param newItem The new [Transaction].
         * @return true if both [Transaction]s are equal or false otherwise.
         */
        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction) =
            oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
                CardPurchaseTransactionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (val transaction = getItem(position)) {
            null -> Timber.d("Received null item")
            else -> holder.bind(transaction, onClickListener)
        }
    }
}
