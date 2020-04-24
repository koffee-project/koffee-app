package eu.yeger.koffee.ui.user_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eu.yeger.koffee.databinding.CardUserEntryBinding
import eu.yeger.koffee.domain.UserEntry
import eu.yeger.koffee.ui.OnClickListener

class UserEntryListAdapter(private val onClickListener: OnClickListener<UserEntry>) : ListAdapter<UserEntry, UserEntryListAdapter.ViewHolder>(DiffCallback) {

    inner class ViewHolder(private val binding: CardUserEntryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds a [UserEntry] to the CardUserEntryBinding.
         *
         * @param userEntry The [UserEntry] that will be bound to the CardUserEntryBinding.
         */
        fun bind(userEntry: UserEntry) {
            binding.userEntry = userEntry
            binding.onClickListener = onClickListener
            binding.executePendingBindings()
        }
    }

    /**
     * [DiffUtil.ItemCallback](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil.ItemCallback) for [UserEntry]s.
     *
     * @author Jan Müller
     */
    companion object DiffCallback : DiffUtil.ItemCallback<UserEntry>() {

        /**
         * Checks if two [UserEntry]s have the same id.
         *
         * @param oldItem The old [UserEntry].
         * @param newItem The new [UserEntry].
         * @return true if both [UserEntry]s have the same id or false otherwise.
         */
        override fun areItemsTheSame(oldItem: UserEntry, newItem: UserEntry) = oldItem.id == newItem.id

        /**
         * Checks if two [UserEntry]s are equal.
         *
         * @param oldItem The old [UserEntry].
         * @param newItem The new [UserEntry].
         * @return true if both [UserEntry]s are equal or false otherwise.
         */
        override fun areContentsTheSame(oldItem: UserEntry, newItem: UserEntry) = oldItem == newItem
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CardUserEntryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userEntry: UserEntry = getItem(position)
        holder.bind(userEntry)
    }
}
