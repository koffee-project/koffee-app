package eu.yeger.koffee

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController

/**
 * [Activity](https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity) for shared multi user mode.
 *
 * @author Jan Müller
 */
class SharedActivity : AppCompatActivity() {

    /**
     * Inflates the view and initializes the nav controller.
     *
     * @param savedInstanceState Unused.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_shared)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_shared_user_list))

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    /**
     * Delegates navigation to the nav controller.
     *
     * @return true if navigation was performed.
     */
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}

/**
 * Finished the current activity and starts [SharedActivity].
 *
 * @receiver The current activity, that will be finished.
 */
fun Activity.goToSharedActivity() {
    val intent = Intent(this, SharedActivity::class.java)
    startActivity(intent)
    finish()
}
