package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.yeger.koffee.databinding.CardFundingTransactionBinding
import eu.yeger.koffee.databinding.CardPurchaseTransactionBinding
import eu.yeger.koffee.databinding.CardRefundTransactionBinding
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.ui.OnClickListener

private const val FUNDING_VIEW_TYPE = 0
private const val PURCHASE_VIEW_TYPE = 1
private const val REFUND_VIEW_TYPE = 2

class TransactionListAdapter(private val onClickListener: OnClickListener<Transaction>) :
    ListAdapter<Transaction, TransactionListAdapter.ViewHolder>(DiffCallback) {

    sealed class ViewHolder(view: View) :
        RecyclerView.ViewHolder(view) {

        abstract fun bind(transaction: Transaction, onClickListener: OnClickListener<Transaction>)

        class FundingViewHolder(private val binding: CardFundingTransactionBinding) :
            ViewHolder(binding.root) {

            override fun bind(transaction: Transaction, onClickListener: OnClickListener<Transaction>) {
                binding.funding = transaction as Transaction.Funding
                binding.onClickListener = onClickListener
                binding.executePendingBindings()
            }
        }

        class PurchaseViewHolder(private val binding: CardPurchaseTransactionBinding) :
            ViewHolder(binding.root) {

            override fun bind(transaction: Transaction, onClickListener: OnClickListener<Transaction>) {
                binding.purchase = transaction as Transaction.Purchase
                binding.onClickListener = onClickListener
                binding.executePendingBindings()
            }
        }

        class RefundViewHolder(private val binding: CardRefundTransactionBinding) :
            ViewHolder(binding.root) {

            override fun bind(transaction: Transaction, onClickListener: OnClickListener<Transaction>) {
                binding.refund = transaction as Transaction.Refund
                binding.onClickListener = onClickListener
                binding.executePendingBindings()
            }
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

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Transaction.Funding -> FUNDING_VIEW_TYPE
            is Transaction.Purchase -> PURCHASE_VIEW_TYPE
            is Transaction.Refund -> REFUND_VIEW_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            FUNDING_VIEW_TYPE -> ViewHolder.FundingViewHolder(
                CardFundingTransactionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            PURCHASE_VIEW_TYPE -> ViewHolder.PurchaseViewHolder(
                CardPurchaseTransactionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            REFUND_VIEW_TYPE -> ViewHolder.RefundViewHolder(
                CardRefundTransactionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException("Impossible viewType")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction: Transaction = getItem(position)
        holder.bind(transaction, onClickListener)
    }
}
