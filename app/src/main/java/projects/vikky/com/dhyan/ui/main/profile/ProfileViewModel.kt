package projects.vikky.com.dhyan.ui.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.User
import projects.vikky.com.dhyan.ui.auth.AuthResource
import javax.inject.Inject

class ProfileViewModel @Inject constructor(val sessionManager: SessionManager) : ViewModel() {

    val TAG: String = "ProfileViewModel"


    init {

        Log.d(TAG, "${TAG}: viewmodel is ready")
    }

    fun getAuthenticatedUser(): LiveData<AuthResource<User>> {
        return sessionManager.getAuthUser()
    }
}