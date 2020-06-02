package eu.yeger.koffee.ui.alternative.user.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.ProfileImageRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.user.details.BaseUserDetailsViewModel

class AlternativeUserDetailsViewModel(
    userId: String,
    userRepository: UserRepository,
    profileImageRepository: ProfileImageRepository,
    transactionRepository: TransactionRepository
) : BaseUserDetailsViewModel(
    isActiveUser = true,
    userId = userId,
    profileImageRepository = profileImageRepository,
    transactionRepository = transactionRepository,
    userRepository = userRepository
) {
    override val canModify: LiveData<Boolean>
        get() = MutableLiveData(false)

    override val canDelete: LiveData<Boolean>
        get() = MutableLiveData(false)

    override fun activateEditUserAction() = Unit

    override fun activateCreditUserAction() = Unit

    override fun activateDeleteUserAction() = Unit
}
