package eu.yeger.koffee.ui.user.details

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.utility.ActionLiveData
import eu.yeger.koffee.utility.sourcedLiveData

class UserDetailsViewModel(
    private val isActiveUser: Boolean,
    private val userId: String?,
    private val adminRepository: AdminRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    private val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    val user = userRepository.getUserByIdAsLiveData(userId)
    val hasUser = user.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserId(userId)

    // TODO disable refund after expiry
    val canRefund = transactionRepository.getLastRefundableTransactionByUserId(userId)
        .map { isActiveUser && it != null }

    val canModify = sourcedLiveData(isAuthenticated, hasUser) {
        !isActiveUser && isAuthenticated.value ?: false && hasUser.value ?: false
    }

    val editUserAction = ActionLiveData<String?>()
    val deleteUserAction = ActionLiveData<String?>()
    val userDeletedAction = ActionLiveData(false)

    init {
        userId?.let {
            onViewModelScope {
                userRepository.fetchUserById(userId)
            }
            onViewModelScope {
                transactionRepository.fetchTransactionsByUserId(userId)
            }
        }
    }

    fun refundPurchase() {
        if (!isActiveUser || userId === null) return

        onViewModelScope {
            transactionRepository.run {
                refundPurchase(userId)
                fetchTransactionsByUserId(userId)
            }
            userRepository.fetchUserById(userId)
        }
    }

    fun deleteUser() {
        userId?.let {
            onViewModelScope {
                val jwt = adminRepository.getJWT()!!
                userRepository.deleteUser(userId, jwt)
                userDeletedAction.trigger(true)
            }
        }
    }

    fun triggerEditUserAction() = editUserAction.trigger(user.value?.id)

    fun triggerDeleteUserAction() = deleteUserAction.trigger(user.value?.id)
}
