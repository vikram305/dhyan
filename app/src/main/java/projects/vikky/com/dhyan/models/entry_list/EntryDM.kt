package projects.vikky.com.dhyan.models.entry_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class EntryDM(
    @SerializedName("entry_id")
    @Expose
    var entryId: Int? = null,

    @SerializedName("machine_name")
    @Expose
    var machineName: String? = null,

    @SerializedName("entry")
    @Expose
    var entry: Int? = null,

    @SerializedName("added_at")
    @Expose
    var addedDate: String? = null
)