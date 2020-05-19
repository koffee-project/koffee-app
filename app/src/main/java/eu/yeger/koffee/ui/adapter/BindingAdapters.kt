package eu.yeger.koffee.ui.adapter

import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.imageview.ShapeableImageView
import eu.yeger.koffee.R
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.Transaction

/**
 * Sets the visibility of a [View](https://developer.android.com/reference/android/view/View).
 *
 * @receiver The target view.
 * @param visible true for View.VISIBLE and false for View.GONE.
 *
 * @author Jan Müller
 */
@BindingAdapter("visible")
fun View.bindVisibility(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.GONE
}

/**
 * Sets the refresh listener of a [SwipeRefreshLayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout).
 *
 * @receiver The target [SwipeRefreshLayout](https://developer.android.com/jetpack/androidx/releases/swiperefreshlayout).
 * @param listener The [Runnable] to be run inside the listener.
 *
 * @author Jan Müller
 */
@BindingAdapter("onRefresh")
fun SwipeRefreshLayout.bindRefreshListener(listener: Runnable) {
    setOnRefreshListener {
        listener.run()
    }
}

/**
 * Submits an [Item] [PagedList] to the [GenericPagedListAdapter] of a [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 *
 * @receiver The target [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 * @param T Type of the items.
 * @param items The [List] of items that will be submitted.
 * @param callback Callback, which is executed after the list has been submitted. Defaults to empty [Runnable].
 *
 * @author Jan Müller
 */
@BindingAdapter(value = ["items", "callback"], requireAll = false)
fun <T> RecyclerView.bindItems(items: PagedList<T>?, callback: Runnable? = Runnable { }) {
    @Suppress("UNCHECKED_CAST")
    val adapter = adapter as GenericPagedListAdapter<T>
    adapter.submitList(items, callback)
    smoothScrollToPosition(0)
}

@BindingAdapter("transaction")
fun ShapeableImageView.bindTransaction(transaction: Transaction?) {
    transaction?.let {
        val resourceId = when (transaction) {
            is Transaction.Funding -> R.drawable.ic_attach_money_24dp
            is Transaction.Purchase -> R.drawable.ic_shopping_cart_24dp
            is Transaction.Refund -> R.drawable.ic_remove_shopping_cart_24dp
        }
        setImageResource(resourceId)
    }
}

@BindingAdapter("transactionType")
fun TextView.bindTransactionType(transaction: Transaction?) {
    val resId = when (transaction) {
        null -> R.string.none
        is Transaction.Funding -> R.string.funding
        is Transaction.Purchase -> R.string.purchase
        is Transaction.Refund -> R.string.refund
    }
    setText(resId)
}

@BindingAdapter("transactionValue")
fun TextView.bindTransactionValue(transaction: Transaction?) {
    val resId = when {
        transaction == null -> R.string.no_data
        transaction.value > 0 -> R.string.positive_currency_format
        else -> R.string.currency_format
    }
    text = context.getString(resId, transaction?.value)
}

@BindingAdapter("transactionDetails")
fun TextView.bindTransactionDetails(transaction: Transaction?) {
    val defaultText = { amount: Int, itemName: String ->
        context.getString(R.string.transaction_item_details, itemName, amount)
    }
    val newText = when (transaction) {
        null -> ""
        is Transaction.Funding -> ""
        is Transaction.Purchase -> defaultText(transaction.amount, transaction.itemId)
        is Transaction.Refund -> defaultText(transaction.amount, transaction.itemId)
    }
    text = newText
}
