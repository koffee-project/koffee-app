package eu.yeger.koffee.ui.item.creation

import androidx.lifecycle.MutableLiveData
import eu.yeger.koffee.repository.AdminRepository
import eu.yeger.koffee.repository.ItemRepository
import eu.yeger.koffee.ui.CoroutineViewModel
import eu.yeger.koffee.ui.DataAction
import eu.yeger.koffee.utility.isValidCurrencyAmount
import eu.yeger.koffee.utility.nullIfBlank
import eu.yeger.koffee.utility.sourcedLiveData

/**
 * [CoroutineViewModel] for creating items.
 *
 * @property adminRepository [AdminRepository] for accessing authentication tokens.
 * @property itemRepository [ItemRepository] for creating the item.
 * @property itemId Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the item id.
 * @property itemName Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the item name.
 * @property itemPrice Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the item price.
 * @property itemAmount Bidirectional [MutableLiveData](https://developer.android.com/reference/androidx/lifecycle/MutableLiveData) for binding the item amount.
 * @property canCreateItem Indicates that creating an item is possible with the current input values.
 * @property itemCreatedAction [DataAction] that is activated when the item has been created. Contains the item's id.
 *
 * @author Jan Müller
 */
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

    /**
     * Creates an item with the current id, name, price and amount.
     */
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
