package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.yeger.koffee.databinding.CardUserBinding
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.ui.OnClickListener

class UserListAdapter(private val onClickListener: OnClickListener<User>) :
    ListAdapter<User, UserListAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: CardUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds a [User] to the CardUserBinding.
         *
         * @param user The [User] that will be bound to the CardUserBinding.
         */
        fun bind(user: User) {
            binding.user = user
            binding.onClickListener = onClickListener
            binding.executePendingBindings()
        }
    }

    /**
     * [DiffUtil.ItemCallback](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil.ItemCallback) for [User]s.
     *
     * @author Jan Müller
     */
    companion object DiffCallback : DiffUtil.ItemCallback<User>() {

        /**
         * Checks if two [User]s have the same id.
         *
         * @param oldItem The old [User].
         * @param newItem The new [User].
         * @return true if both [User]s have the same id or false otherwise.
         */
        override fun areItemsTheSame(oldItem: User, newItem: User) =
            oldItem.id == newItem.id

        /**
         * Checks if two [User]s are equal.
         *
         * @param oldItem The old [User].
         * @param newItem The new [User].
         * @return true if both [User]s are equal or false otherwise.
         */
        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User = getItem(position)
        holder.bind(user)
    }
}
