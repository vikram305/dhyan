package projects.vikky.com.dhyan.ui.main.chart

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.schedulers.Schedulers
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.machine_list.Machine
import projects.vikky.com.dhyan.models.machine_list.MachineResponseDM
import projects.vikky.com.dhyan.network.main.MainApi
import projects.vikky.com.dhyan.ui.main.Resource
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChartViewModel @Inject constructor(val sessionManager: SessionManager, val mainApi: MainApi) :
    ViewModel() {
    private val TAG: String = "ChartViewModel"
    private var machines: MediatorLiveData<Resource<List<Machine>>>? = null

    init {
        Log.d(TAG, "ChartViewModel working...")
    }

    fun observeMachineList(): LiveData<Resource<List<Machine>>>? {
        if (machines == null) {
            machines = MediatorLiveData()
            machines!!.value = Resource.loading(null)
        }
        val source: LiveData<Resource<out List<Machine>>> = LiveDataReactiveStreams.fromPublisher(
            mainApi.getMachineList("Bearer ${sessionManager.getAuthUser().value!!.data!!.meta!!.token!!}")
                .subscribeOn(Schedulers.io())
                .delay(1000, TimeUnit.MILLISECONDS)
                .onErrorReturn {
                    var machine = Machine()
                    Log.d(TAG, "apply: error ${it is UnknownHostException}")
                    if (it.message!!.contains("401")) {
                        machine.machineId = -401
                    } else {
                        machine.machineId = -1
                    }
//                    var machine=Machine(-1)
                    val listMachine = ArrayList<Machine>()
                    listMachine.add(machine)
                    MachineResponseDM(listMachine)
                }.map {
                    if (it.machineList?.size == 1) {
                        if (it.machineList?.get(0)?.machineId == -401) {
                            Resource.notauthenticated("Session Timeout", null)
                        } else if (it.machineList?.get(0)?.machineId == -1) {
                            Resource.error("Something went wrong please try after some time", null)
                        } else {
                            Resource.success(it.machineList)
                        }
                    } else {
                        Resource.success(it.machineList)
                    }
//                    if(it.machineList?.size!! > 0){
//                        if(it.machineList?.get(0)?.machineId==-1){
//                            Resource.error("Something went wrong please try after some time",null)
//                        } else{
//                            Resource.success(it.machineList)
//                        }
//                    }else{
//                        Resource.error("Session Timeout",null)
//                    }

                }

        )

        machines!!.addSource(source, Observer {
            machines!!.value = it as Resource<List<Machine>>
            machines!!.removeSource(source)
        })

        return machines
    }
}