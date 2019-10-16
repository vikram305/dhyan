package projects.vikky.com.dhyan.models.machine_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import projects.vikky.com.dhyan.models.Meta
import projects.vikky.com.dhyan.models.UserData

data class MachineList(
    @SerializedName("data")
    @Expose
    var machineList: List<Machine>?=null,

    @SerializedName("meta")
    @Expose
    var meta: Meta?=null
)