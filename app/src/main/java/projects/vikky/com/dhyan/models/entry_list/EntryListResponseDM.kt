package projects.vikky.com.dhyan.models.entry_list

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import projects.vikky.com.dhyan.models.Meta

data class EntryListResponseDM(
    @SerializedName("data")
    @Expose
    var entryList: List<EntryDM>? = null,

    @SerializedName("meta")
    @Expose
    var meta: Meta? = null
)