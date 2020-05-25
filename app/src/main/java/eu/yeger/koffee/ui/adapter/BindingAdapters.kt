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
 * @param present true for View.VISIBLE and false for View.GONE.
 *
 * @author Jan Müller
 */
@BindingAdapter("present")
fun View.bindPresence(present: Boolean?) {
    visibility = if (present == true) View.VISIBLE else View.GONE
}

/**
 * Sets the visibility of a [View](https://developer.android.com/reference/android/view/View).
 *
 * @receiver The target view.
 * @param visible true for View.VISIBLE and false for View.INVISIBLE.
 *
 * @author Jan Müller
 */
@BindingAdapter("visible")
fun View.bindVisibility(visible: Boolean?) {
    visibility = if (visible == true) View.VISIBLE else View.INVISIBLE
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
fun <T> RecyclerView.bindItems(items: PagedList<T>?, callback: (() -> Unit)?) {
    @Suppress("UNCHECKED_CAST")
    val adapter = adapter as GenericPagedListAdapter<T>
    adapter.submitList(items) {
        callback?.invoke()
        layoutManager?.scrollToPosition(0)
    }
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

@BindingAdapter("transactionValue")
fun TextView.bindTransactionValue(transaction: Transaction?) {
    setTextColor(resources.getColor(R.color.design_default_color_on_primary, context.theme))
    val resId = when {
        transaction == null -> R.string.no_data
        transaction.value > 0 -> {
            setTextColor(resources.getColor(R.color.colorGreen, context.theme))
            R.string.positive_currency_format
        }
        else -> R.string.currency_format
    }
    text = context.getString(resId, transaction?.value)
}

@BindingAdapter("transactionDetails")
fun TextView.bindTransactionDetails(transaction: Transaction?) {
    val defaultText = { amount: Int, itemName: String ->
        if (amount > 1) {
            context.getString(R.string.transaction_item_details, itemName, amount)
        } else {
            itemName
        }
    }
    val newText = when (transaction) {
        is Transaction.Purchase -> defaultText(transaction.amount, transaction.itemName)
        is Transaction.Refund -> defaultText(transaction.amount, transaction.itemName)
        else -> context.getString(R.string.funding)
    }
    text = newText
}
