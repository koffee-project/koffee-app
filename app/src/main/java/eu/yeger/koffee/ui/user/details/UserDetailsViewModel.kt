package eu.yeger.koffee.ui.user.details

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.SuccessErrorViewModel
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

class UserDetailsViewModel(
    private val isActiveUser: Boolean,
    private val userId: String?,
    private val adminRepository: AdminRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : SuccessErrorViewModel<String>() {

    private val isAuthenticated = adminRepository.isAuthenticatedAsLiveData()

    val user = userRepository.getUserByIdAsLiveData(userId)

    val hasUser = user.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserId(userId)

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
            viewModelScope.launch {
                userRepository.fetchUserById(userId)
            }
            viewModelScope.launch {
                transactionRepository.fetchTransactionsByUserId(userId)
            }
        }
    }

    fun refundPurchase() {
        if (!isActiveUser || userId === null) return

        viewModelScope.launch {
            transactionRepository.run {
                refundPurchase(userId)
                fetchTransactionsByUserId(userId)
            }
            userRepository.fetchUserById(userId)
        }
    }

    fun triggerEditUserAction() {
        viewModelScope.launch {
            _editUserAction.value = user.value?.id
        }
    }

    fun onEditUserActionHandled() {
        viewModelScope.launch {
            _editUserAction.value = null
        }
    }

    fun triggerDeleteUserAction() {
        viewModelScope.launch {
            _deleteUserAction.value = user.value?.id
        }
    }

    fun onDeleteUserActionHandled() {
        viewModelScope.launch {
            _deleteUserAction.value = null
        }
    }

    fun deleteUser() {
        userId?.let {
            viewModelScope.launch(exceptionHandler) {
                val jwt = adminRepository.getJWT()!!
                userRepository.deleteUser(userId, jwt)
                _userDeletedAction.value = true
            }
        }
    }

    fun onUserDeletedActionHandled() {
        viewModelScope.launch {
            _userDeletedAction.value = false
        }
    }
}
