package eu.yeger.koffee.ui.item.creation

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.utility.isValidCurrencyAmount
import eu.yeger.koffee.utility.nullIfBlank
import eu.yeger.koffee.utility.sourcedLiveData

class ItemCreationViewModel(
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository
) : CoroutineViewModel() {

    val itemId = MutableLiveData("")

    val itemName = MutableLiveData("")

    val itemPrice = MutableLiveData("")

    val itemAmount = MutableLiveData("")

    val canCreateItem = sourcedLiveData(itemName, itemPrice, itemAmount) {
        itemName.value.isNullOrBlank().not() &&
                itemPrice.value?.toDoubleOrNull().isValidCurrencyAmount() &&
                (itemAmount.value.isNullOrBlank() || itemAmount.value?.toIntOrNull() ?: -1 >= 0)
    }

    val itemCreatedAction = DataAction<String>()

    fun createItem() {
        onViewModelScope {
            val jwt = adminRepository.getJWT()!!
            val itemId = itemRepository.createItem(
                itemId = itemId.value?.nullIfBlank(),
                itemName = itemName.value!!,
                itemPrice = itemPrice.value!!.toDouble(),
                itemAmount = itemAmount.value?.toIntOrNull(),
                jwt = jwt
            )
            itemCreatedAction.activateWith(itemId)
        }
    }
}
