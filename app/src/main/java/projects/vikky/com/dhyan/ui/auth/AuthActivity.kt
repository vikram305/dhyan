package projects.vikky.com.dhyan

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.RequestManager
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import kotlin.math.log

class AuthActivity : DaggerAppCompatActivity() {

    val TAG:String="AuthActivity"

    @Inject
    lateinit var logo:Drawable

    @Inject
    lateinit var requestManager: RequestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        setLogo()

    }

    private fun setLogo() {
        requestManager.load(logo).into(findViewById(R.id.login_logo))
    }
}
