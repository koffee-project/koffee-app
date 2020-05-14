package eu.yeger.koffee.ui.home

import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.utility.ActionLiveData

class HomeViewModel(
    private val userId: String?,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    val userSelectionRequiredAction = ActionLiveData<Boolean>()

    init {
        onViewModelScope {
            userId?.let {
                userRepository.fetchUserById(userId)
            }
        }.invokeOnCompletion {
            onViewModelScope {
                userSelectionRequiredAction.trigger(userRepository.hasUserWithId(userId).not())
            }
        }
    }
}
