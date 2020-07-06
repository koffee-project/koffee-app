package eu.yeger.koffee.ui.user.crediting

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.utility.isValidCurrencyAmount
import eu.yeger.koffee.utility.sourcedLiveData

/**
 * ViewModel for crediting users.
 *
 * @property userId The id of the user that is being credited.
 * @property adminRepository [AdminRepository] for accessing authentication tokens.
 * @property userRepository [UserRepository] for crediting the user.
 * @property creditAmount Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the crediting amount.
 * @property canCreditUser Indicates that crediting a user is possible with the current input values.
 * @property userCreditedAction [DataAction] that is activated when the user has been credited.
 *
 * @author Jan Müller
 */
class UserCreditingViewModel(
    val userId: String,
    private val adminRepository: AdminRepository,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    val creditAmount = MutableLiveData("")

    val canCreditUser = sourcedLiveData(creditAmount) {
        creditAmount.value?.toDoubleOrNull().isValidCurrencyAmount()
    }

    val userCreditedAction = DataAction<String>()

    /**
     * Processes a funding with the given amount for the user with the given id.
     */
    fun creditUser() {
        onViewModelScope {
            adminRepository.getJWT()?.let {
                userRepository.creditUser(
                    userId = userId,
                    amount = creditAmount.value!!.toDouble(),
                    jwt = it
                )
                userCreditedAction.activateWith(userId)
            }
        }
    }
}
