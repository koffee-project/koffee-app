package eu.yeger.koffee.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private const val activeUserId = "active_user_id"

/**
 * Getter for this app's shared preferences.
 *
 * @return This app's shared preferences.
 *
 * @author Jan Müller
 */
val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences("eu.yeger.koffee", Context.MODE_PRIVATE)

fun Context.saveUserIdToSharedPreferences(userId: String) {
    sharedPreferences.edit {
        putString(activeUserId, userId)
    }
}

fun Context.deleteUserIdFromSharedPreferences() {
    sharedPreferences.edit {
        putString(activeUserId, null)
    }
}

fun Context.getUserIdFromSharedPreferences(): String? {
    return sharedPreferences.getString(activeUserId, null)
}

fun Context.getUserIdFromSharedPreferencesIfNull(userId: String?): String? {
    return when (userId) {
        null -> getUserIdFromSharedPreferences() // use active userid if no explicit id was passed
        else -> userId // use argument id otherwise
    }
}
