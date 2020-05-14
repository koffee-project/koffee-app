package eu.yeger.koffee.ui.item.editing

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.SuccessViewModel
import eu.yeger.koffee.utility.isValidPrice
import eu.yeger.koffee.utility.sourcedLiveData

class ItemEditingViewModel(
    val itemId: String,
    private val adminRepository: AdminRepository,
    private val itemRepository: ItemRepository
) : SuccessViewModel<String>() {

    val itemName = MutableLiveData("")

    val itemPrice = MutableLiveData("")

    val itemAmount = MutableLiveData("")

    val canUpdateItem = sourcedLiveData(itemName, itemPrice, itemAmount) {
        itemName.value.isNullOrBlank().not() &&
                itemPrice.value?.toDoubleOrNull().isValidPrice() &&
                (itemAmount.value.isNullOrBlank() || itemAmount.value?.toIntOrNull() ?: -1 >= 0)
    }

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
            setSuccessResult(itemId)
        }
    }
}
