package eu.yeger.koffee.ui.item.creation

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SuccessViewModel
import eu.yeger.koffee.utility.isValidPrice
import eu.yeger.koffee.utility.sourcedLiveData

class ItemCreationViewModel(
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository
) : SuccessViewModel<String>() {

    val itemId = MutableLiveData("")

    val itemName = MutableLiveData("")

    val itemPrice = MutableLiveData("")

    val itemAmount = MutableLiveData("")

    val canCreateItem = sourcedLiveData(itemId, itemName, itemPrice, itemAmount) {
        itemId.value.isNullOrBlank().not() &&
                itemName.value.isNullOrBlank().not() &&
                itemPrice.value?.toDoubleOrNull().isValidPrice() &&
                (itemAmount.value.isNullOrBlank() || itemAmount.value?.toIntOrNull() ?: -1 >= 0)
    }

    fun createItem() {
        launchOnViewModelScope {
            val jwt = adminRepository.getJWT()!!
            val itemId = itemId.value!!
            itemRepository.createItem(
                itemId = itemId,
                itemName = itemName.value!!,
                itemPrice = itemPrice.value!!.toDouble(),
                itemAmount = itemAmount.value?.toIntOrNull(),
                jwt = jwt
            )
            setSuccessResult(itemId)
        }
    }
}
