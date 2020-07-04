package eu.yeger.koffee.ui.adapter

import androidx.recyclerview.widget.DiffUtil

/**
 * Creates a diff callback that uses the given lambdas.
 *
 * @param T The type of the elements.
 * @param isSame Evaluates if two elements are the same.
 * @param isIdentical Evaluates if two elements are identical.
 *
 * @author Jan Müller
 */
fun <T> itemCallback(isSame: (T, T) -> Boolean, isIdentical: (T, T) -> Boolean) = object : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) = isSame(oldItem, newItem)

    override fun areContentsTheSame(oldItem: T, newItem: T) = isIdentical(oldItem, newItem)
}
