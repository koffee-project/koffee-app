package eu.yeger.koffee.ui.home

import eu.yeger.koffee.repository.UserRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.SimpleAction

class HomeViewModel(
    private val userId: String?,
    private val userRepository: UserRepository
) : CoroutineViewModel() {

    val userSelectionRequiredAction = SimpleAction()

    init {
        onViewModelScope {
            userId?.let {
                userRepository.fetchUserById(userId)
            }
        }.invokeOnCompletion {
            onViewModelScope {
                if (!userRepository.hasUserWithId(userId)) {
                    userSelectionRequiredAction.activate()
                }
            }
        }
    }
}
