package projects.vikky.com.dhyan.models.machine_count

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Count(
    @SerializedName("total_count")
    @Expose
    var total_count: Int? = null
)