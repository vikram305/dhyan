package projects.vikky.com.dhyan.ui.main.entries

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.schedulers.Schedulers
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.entry_list.EntryDM
import projects.vikky.com.dhyan.models.entry_list.EntryListResponseDM
import projects.vikky.com.dhyan.models.machine_list.Machine
import projects.vikky.com.dhyan.models.machine_list.MachineResponseDM
import projects.vikky.com.dhyan.network.main.MainApi
import projects.vikky.com.dhyan.ui.main.Resource
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class EntriesViewModel @Inject constructor(
    val sessionManager: SessionManager,
    val mainApi: MainApi
) : ViewModel() {
    private val TAG: String = "EntriesViewModel"
    private var machines: MediatorLiveData<Resource<List<Machine>>>? = null
    private var entries: MediatorLiveData<Resource<List<EntryDM>>> = MediatorLiveData()

    init {
        Log.d(TAG, "EntriesViewModel working...")
    }

    fun observeMachineList(): LiveData<Resource<List<Machine>>>? {
        if (machines == null) {
            machines = MediatorLiveData()
            machines!!.value = Resource.loading(null)
        }
        val source: LiveData<Resource<out List<Machine>>> = LiveDataReactiveStreams.fromPublisher(
            mainApi.getMachineList("Bearer ${sessionManager.getAuthUser().value!!.data!!.meta!!.token!!}")
                .subscribeOn(Schedulers.io())
                .delay(1500, TimeUnit.MILLISECONDS)
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

    fun observeEntryList(machineId: Int, date: String): LiveData<Resource<List<EntryDM>>>? {
//        if(entries==null){
//            entries= MediatorLiveData()
//            entries!!.value= Resource.loading(null)
//        }
        entries.value = Resource.loading(null)
        val source: LiveData<Resource<out List<EntryDM>>> = LiveDataReactiveStreams.fromPublisher(
            mainApi.getEntrieList(
                "Bearer ${sessionManager.getAuthUser().value!!.data!!.meta!!.token!!}",
                machineId,
                date
            )
                .subscribeOn(Schedulers.io())
                .delay(1000, TimeUnit.MILLISECONDS)
                .onErrorReturn {
                    var entry = EntryDM()
                    Log.d(TAG, "apply: error ")
                    if (it.message!!.contains("401")) {
                        entry.entryId = -401
                    } else {
                        entry.entryId = -1
                    }
                    val listMachine = ArrayList<EntryDM>()
                    listMachine.add(entry)
                    EntryListResponseDM(listMachine)
                }.map {
                    if (it.entryList?.size == 1) {
                        if (it.entryList?.get(0)?.entryId == -401) {
                            Resource.notauthenticated("Session Timeout", null)
                        } else if (it.entryList?.get(0)?.entryId == -1) {
                            Resource.error("Something went wrong please try after some time", null)
                        } else {
                            Resource.success(it.entryList)
                        }
                    } else {
                        Resource.success(it.entryList)
                    }
//                    if(it.entryList?.size!! > 0){
//                        if(it.entryList?.get(0)?.entryId==-1){
//                            Resource.error("Something went wrong please try after some time",null)
//                        } else{
//                            Resource.success(it.entryList)
//                        }
//                    }else{
//                        Resource.error("Session Timeout",null)
//                    }

                }

        )

        entries!!.addSource(source, Observer {
            entries!!.value = it as Resource<List<EntryDM>>
            entries!!.removeSource(source)
        })

        return entries
    }

    fun getEntriesLiveData(): LiveData<Resource<List<EntryDM>>>? {
        return entries
    }
}