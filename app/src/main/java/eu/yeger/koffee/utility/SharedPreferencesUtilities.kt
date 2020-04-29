package eu.yeger.koffee.utility

import android.content.Context
import android.content.SharedPreferences

/**
 * Getter for this app's shared preferences.
 *
 * @return This app's shared preferences.
 *
 * @author Jan Müller
 */
val Context.sharedPreferences: SharedPreferences
        get() = getSharedPreferences("eu.yeger.koffee", Context.MODE_PRIVATE)

object SharedPreferencesKeys {
    const val activeUserId = "active_user_id"
}

fun Context.getUserIdFromSharedPreferencesIfNull(userId: String?): String? {
    return when (userId) {
        null -> sharedPreferences.getString(
            SharedPreferencesKeys.activeUserId,
            null
        ) // use active userid if no explicit id was passed
        else -> userId // use argument id otherwise
    }
}
