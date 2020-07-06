package eu.yeger.koffee

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * The main [Activity](https://developer.android.com/reference/androidx/appcompat/app/AppCompatActivity) of this application.
 * Used for single user mode.
 *
 * @author Jan Müller
 */
class MainActivity : AppCompatActivity() {

    /**
     * Inflates the view and initializes the nav controller.
     *
     * @param savedInstanceState Unused.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(navController)
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
 * Finished the current activity and starts [MainActivity].
 *
 * @receiver The current activity, that will be finished.
 */
fun Activity.goToMainActivity() {
    val intent = Intent(this, MainActivity::class.java)
    startActivity(intent)
    finish()
}
