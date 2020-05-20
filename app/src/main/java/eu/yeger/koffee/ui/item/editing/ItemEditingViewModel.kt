package eu.yeger.koffee.ui.item.editing

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.utility.isValidCurrencyAmount
import eu.yeger.koffee.utility.sourcedLiveData

class ItemEditingViewModel(
    val itemId: String,
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository
) : CoroutineViewModel() {

    val itemName = MutableLiveData("")

    val itemPrice = MutableLiveData("")

    val itemAmount = MutableLiveData("")

    val canUpdateItem = sourcedLiveData(itemName, itemPrice, itemAmount) {
        itemName.value.isNullOrBlank().not() &&
                itemPrice.value?.toDoubleOrNull().isValidCurrencyAmount() &&
                (itemAmount.value.isNullOrBlank() || itemAmount.value?.toIntOrNull() ?: -1 >= 0)
    }

    val itemUpdatedAction = DataAction<String>()

    init {
        onViewModelScope {
            itemRepository.getItemById(itemId)?.run {
                itemName.value = name
                itemPrice.value = price.toString()
                itemAmount.value = amount?.toString() ?: ""
            }
        }
    }

    fun updateItem() {
        onViewModelScope {
            val jwt = adminRepository.getJWT()!!
            itemRepository.updateItem(
                itemId = itemId,
                itemName = itemName.value!!,
                itemPrice = itemPrice.value!!.toDouble(),
                itemAmount = itemAmount.value?.toIntOrNull(),
                jwt = jwt
            )
            itemUpdatedAction.activateWith(itemId)
        }
    }
}
