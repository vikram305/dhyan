package projects.vikky.com.dhyan.ui.main.machine

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.models.machine_list.Machine

class MachineRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "MachineRecyclerAdapter"

    private var machines: List<Machine> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.machine_list_item, parent, false)
        return MachineViewHoder(view)
    }

    override fun getItemCount(): Int {
        return machines.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MachineViewHoder).bind(machines[position])
    }

    fun setPosts(machineList: List<Machine>) {
        this.machines = machineList
        Log.d(TAG, "machine list size:${machines.size}")
        notifyDataSetChanged()
    }

    class MachineViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var machineName: TextView
        lateinit var addedDate: TextView

        init {
            machineName = itemView.findViewById(R.id.machineNameTV)
            addedDate = itemView.findViewById(R.id.addedDateTV)
        }

        fun bind(machine: Machine) {
            machineName.text = "Machine Name: ${machine.name}"
            addedDate.text = "Added At: ${machine.addedDate}"
        }
    }
}