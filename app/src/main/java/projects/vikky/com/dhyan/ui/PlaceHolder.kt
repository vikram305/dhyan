package projects.vikky.com.dhyan.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import dagger.android.support.DaggerAppCompatActivity
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.ui.auth.AuthActivity
import projects.vikky.com.dhyan.ui.auth.AuthResource
import projects.vikky.com.dhyan.ui.main.MainAcitvity
import javax.inject.Inject


class PlaceHolder : DaggerAppCompatActivity() {

    private val TAG: String = "PlaceHolder"

    @Inject
    lateinit var editor: SharedPreferences.Editor

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        Log.d(TAG, "hello ${sessionManager.getAuthUser().value?.status}")
        scheduleSplashScreen()

    }

    private fun scheduleSplashScreen() {
        val splashScreenDuration = 1500L
        Handler().postDelayed(
            {
                navigateTo()
                finish()
            },
            splashScreenDuration
        )
    }

    private fun navigateTo() {
        if (sessionManager.getAuthUser().value != null) {
            Log.d(TAG, "value is not null ")
            if (sessionManager.getAuthUser().value!!.status == AuthResource.AuthStatus.AUTHENTICATED) {
                Log.d(TAG, "authenticated already in session ");
                startActivity(Intent(this, MainAcitvity::class.java))
                finish()
            } else {
                Log.d(TAG, "not authenticated or error ");
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }

        } else {
            Log.d(TAG, "not authenticated ");
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}

