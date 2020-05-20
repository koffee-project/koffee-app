package eu.yeger.koffee.ui.user.crediting

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.utility.isValidCurrencyAmount
import eu.yeger.koffee.utility.sourcedLiveData

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
