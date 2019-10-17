package projects.vikky.com.dhyan.models.chart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChartDM(
    @SerializedName("field1")
    @Expose
    var field0: Int? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("hour")
    @Expose
    var hour: Int? = null,

    @SerializedName("total_count")
    @Expose
    var total_count: Int? = null,

    @SerializedName("machine_name")
    @Expose
    var machineName: String? = null
)