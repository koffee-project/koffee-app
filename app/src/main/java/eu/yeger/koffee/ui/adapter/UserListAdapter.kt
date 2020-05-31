package eu.yeger.koffee.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import eu.yeger.koffee.databinding.CardUserBinding
import eu.yeger.koffee.domain.User
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.ui.OnClickListener
import kotlinx.coroutines.*

class UserViewHolder(
    private val binding: CardUserBinding,
    private val profileImageRepository: ProfileImageRepository,
    private val scope: CoroutineScope
) : GenericPagedListAdapter.ViewHolder<User>(binding.root) {

    private var job: Job? = null

    class Factory(private val profileImageRepository: ProfileImageRepository) :
        GenericPagedListAdapter.ViewHolderFactory<User> {

        private val scope = CoroutineScope(Dispatchers.Main)

        fun clear() {
            scope.cancel()
        }

        override fun createViewHolder(parent: ViewGroup): GenericPagedListAdapter.ViewHolder<User> {
            return UserViewHolder(
                CardUserBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                profileImageRepository,
                scope
            )
        }
    }

    override fun bind(item: User, onClickListener: OnClickListener<User>) {
        job?.cancel()
        binding.apply {
            user = item
            profileImage = null
            this.onClickListener = onClickListener
        }.executePendingBindings()
        job = scope.launch {
            profileImageRepository.fetchProfileImageByUserId(item.id)
            val image = profileImageRepository.getProfileImageByUserId(item.id)
            binding.apply {
                profileImage = image
            }.executePendingBindings()
        }
    }
}

fun userListAdapter(
    factory: UserViewHolder.Factory,
    onClickListener: OnClickListener<User>
): GenericPagedListAdapter<User> {
    return GenericPagedListAdapter(
        onClickListener,
        factory,
        itemCallback(
            isSame = { old, new -> old.id == new.id },
            isIdentical = { old, new -> old == new }
        )
    )
}
