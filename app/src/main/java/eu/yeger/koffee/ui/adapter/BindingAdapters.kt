package eu.yeger.koffee.ui.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import eu.yeger.koffee.R
import eu.yeger.koffee.domain.Item
import eu.yeger.koffee.domain.ProfileImage
import eu.yeger.koffee.domain.PurchaseStatistic
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
 * Submits an [Item] PagedList to the [GenericPagedListAdapter] of a [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
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

/**
 * Submits a [List] of [PurchaseStatistic]s to the [PurchaseStatisticListAdapter] of a [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 *
 * @receiver The target [RecyclerView](https://developer.android.com/jetpack/androidx/releases/recyclerview).
 * @param statistics The [List] of [PurchaseStatistic]s that will be submitted.
 *
 * @author Jan Müller
 */
@BindingAdapter("statistics")
fun RecyclerView.bindStatistics(statistics: List<PurchaseStatistic>?) {
    val adapter = adapter as PurchaseStatisticListAdapter
    adapter.submitList(statistics) {
        layoutManager?.scrollToPosition(0)
    }
}

/**
 * Sets the resource of an [ImageView](https://developer.android.com/reference/android/widget/ImageView) depending on the type of a [Transaction].
 *
 * @receiver The target [ImageView](https://developer.android.com/reference/android/widget/ImageView).
 * @param transaction The source [Transaction].
 *
 * @author Jan Müller
 */
@BindingAdapter("transaction")
fun ImageView.bindTransaction(transaction: Transaction?) {
    transaction?.let {
        val resourceId = when (transaction) {
            is Transaction.Funding -> R.drawable.ic_attach_money_24dp
            is Transaction.Purchase -> R.drawable.ic_shopping_cart_24dp
            is Transaction.Refund -> R.drawable.ic_remove_shopping_cart_24dp
        }
        setImageResource(resourceId)
    }
}

/**
 * Sets the text and color of a [TextView](https://developer.android.com/reference/android/widget/TextView) depending on the value of a [Transaction].
 *
 * @receiver The target [TextView](https://developer.android.com/reference/android/widget/TextView).
 * @param transaction The source [Transaction].
 *
 * @author Jan Müller
 */
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

/**
 * Sets the text of a [TextView](https://developer.android.com/reference/android/widget/TextView) depending on the details of a [Transaction].
 *
 * @receiver The target [TextView](https://developer.android.com/reference/android/widget/TextView).
 * @param transaction The source [Transaction].
 *
 * @author Jan Müller
 */
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

/**
 * Binds a currency [Double]-value to a [TextView](https://developer.android.com/reference/android/widget/TextView) and sets its background depending on the sign.
 *
 * @receiver The target [TextView](https://developer.android.com/reference/android/widget/TextView).
 * @param value The source value.
 *
 * @author Jan Müller
 */
@BindingAdapter("currencyValue")
fun TextView.bindCurrencyValue(value: Double?) {
    when (value) {
        null -> {
            text = ""
            background = null
        }
        else -> {
            text = resources.getString(R.string.currency_format, value)
            background = when {
                value >= 0 -> resources.getDrawable(R.drawable.green_rectangle, context.theme)
                else -> resources.getDrawable(R.drawable.red_rectangle, context.theme)
            }
            setTextColor(resources.getColor(R.color.dark_grey, context.theme))
        }
    }
}

/**
 * Binds a [ProfileImage] to an [ImageView](https://developer.android.com/reference/android/widget/ImageView) with a placeholder.
 *
 * @receiver The target [ImageView](https://developer.android.com/reference/android/widget/ImageView).
 * @param profileImage The [ProfileImage] to be used.
 * @param placeholder The placeholder resource, if the image is null.
 *
 * @author Jan Müller
 */
fun ImageView.bindProfileImage(profileImage: ProfileImage?, placeholder: Int) {
    when (profileImage) {
        null -> setImageResource(placeholder)
        else -> Glide.with(context)
            .load(profileImage.bytes)
            .signature(ObjectKey(profileImage.timestamp))
            .fitCenter()
            .circleCrop()
            .placeholder(R.drawable.ic_edit_24dp)
            .into(this)
    }
}

/**
 * Binds a [ProfileImage] to an [ImageView](https://developer.android.com/reference/android/widget/ImageView) with a person-icon as a placeholder.
 *
 * @receiver The target [ImageView](https://developer.android.com/reference/android/widget/ImageView).
 * @param profileImage The [ProfileImage] to be used.
 *
 * @author Jan Müller
 */
@BindingAdapter("regularProfileImage")
fun ImageView.bindRegularProfileImage(profileImage: ProfileImage?) =
    bindProfileImage(profileImage, R.drawable.ic_person_24dp)

/**
 * Binds a [ProfileImage] to an [ImageView](https://developer.android.com/reference/android/widget/ImageView) with an edit-icon as a placeholder.
 *
 * @receiver The target [ImageView](https://developer.android.com/reference/android/widget/ImageView).
 * @param profileImage The [ProfileImage] to be used.
 *
 * @author Jan Müller
 */
@BindingAdapter("editableProfileImage")
fun ImageView.bindEditableProfileImage(profileImage: ProfileImage?) =
    bindProfileImage(profileImage, R.drawable.ic_edit_24dp)
