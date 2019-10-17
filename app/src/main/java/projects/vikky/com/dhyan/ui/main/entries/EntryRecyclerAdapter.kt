package projects.vikky.com.dhyan.ui.main.entries

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.models.entry_list.EntryDM

class EntryRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG: String = "EntryRecyclerAdapter"

    private var entries: List<EntryDM> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.entry_list_item, parent, false)
        return EntryViewHoder(view)
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as EntryViewHoder).bind(entries[position])
    }

    fun setPosts(entryList: List<EntryDM>) {
        this.entries = entryList
        Log.d(TAG, "entry list size:${entries.size}")
        notifyDataSetChanged()
    }

    class EntryViewHoder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var machineName: TextView
        lateinit var addedDate: TextView
        lateinit var entryTV: TextView
        init {
            machineName=itemView.findViewById(R.id.machineNameTV)
            addedDate=itemView.findViewById(R.id.addedDateTV)
            entryTV = itemView.findViewById(R.id.entryTV)
        }

        fun bind(entry: EntryDM) {
            machineName.text = "Machine Name: ${entry.machineName}"
            addedDate.text = "Added At: ${entry.addedDate}"
            entryTV.text = "Entry: ${entry.entry}"
            //todo set entry text
        }
    }
}