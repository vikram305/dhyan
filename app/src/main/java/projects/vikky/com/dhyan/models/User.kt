package projects.vikky.com.dhyan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("data")
    @Expose
    var userData: UserData? = null,

    @SerializedName("meta")
    @Expose
    var meta: Meta? = null
)

