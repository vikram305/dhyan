package projects.vikky.com.dhyan.network.main

import io.reactivex.Flowable
import projects.vikky.com.dhyan.models.Post
import projects.vikky.com.dhyan.models.chart.ChartListResponseDM
import projects.vikky.com.dhyan.models.entry_list.EntryListResponseDM
import projects.vikky.com.dhyan.models.machine_count.MachineCount
import projects.vikky.com.dhyan.models.machine_list.MachineResponseDM
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface MainApi {

    //posts?userId=1
    @GET("posts")
    fun getPostFromUser(@Query("userId") id: Int): Flowable<List<Post>>


    @Headers(
        "Accept:application/json"
    )
    @GET("machine-count")
    fun getMachineCount(@Header("Authorization") authToken: String): Flowable<MachineCount>

    @Headers(
        "Accept:application/json"
    )
    @GET("machine-list")
    fun getMachineList(@Header("Authorization") authToken: String): Flowable<MachineResponseDM>

    @Headers(
        "Accept:application/json"
    )
    @GET("entry-list")
    fun getEntrieList(
        @Header("Authorization") authToken: String, @Query("m") machineId: Int, @Query(
            "d"
        ) date: String
    ): Flowable<EntryListResponseDM>

    @GET("chart-list")
    fun getChartList(
        @Header("Authorization") authToken: String, @Query("m") machineId: Int, @Query(
            "d"
        ) date: String
    ): Flowable<ChartListResponseDM>
}