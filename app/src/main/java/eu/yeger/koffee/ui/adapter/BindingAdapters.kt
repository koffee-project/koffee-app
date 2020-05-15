package eu.yeger.koffee.ui.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.Transaction
import eu.yeger.koffee.domain.User

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
 * Submits a [User] [List] to the [UserListAdapter] of a [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 *
 * @receiver The target [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 * @param users The [List] of [User] objects that will be submitted.
 * @param callback Callback, which is executed after the list has been submitted.
 *
 * @author Jan Müller
 */
@BindingAdapter(value = ["users", "callback"], requireAll = true)
fun RecyclerView.bindUserList(users: List<User>?, callback: Runnable) {
    val adapter = adapter as UserListAdapter
    adapter.submitList(users, callback)
    smoothScrollToPosition(0)
}

/**
 * Submits an [Item] [List] to the [ItemListAdapter] of a [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 *
 * @receiver The target [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 * @param items The [List] of [Item] objects that will be submitted.
 * @param callback Callback, which is executed after the list has been submitted.
 *
 * @author Jan Müller
 */
@BindingAdapter(value = ["items", "callback"], requireAll = true)
fun RecyclerView.bindItemList(items: List<Item>?, callback: Runnable) {
    val adapter = adapter as ItemListAdapter
    adapter.submitList(items, callback)
    smoothScrollToPosition(0)
}

/**
 * Submits a [Transaction] [List] to the [TransactionListAdapter] of a [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 *
 * @receiver The target [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 * @param transactions The [List] of [Transaction] objects that will be submitted.
 *
 * @author Jan Müller
 */
@BindingAdapter("transactions")
fun RecyclerView.bindTransactionList(transactions: List<Transaction>?) {
    val adapter = adapter as TransactionListAdapter
    adapter.submitList(transactions)
    smoothScrollToPosition(0)
}
