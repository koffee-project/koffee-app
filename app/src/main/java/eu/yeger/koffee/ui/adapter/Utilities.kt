package eu.yeger.koffee.ui.adapter

import androidx.recyclerview.widget.DiffUtil

fun <T> itemCallback(isSame: (T, T) -> Boolean, isIdentical: (T, T) -> Boolean) = object : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) = isSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T) = isIdentical(oldItem, newItem)
}
