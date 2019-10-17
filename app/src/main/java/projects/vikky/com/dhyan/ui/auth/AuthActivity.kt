package projects.vikky.com.dhyan.ui.auth

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.RequestManager
import com.google.gson.JsonObject
import dagger.android.support.DaggerAppCompatActivity
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.ui.main.MainAcitvity
import javax.inject.Inject

class AuthActivity : DaggerAppCompatActivity(), View.OnClickListener {

    val TAG: String = "AuthActivity"

    private lateinit var userIdET: EditText
    private lateinit var passwordET: EditText
    private lateinit var loginBT: Button
    private lateinit var progressBar: ProgressBar


    @Inject
    lateinit var viewModelProviderFactory: ViewModelProvider.Factory
    private lateinit var viewModel: AuthViewModel

    @Inject
    lateinit var logo: Drawable

    @Inject
    lateinit var requestManager: RequestManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        userIdET = findViewById(R.id.input_email)
        passwordET = findViewById(R.id.input_password)
        loginBT = findViewById(R.id.loginBT)
        progressBar = findViewById(R.id.progress_bar)

        loginBT.setOnClickListener(this)

        viewModel =
            ViewModelProviders.of(this, viewModelProviderFactory).get(AuthViewModel::class.java)
//        setLogo()

        subscribeObservers()

    }

    private fun subscribeObservers() {
        viewModel.observeAuthState().observe(this, Observer {
            if (it != null) {
                Log.d(TAG, "onChanged: ${it.data?.userData?.email}")
                when (it.status) {
                    AuthResource.AuthStatus.LOADING -> showProgressbar(true)
                    AuthResource.AuthStatus.AUTHENTICATED -> {
                        showProgressbar(false)
                        Log.d(TAG, "onChanged : Login Success:${it.data?.userData?.email}")
                        onLoginSuccess()
                    }
                    AuthResource.AuthStatus.ERROR -> {
                        showProgressbar(false)
                        Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
                    AuthResource.AuthStatus.NOT_AUTHENTICATED -> showProgressbar(false)
                }
            }
        })
    }

    private fun onLoginSuccess() {

        val intent: Intent = Intent(this, MainAcitvity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showProgressbar(isShowing: Boolean) {
        if (isShowing) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun setLogo() {
        requestManager.load(logo).into(findViewById(R.id.login_logo))
    }

    override fun onClick(v: View?) {
        when (v?.getId()) {
            R.id.loginBT -> attemptLogin()
        }
    }

    private fun attemptLogin() {
        if (TextUtils.isEmpty(userIdET.text.toString()) && TextUtils.isEmpty(passwordET.text.toString())) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
//            setEditTextError(userIdET,"Please Enter Email Id")
//            setEditTextError(passwordET,"Please Enter Password")
            return
        } else if (TextUtils.isEmpty(userIdET.text.toString())) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
//            setEditTextError(userIdET,"Please Enter Email Id")
            return
        } else if (TextUtils.isEmpty(passwordET.text.toString())) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
//            setEditTextError(passwordET,"Please Enter Password")
            return
        }
        var jsonObject: JsonObject = JsonObject()
        jsonObject.addProperty("email", userIdET.text.toString())
        jsonObject.addProperty("password", passwordET.text.toString())
//        viewModel.authenticateWithId(userIdET.text.toString().toInt())
        viewModel.authenticate(jsonObject)
    }

    private fun setEditTextError(editText: EditText, msg: String) {
        editText.error = msg
    }
}
