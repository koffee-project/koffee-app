package eu.yeger.koffee.ui.user.details

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
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

    private val _editUserAction = MutableLiveData<String>(null)
    val editUserAction: LiveData<String> = _editUserAction

    private val _deleteUserAction = MutableLiveData<String>(null)
    val deleteUserAction: LiveData<String> = _deleteUserAction

    private val _userDeletedAction = MutableLiveData(false)
    val userDeletedAction: LiveData<Boolean> = _userDeletedAction

    init {
        userId?.let {
            launchOnViewModelScope {
                userRepository.fetchUserById(userId)
            }
            launchOnViewModelScope {
                transactionRepository.fetchTransactionsByUserId(userId)
            }
        }
    }

    fun refundPurchase() {
        if (!isActiveUser || userId === null) return

        launchOnViewModelScope {
            transactionRepository.run {
                refundPurchase(userId)
                fetchTransactionsByUserId(userId)
            }
            userRepository.fetchUserById(userId)
        }
    }

    fun deleteUser() {
        userId?.let {
            launchOnViewModelScope {
                val jwt = adminRepository.getJWT()!!
                userRepository.deleteUser(userId, jwt)
                _userDeletedAction.value = true
            }
        }
    }

    fun triggerEditUserAction() = _editUserAction.postValue(user.value?.id)

    fun onEditUserActionHandled() = _editUserAction.postValue(null)

    fun triggerDeleteUserAction() = _deleteUserAction.postValue(user.value?.id)

    fun onDeleteUserActionHandled() = _deleteUserAction.postValue(null)

    fun onUserDeletedActionHandled() = _userDeletedAction.postValue(false)
}
