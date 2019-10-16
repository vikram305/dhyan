package projects.vikky.com.dhyan.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MachineCount(
    @SerializedName("data")
    @Expose
    var machineCount: MachineCount,

    @SerializedName("meta")
    @Expose
    var meta: Meta?=null

)
