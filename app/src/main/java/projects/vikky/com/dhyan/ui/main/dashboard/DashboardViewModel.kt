package projects.vikky.com.dhyan.ui.main.dashboard

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.schedulers.Schedulers
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.machine_count.Count
import projects.vikky.com.dhyan.models.machine_count.MachineCount
import projects.vikky.com.dhyan.network.main.MainApi
import projects.vikky.com.dhyan.ui.main.Resource
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    val sessionManager: SessionManager,
    val mainApi: MainApi
) : ViewModel() {

    private val TAG: String = "DashboardViewModel"
    private var machineCount: MediatorLiveData<Resource<MachineCount>>? = null

    init {
        Log.d(TAG, "${TAG}: viewmolde is working ")
    }

    fun observeMachineCount(): LiveData<Resource<MachineCount>> {
        if (machineCount == null) {
            machineCount = MediatorLiveData()
            machineCount!!.value = Resource.loading(null)

            val source: LiveData<Resource<out MachineCount>> =
                LiveDataReactiveStreams.fromPublisher(
                    mainApi.getMachineCount("Bearer ${sessionManager.getAuthUser().value!!.data!!.meta!!.token!!}")
                        .delay(1000, TimeUnit.MILLISECONDS)
                        .onErrorReturn {
                            var count = Count()
                            Log.d(TAG, "apply: error ")
                            if (it.message!!.contains("401")) {
                                count.total_count = -401
                            } else {
                                count.total_count = -1
                            }
//
                            var machineCount = MachineCount(count)
                            machineCount
                        }.map {
                            if (it.machineCount.total_count == -1) {
                                Resource.error(
                                    "Something went wrong please try after some time",
                                    null
                                )
                            } else if (it.machineCount.total_count == -401) {
                                Resource.notauthenticated("Session Timeout", null)
                            }
//                        else if(it.meta?.code!=1){
//                            Resource.notauthenticated("Session Timedout",null)
//                        }
                            else {
                                Resource.success(it)
                            }

                        }.subscribeOn(Schedulers.io())
                )

            machineCount!!.addSource(source, Observer {
                machineCount!!.value = it as Resource<MachineCount>
                machineCount!!.removeSource(source)
            })
        }


        return machineCount!!
    }


}