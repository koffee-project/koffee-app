package eu.yeger.koffee

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class UserSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user_selection)
    }
}

fun Activity.goToUserSelection() {
    val intent = Intent(this, UserSelectionActivity::class.java)
    startActivity(intent)
    finish()
}
