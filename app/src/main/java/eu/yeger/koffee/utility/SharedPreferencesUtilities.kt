package eu.yeger.koffee.utility

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

private const val activeUserId = "active_user_id"

/**
 * Getter for this app's shared preferences.
 *
 * @receiver A context of this app.
 * @return This app's shared preferences.
 *
 * @author Jan Müller
 */
val Context.sharedPreferences: SharedPreferences
    get() = getSharedPreferences("eu.yeger.koffee", Context.MODE_PRIVATE)

/**
 * Saves the user id to this app's shared preferences.
 *
 * @receiver A context of this app.
 * @param userId The user id to be saved.
 *
 * @author Jan Müller
 */
fun Context.saveUserIdToSharedPreferences(userId: String) {
    sharedPreferences.edit {
        putString(activeUserId, userId)
    }
}

/**
 * Deletes the user id from this app's shared preferences.
 *
 * @receiver A context of this app.
 *
 * @author Jan Müller
 */
fun Context.deleteUserIdFromSharedPreferences() {
    sharedPreferences.edit {
        putString(activeUserId, null)
    }
}

/**
 * Reads the user id from this app's shared preferences.
 *
 * @receiver A context of this app.
 * @return The saves user id.
 *
 * @author Jan Müller
 */
fun Context.getUserIdFromSharedPreferences(): String? {
    return sharedPreferences.getString(activeUserId, null)
}
