package eu.yeger.koffee

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * [Activity](https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity) for user selection.
 *
 * @author Jan Müller
 */
class UserSelectionActivity : AppCompatActivity() {

    /**
     * Inflates the view.
     *
     * @param savedInstanceState Unused.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_selection)
    }
}

/**
 * Starts [UserSelectionActivity] but does not finish the current activity.
 *
 * @receiver The current activity.
 */
fun Activity.goToUserSelection() {
    val intent = Intent(this, UserSelectionActivity::class.java)
    startActivity(intent)
}
