package projects.vikky.com.dhyan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Data(

    @SerializedName("userId")
    @Expose
    var userId:Int?=null,

    @SerializedName("name")
    @Expose
    val name:String?=null,

    @SerializedName("email")
    @Expose
    val email:String?=null,

    @SerializedName("mobile")
    @Expose
    val mobile:Long?=null
)

