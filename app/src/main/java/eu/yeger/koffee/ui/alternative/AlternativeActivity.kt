package eu.yeger.koffee.ui.alternative

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import eu.yeger.koffee.R

class AlternativeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_alternative)

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_alt_user_list))

        val navController = findNavController(R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }
}
