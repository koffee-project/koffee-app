package eu.yeger.koffee.ui.item.creation

import androidx.lifecycle.*
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.utility.isValidCurrencyAmount
import eu.yeger.koffee.utility.sourcedLiveData

class ItemCreationViewModel(
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository
) : CoroutineViewModel() {

    val itemId = MutableLiveData("")

    val itemName = MutableLiveData("")

    val itemPrice = MutableLiveData("")

    val itemAmount = MutableLiveData("")

    val canCreateItem = sourcedLiveData(itemId, itemName, itemPrice, itemAmount) {
        itemId.value.isNullOrBlank().not() &&
                itemName.value.isNullOrBlank().not() &&
                itemPrice.value?.toDoubleOrNull().isValidCurrencyAmount() &&
                (itemAmount.value.isNullOrBlank() || itemAmount.value?.toIntOrNull() ?: -1 >= 0)
    }

    val itemCreatedAction = DataAction<String>()

    fun createItem() {
        onViewModelScope {
            val jwt = adminRepository.getJWT()!!
            val itemId = itemId.value!!
            itemRepository.createItem(
                itemId = itemId,
                itemName = itemName.value!!,
                itemPrice = itemPrice.value!!.toDouble(),
                itemAmount = itemAmount.value?.toIntOrNull(),
                jwt = jwt
            )
            itemCreatedAction.activateWith(itemId)
        }
    }
}
