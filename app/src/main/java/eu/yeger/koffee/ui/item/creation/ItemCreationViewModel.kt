package eu.yeger.koffee.ui.item.creation

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SuccessErrorViewModel
import eu.yeger.koffee.utility.sourcedLiveData
import kotlinx.coroutines.launch

class ItemCreationViewModel(
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository
) : SuccessErrorViewModel<String>() {

    val itemId = MutableLiveData("")

    val itemName = MutableLiveData("")

    val itemPrice = MutableLiveData("")

    val itemAmount = MutableLiveData("")

    // TODO verify price
    val canCreateItem = sourcedLiveData(itemId, itemName, itemPrice, itemAmount) {
        itemId.value.isNullOrBlank().not()
                && itemName.value.isNullOrBlank().not()
                && (itemAmount.value.isNullOrBlank() || itemAmount.value?.toIntOrNull() ?: -1 >= 0)
    }

    // TODO catch exceptions
    fun createItem() {
        viewModelScope.launch(exceptionHandler) {
            val jwt = adminRepository.getJWT()!!
            val itemId = itemId.value!!
            itemRepository.createItem(
                itemId = itemId,
                itemName = itemName.value!!,
                itemPrice = itemPrice.value!!.toDouble(),
                itemAmount = itemAmount.value?.toIntOrNull(),
                jwt = jwt
            )
            _successAction.value = itemId
        }
    }

    class Factory(
        private val adminRepository: AdminRepository,
        private val itemRepository: ItemRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ItemCreationViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ItemCreationViewModel(
                    adminRepository,
                    itemRepository
                ) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
