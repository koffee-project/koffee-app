package eu.yeger.koffee.ui.user.details

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.paging.toLiveData
import eu.yeger.koffee.BuildConfig
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.TransactionRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.ui.SimpleAction
import eu.yeger.koffee.utility.singleTickTimer
import eu.yeger.koffee.utility.sourcedLiveData

private const val PAGE_SIZE = 50

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

    val transactions = transactionRepository.getTransactionsByUserIdPaged(userId).toLiveData(PAGE_SIZE)
    val hasTransactions = transactions.map { it.isNotEmpty() }

    private var refundTimer: CountDownTimer? = null

    private val isWithinRefundInterval = MutableLiveData(true)

    private val hasRefundable =
        transactionRepository.getLastRefundableTransactionByUserIdAsLiveData(userId).map {
            refundTimer?.cancel()
            when {
                it === null || !isActiveUser -> false // no refundable
                else -> {
                    val elapsedTime = System.currentTimeMillis() - it.timestamp
                    val remainingTime = BuildConfig.REFUND_INTERVAL - elapsedTime
                    when {
                        remainingTime > 0 -> {
                            isWithinRefundInterval.postValue(true)
                            refundTimer = singleTickTimer(remainingTime) {
                                isWithinRefundInterval.postValue(false)
                            }.start()
                            true
                        }
                        else -> false
                    }
                }
            }
        }

    val canRefund = sourcedLiveData(isWithinRefundInterval, hasRefundable) {
        isWithinRefundInterval.value == true && hasRefundable.value == true
    }

    val canEdit = sourcedLiveData(isAuthenticated, hasUser) {
        isAuthenticated.value == true && hasUser.value == true
    }

    val canDelete = canEdit.map { !isActiveUser && it }

    private val _refreshing = MutableLiveData(false)
    val refreshing: LiveData<Boolean> = _refreshing

    val editUserAction = DataAction<String>()
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
                userDeletedAction.activate()
            }
        }
    }

    fun activateEditUserAction() = editUserAction.activateWith(user.value?.id)

    fun activateDeleteUserAction() = deleteUserAction.activateWith(user.value?.id)

    override fun onCleared() {
        super.onCleared()
        refundTimer?.cancel()
    }
}
