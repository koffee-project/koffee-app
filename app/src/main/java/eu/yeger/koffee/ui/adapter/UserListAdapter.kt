package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import eu.yeger.koffee.databinding.CardUserBinding
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.ui.OnClickListener

class UserViewHolder(
    private val binding: CardUserBinding
) : GenericPagedListAdapter.ViewHolder<User>(binding.root) {

    companion object Factory : GenericPagedListAdapter.ViewHolderFactory<User> {
        override fun createViewHolder(parent: ViewGroup): GenericPagedListAdapter.ViewHolder<User> {
            return UserViewHolder(
                CardUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun bind(item: User, onClickListener: OnClickListener<User>) {
        binding.apply {
            user = item
            this.onClickListener = onClickListener
        }.executePendingBindings()
    }
}

fun userListAdapter(onClickListener: OnClickListener<User>): GenericPagedListAdapter<User> {
    return GenericPagedListAdapter(
        onClickListener,
        UserViewHolder.Factory,
        itemCallback(
            isSame = { old, new -> old.id == new.id },
            isIdentical = { old, new -> old == new }
        )
    )
}
