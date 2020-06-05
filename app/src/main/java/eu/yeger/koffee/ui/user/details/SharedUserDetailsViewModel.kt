package eu.yeger.koffee.ui.user.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.SimpleAction

class SharedUserDetailsViewModel(
    userId: String,
    userRepository: UserRepository,
    profileImageRepository: ProfileImageRepository,
    transactionRepository: TransactionRepository
) : UserDetailsViewModel(
    userId = userId,
    profileImageRepository = profileImageRepository,
    transactionRepository = transactionRepository,
    userRepository = userRepository
) {

    override val canModify: LiveData<Boolean>
        get() = MutableLiveData(false)

    override val canDelete: LiveData<Boolean>
        get() = MutableLiveData(false)

    override val showItemsButton = true

    val showItemsAction = SimpleAction()

    override fun activateShowItemsAction() = showItemsAction.activate()

    override fun activateEditUserAction() = Unit

    override fun activateCreditUserAction() = Unit

    override fun activateDeleteUserAction() = Unit
}
