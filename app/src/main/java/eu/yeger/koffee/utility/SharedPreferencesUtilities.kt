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
