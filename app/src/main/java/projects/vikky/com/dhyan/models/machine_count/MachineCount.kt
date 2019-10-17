package projects.vikky.com.dhyan.models.machine_count

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import projects.vikky.com.dhyan.models.Meta

data class MachineCount(
    @SerializedName("data")
    @Expose
    var machineCount: Count,

    @SerializedName("meta")
    @Expose
    var meta: Meta?=null

)
