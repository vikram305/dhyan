package projects.vikky.com.dhyan.models.chart

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import projects.vikky.com.dhyan.models.Meta

data class ChartListResponseDM(
    @SerializedName("data")
    @Expose
    var chartList: List<ChartDM>? = null,

    @SerializedName("meta")
    @Expose
    var meta: Meta? = null
)