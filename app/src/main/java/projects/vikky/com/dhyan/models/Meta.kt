package projects.vikky.com.dhyan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Meta(

    @SerializedName("token")
    @Expose
    var token: String? = null,

    @SerializedName("code")
    @Expose
    var code: Int? = null,

    @SerializedName("message")
    @Expose
    var message: String? = null

)