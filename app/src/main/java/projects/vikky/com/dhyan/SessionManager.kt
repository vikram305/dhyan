package projects.vikky.com.dhyan

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import projects.vikky.com.dhyan.models.User
import projects.vikky.com.dhyan.ui.auth.AuthResource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(val sharedPreferences: SharedPreferences) {
    private val TAG: String = "SessionManager"

    private var cachedUser: MediatorLiveData<AuthResource<User>> = MediatorLiveData()

//    @Inject
//    lateinit var sharedPreferences: SharedPreferences

    lateinit var editor: SharedPreferences.Editor

    init {
        var gson = Gson()
//        sharedPreferences=PreferenceManager.
        var data: String = sharedPreferences.getString("user", "")
        if (data != null) {
            Log.d(TAG, "")
            cachedUser.value = fromJson(data)
            if (cachedUser.value?.status == AuthResource.AuthStatus.ERROR) {
                cachedUser.value = AuthResource.logout()
            }
            Log.d(TAG, "${cachedUser.value?.status}")
        } else {
            cachedUser.value = AuthResource.logout()
        }
    }

    fun authenticate(source: LiveData<AuthResource<out User>>) {
        editor = sharedPreferences.edit()
        if (cachedUser != null) {
            cachedUser.value = AuthResource.loading(null)
            cachedUser.addSource(source, Observer {
                cachedUser.value = it as AuthResource<User>
                if (cachedUser.value?.status == AuthResource.AuthStatus.ERROR) {
                    cachedUser.value = AuthResource.logout()
                }
                var gson: Gson = Gson()
                var userString: String = gson.toJson(cachedUser.value)
                editor.putString("user", userString)
                editor.apply()
                editor.commit()
                cachedUser.removeSource(source)
            })
        }
    }

    fun authenticateWithId(source: LiveData<AuthResource<out User>>) {
        editor = sharedPreferences.edit()
        if (cachedUser != null) {
            cachedUser.value = AuthResource.loading(null)
            cachedUser.addSource(source, Observer {
                cachedUser.value = it as AuthResource<User>
                var gson: Gson = Gson()
                var userString: String = gson.toJson(cachedUser.value)
                editor.putString("user", userString)
                editor.apply()
                editor.commit()
                cachedUser.removeSource(source)
            })
        }
    }

    fun logout(loggingout: Boolean = false) {
        Log.d(TAG, "logout ")
        editor = sharedPreferences.edit()
        editor.remove("user")
        editor.commit()
        editor.apply()
        if (loggingout) {
            cachedUser.value = AuthResource.loggingout()
        } else {
            cachedUser.value = AuthResource.logout()
        }

    }


    fun getAuthUser(): LiveData<AuthResource<User>> {
//        editor=sharedPreferences.edit()
//        var gson=Gson()
//        var userData:String=sharedPreferences.getString("user","")
////        cachedUser.value=gson.fromJson("user",)
////        cachedUser.value=fromJson(userData)
//        return fromJson(userData)
        return cachedUser
    }

    inline fun <reified T> fromJson(json: String): T {
        return Gson().fromJson(json, object : TypeToken<T>() {}.type)
    }
}