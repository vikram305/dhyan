package projects.vikky.com.dhyan

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.lifecycle.Observer
import dagger.android.support.DaggerAppCompatActivity
import projects.vikky.com.dhyan.ui.auth.AuthActivity
import projects.vikky.com.dhyan.ui.auth.AuthResource
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity() {

    private val TAG: String = "BaseActivity"

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        sessionManager.getAuthUser().observe(this, Observer {
            if (it != null) {
                when (it.status) {
                    AuthResource.AuthStatus.LOADING -> {
                    }
                    AuthResource.AuthStatus.AUTHENTICATED -> {
                        Log.d(TAG, "onChanged: Login Success :${it.data?.userData?.name}")
                    }
                    AuthResource.AuthStatus.ERROR -> {
                        Log.d(TAG, "onChanged: Error :${it.message}")
                    }
                    AuthResource.AuthStatus.NOT_AUTHENTICATED -> {
                        navigateToLogin()
                    }
                    AuthResource.AuthStatus.LOGGING_OUT -> {
                        navigateToLoginByLoggingout()
                    }
                }
            }
        })
    }

    private fun navigateToLoginByLoggingout() {
        val intent: Intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Alert")
        builder.setCancelable(false)
        builder.setMessage("Session Timeout, Hit Ok to login again")
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Ok") { dialogInterface, i ->
            dialogInterface.cancel()
            val intent: Intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()

        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.setOnKeyListener { dialogInterface, i, keyEvent ->
            if (i == KeyEvent.KEYCODE_BACK) {
                val intent: Intent = Intent(this, AuthActivity::class.java)
//                alertDialog.dismiss()
                dialogInterface.cancel()
                startActivity(intent)
                finish()
            }
            true
        }
        alertDialog.show()
//        val intent: Intent =Intent(this,AuthActivity::class.java)
//        startActivity(intent)
//        finish()
    }


}