package projects.vikky.com.dhyan.network.auth

import com.google.gson.JsonObject
import io.reactivex.Flowable
import projects.vikky.com.dhyan.models.User
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("login")
    fun getUser(@Body jsonObject: JsonObject): Flowable<User>

}