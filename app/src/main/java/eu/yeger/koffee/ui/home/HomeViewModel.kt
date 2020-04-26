package eu.yeger.koffee.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import eu.yeger.koffee.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    private val userId: String?,
    private val userRepository: UserRepository
) : ViewModel() {

    val user = userRepository.getUserById(userId)

    val hasUser = user.map { it != null }

    init {
        userId?.let {
            viewModelScope.launch {
                userRepository.fetchUserById(userId)
            }
        }
    }

    class Factory(
        private val userId: String?,
        private val userRepository: UserRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(userId, userRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
