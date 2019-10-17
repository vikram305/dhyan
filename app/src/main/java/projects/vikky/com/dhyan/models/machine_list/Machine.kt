package projects.vikky.com.dhyan.models.machine_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Machine(
    @SerializedName("machine_id")
    @Expose
    var machineId: Int? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("added_at")
    @Expose
    var addedDate: String? = null
)