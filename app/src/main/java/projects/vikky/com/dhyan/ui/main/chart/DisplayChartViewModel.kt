package projects.vikky.com.dhyan.ui.main.chart

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.schedulers.Schedulers
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.chart.ChartDM
import projects.vikky.com.dhyan.models.chart.ChartListResponseDM
import projects.vikky.com.dhyan.network.main.MainApi
import projects.vikky.com.dhyan.ui.main.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DisplayChartViewModel @Inject constructor(
    val sessionManager: SessionManager,
    val mainApi: MainApi
) : ViewModel() {
    private val TAG: String = "DisplayChartViewModel"

    private var entries: MediatorLiveData<Resource<List<ChartDM>>> = MediatorLiveData()

    init {
        Log.d(TAG, "${TAG} working...");
    }

    fun observeEntryList(machineId: Int, date: String): LiveData<Resource<List<ChartDM>>>? {
//        if(entries==null){
//            entries= MediatorLiveData()
//            entries!!.value= Resource.loading(null)
//        }
        val source: LiveData<Resource<out List<ChartDM>>> = LiveDataReactiveStreams.fromPublisher(
            mainApi.getChartList(
                "Bearer ${sessionManager.getAuthUser().value!!.data!!.meta!!.token!!}",
                machineId,
                date
            )
                .subscribeOn(Schedulers.io())
                .delay(500, TimeUnit.MILLISECONDS)
                .onErrorReturn {
                    Log.d(TAG, "apply: error ")
                    var chart = ChartDM()
                    if (it.message!!.contains("401")) {
                        chart.field0 = -401
                    } else {
                        chart.field0 = -1
                    }
                    val listChart = ArrayList<ChartDM>()
                    listChart.add(chart)
                    ChartListResponseDM(listChart)
                }.map {

                    if (it.chartList?.size == 1) {
                        if (it.chartList?.get(0)?.field0 == -401) {
                            Resource.notauthenticated("Session Timeout", null)
                        } else if (it.chartList?.get(0)?.field0 == -1) {
                            Resource.error("Something went wrong please try after some time", null)
                        } else {
                            Resource.success(it.chartList)
                        }
                    } else {
                        Resource.success(it.chartList)
                    }
//                    if(it.chartList?.size!! > 0){
//                        if(it.chartList?.get(0)?.field0==-1){
//                            Resource.error("Something went wrong please try after some time",null)
//                        } else{
//                            Resource.success(it.chartList)
//                        }
//                    }else{
//                        Resource.error("Session Timeout",null)
//                    }

                }

        )

        entries!!.addSource(source, Observer {
            entries!!.value = it as Resource<List<ChartDM>>
            entries!!.removeSource(source)
        })

        return entries
    }

    fun getEntriesLiveData(): LiveData<Resource<List<ChartDM>>>? {
        return entries
    }
}