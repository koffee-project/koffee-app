package eu.yeger.koffee.ui

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import eu.yeger.koffee.domain.UserEntry
import eu.yeger.koffee.ui.user_selection.UserEntryListAdapter

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
 * Submits a [UserEntry] [List] to the [UserEntryListAdapter] of a [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 *
 * @receiver The target [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 * @param userEntries The [List] of [UserEntry] objects that will be submitted.
 * @param callback Callback, which is executed after the list has been submitted.
 *
 * @author Jan Müller
 */
@BindingAdapter(value = ["userEntries", "callback"], requireAll = true)
fun RecyclerView.bindUserEntryList(userEntries: List<UserEntry>?, callback: Runnable) {
    val adapter = adapter as UserEntryListAdapter
    adapter.submitList(userEntries, callback)
}
