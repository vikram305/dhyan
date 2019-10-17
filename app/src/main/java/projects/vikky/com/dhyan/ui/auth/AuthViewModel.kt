package projects.vikky.com.dhyan.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams.fromPublisher
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import io.reactivex.schedulers.Schedulers
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.User
import projects.vikky.com.dhyan.models.UserData
import projects.vikky.com.dhyan.network.auth.AuthApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthViewModel @Inject constructor(var authApi: AuthApi, var sessionManager: SessionManager) :
    ViewModel() {

    private val TAG: String = "AuthViewModel"

//    private var authUser: MediatorLiveData<AuthResource<User>> = MediatorLiveData()
//    private var authUser: MediatorLiveData<User> = MediatorLiveData()

    init {
        Log.d(TAG, "AuthViewModel:viewModel is working")

    }

    fun authenticate(jsonObject: JsonObject) {
        sessionManager.authenticate(queryUserId(jsonObject))
    }

    fun authenticateWithId(userId: Int) {
        Log.d(TAG, "attempting to login with user id${userId} ")
//        sessionManager.authenticateWithId(queryUserId(userId))
//        authUser.setValue(AuthResource.loading(null))
//
//        val source: LiveData<AuthResource<out User>> = fromPublisher(
//            authApi.getUser(userId).subscribeOn(Schedulers.io())
//                .delay(2000,TimeUnit.MILLISECONDS)
//
//                .onErrorReturn {
//                    val user = User(-1)
//                    Log.d(TAG, "error in calling ")
//                    user
//                }.map {
//                    if (it.id == -1) {
//                        AuthResource.error("could not authenticated", null)
//                    } else {
//                        AuthResource.authenticated(it)
//                    }
//                }
//        )
//
//        authUser.addSource(source, Observer {
//            authUser.setValue(it as AuthResource<User>?)
//            authUser.removeSource(source)
//        })
    }

//    private fun queryUserId(userId:Int):LiveData<AuthResource<out User>>{
//        return fromPublisher(
//            authApi.getUser(userId).subscribeOn(Schedulers.io())
//                .delay(100,TimeUnit.MILLISECONDS)
//
//                .onErrorReturn {
//                    val user = User(-1)
//                    Log.d(TAG, "error in calling ")
//                    user
//                }.map {
//                    if (it.id == -1) {
//                        AuthResource.error("could not authenticated", null)
//                    } else {
//                        AuthResource.authenticated(it)
//                    }
//                }
//        )
//    }

    private fun queryUserId(jsonObject: JsonObject): LiveData<AuthResource<out User>> {
        return fromPublisher(
            authApi.getUser(jsonObject).subscribeOn(Schedulers.io())
                .delay(100, TimeUnit.MILLISECONDS)
                .onErrorReturn {
                    val user = User(UserData(-1), null)
                    Log.d(TAG, "error in network ${it.message}")
                    user
                }.map {
                    if (it.userData?.userId == -1) {
                        AuthResource.error(
                            "Something went wrong please check your internet connection ",
                            null
                        )
                    } else if (it.meta?.code == 0) {
                        AuthResource.error("${it.meta?.message}", null)
                    } else {
                        AuthResource.authenticated(it)
                    }
                }
        )
    }

    fun observeAuthState(): LiveData<AuthResource<User>> {
        return sessionManager.getAuthUser()
    }
}