package eu.yeger.koffee.ui.home

import androidx.lifecycle.*
import eu.yeger.koffee.repository.UserEntryRepository

class HomeViewModel(
    private val userId: String?,
    private val userEntryRepository: UserEntryRepository
) : ViewModel() {

    val user = userEntryRepository.getUserById(userId)

    val hasUser = user.map { it != null }

    class Factory(
        private val userId: String?,
        private val userEntryRepository: UserEntryRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(userId, userEntryRepository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
