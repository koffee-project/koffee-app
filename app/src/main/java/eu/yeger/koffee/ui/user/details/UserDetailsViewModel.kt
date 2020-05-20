package eu.yeger.koffee.ui.user.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.paging.toLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.sourcedLiveData

private const val PAGE_SIZE = 50

class UserDetailsViewModel(
    private val isActiveUser: Boolean,
    private val userId: String?,
    private val adminRepository: AdminRepository,
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    private val isAuthenticated = adminRepository.isAuthenticatedFlow().asLiveData()

    val user = userRepository.getUserByIdFlow(userId).asLiveData()
    val hasUser = user.map { it != null }

    val transactions = transactionRepository.getTransactionsByUserId(userId).toLiveData(PAGE_SIZE)
    val hasTransactions = transactions.map { it.isNotEmpty() }

    val canModify = sourcedLiveData(isAuthenticated, hasUser) {
        isAuthenticated.value == true && hasUser.value == true
    }

    val canDelete = canModify.map { !isActiveUser && it }

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val editUserAction = DataAction<String>()
    val creditUserAction = DataAction<String>()
    val deleteUserAction = DataAction<String>()
    val userDeletedAction = SimpleAction()
    val userNotFoundAction = SimpleAction()

    fun refreshUser() {
        if (userId == null) {
            userNotFoundAction.activate()
            return
        }

        onViewModelScope {
            _refreshing.value = true
            userRepository.fetchUserById(userId)
        }.invokeOnCompletion {
            _refreshing.postValue(false)
            onViewModelScope {
                if (!userRepository.hasUserWithId(userId)) {
                    userNotFoundAction.activate()
                }
            }
        }
        onViewModelScope {
            transactionRepository.fetchTransactionsByUserId(userId)
        }
    }

    fun deleteUser() {
        userId?.let {
            onViewModelScope {
                val jwt = adminRepository.getJWT()!!
                userRepository.deleteUser(userId, jwt)
                userDeletedAction.activate()
            }
        }
    }

    fun activateEditUserAction() = editUserAction.activateWith(user.value?.id)

    fun activateCreditUserAction() = creditUserAction.activateWith(user.value?.id)

    fun activateDeleteUserAction() = deleteUserAction.activateWith(user.value?.id)
}
