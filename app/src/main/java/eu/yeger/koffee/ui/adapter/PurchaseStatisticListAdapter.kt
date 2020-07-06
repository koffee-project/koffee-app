package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.yeger.koffee.databinding.CardPurchaseStatisticBinding
import eu.yeger.koffee.domain.PurchaseStatistic
import eu.yeger.koffee.ui.OnClickListener
import eu.yeger.koffee.utility.formatTimestamp

/**
 * Simple list adapter for [PurchaseStatistic]s.
 *
 * @property onClickListener [OnClickListener] for [PurchaseStatistic]s.
 *
 * @author Jan Müller
 */
class PurchaseStatisticListAdapter(
    private val onClickListener: OnClickListener<PurchaseStatistic>
) : ListAdapter<PurchaseStatistic, PurchaseStatisticListAdapter.ViewHolder>(
    itemCallback(
        isSame = { old, new -> old.itemId == new.itemId },
        isIdentical = { old, new -> old == new }
    )
) {

    inner class ViewHolder(private val binding: CardPurchaseStatisticBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(purchaseStatistic: PurchaseStatistic) {
            binding.apply {
                this.purchaseStatistic = purchaseStatistic
                this.lastPurchaseTimestamp = formatTimestamp(purchaseStatistic.lastPurchaseTimestamp)
                this.onClickListener = this@PurchaseStatisticListAdapter.onClickListener
                executePendingBindings()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardPurchaseStatisticBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }
}
