package projects.vikky.com.dhyan.models.machine_list

import androidx.annotation.Nullable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import projects.vikky.com.dhyan.models.Meta

data class MachineResponseDM(
    @SerializedName("data")
    @Nullable
    @Expose
    var machineList: List<Machine>?=null,

    @SerializedName("meta")
    @Expose
    var meta: Meta?=null
)