package projects.vikky.com.dhyan.ui.main.entries

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_entries.*
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.entry_list.EntryDM
import projects.vikky.com.dhyan.models.machine_list.Machine
import projects.vikky.com.dhyan.ui.main.Resource
import projects.vikky.com.dhyan.viewmodels.ViewModelProviderFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class EntriesFragment : DaggerFragment(), DatePicker.OnDateChangedListener, View.OnClickListener {

    private val TAG: String = "EntriesFragment"

    private lateinit var viewModel: EntriesViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var machineSpinner: Spinner
    //    private lateinit var datePicker: DatePicker
    private lateinit var datePickerEditText: EditText
    private lateinit var findButton: Button
    private lateinit var selectedDate: String
    private var selectedMachineId: Int = -1
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView

    private var machineList: List<Machine> = ArrayList<Machine>()
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var entryRecyclerAdapter: EntryRecyclerAdapter

    val myCalander: Calendar = Calendar.getInstance()

    private var entries: LiveData<Resource<List<EntryDM>>>? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_entries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.entryRecyclerView)
        progressBar = view.findViewById(R.id.entries_progress_bar)
        machineSpinner = view.findViewById(R.id.machineListSpinner)
//        datePicker=view.findViewById(R.id.datePicker)
        datePickerEditText = view.findViewById(R.id.datePickerEditText)
        findButton = view.findViewById(R.id.findButton)
        emptyTextView = view.findViewById(R.id.emptyText)
        datePickerEditText.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
        Log.d(TAG, "data :${datePickerEditText.text} ");
        selectedDate = SimpleDateFormat("MM/dd/yyyy").format(System.currentTimeMillis())
        findButton.setOnClickListener(this)

        machineSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedMachineId = machineList[position].machineId!!
            }

        }

        var date: DatePickerDialog.OnDateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                myCalander.set(Calendar.YEAR, year)
                myCalander.set(Calendar.MONTH, monthOfYear)
                myCalander.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "MM/dd/yyyy" // mention the format you need
                val displayFormat = "dd/MM/yyyy"
                val ddf = SimpleDateFormat(displayFormat)
                val sdf = SimpleDateFormat(myFormat)
                datePickerEditText.setText(ddf.format(myCalander.time))
                selectedDate = sdf.format(myCalander.time)
                Log.d(TAG, "data :${datePickerEditText.text} ");
            }
        datePickerEditText.setOnClickListener {
            DatePickerDialog(
                activity, date,
                myCalander.get(Calendar.YEAR),
                myCalander.get(Calendar.MONTH),
                myCalander.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        viewModel = ViewModelProviders.of(this, providerFactory).get(EntriesViewModel::class.java)
        entries = viewModel.getEntriesLiveData()
        entries?.observe(viewLifecycleOwner, Observer {
            subscribeEntryObserver(it)
        })

//        datePicker.setOnDateChangedListener(this)
        initRecyclerView()
        subscribeObserver()
//        subscribeEntryObserver()
    }

    private fun subscribeEntryObserver(it: Resource<List<EntryDM>>) {
//        viewModel.observeEntryList(selectedMachineId,selectedDate)?.removeObservers(viewLifecycleOwner)
//        viewModel.observeEntryList(selectedMachineId,selectedDate)?.observe(viewLifecycleOwner, Observer {
        if (it != null) {
            when (it.status) {
                Resource.Status.LOADING -> {
                    Log.d(TAG, "onChanged: Loading ")
                    showList(false)
                    showErrorText(false)
                    showProgressbar(true)
                }
                Resource.Status.SUCCESS -> {
                    Log.d(TAG, "onChanged: got entries ${it.data?.size}")
                    showProgressbar(false)
                    if (it.data!!.isEmpty()) {
                        emptyTextView.text = "Machine list is empty"
                        showErrorText(true)
                        showList(false)
//                            emptyTextView.visibility=View.VISIBLE
//                            recyclerView.visibility=View.GONE

                    } else {
                        entryRecyclerAdapter.setPosts(it.data!!)
                        showErrorText(false)
                        showList(true)
//                            emptyTextView.visibility=View.GONE
//                            recyclerView.visibility=View.VISIBLE
                    }

//                        entryRecyclerAdapter.setPosts(it.data!!)
                }
                Resource.Status.ERROR -> {
                    Log.d(TAG, "onChanged: Error:${it.message} ")
                    emptyTextView.text = it.message

                    showList(false)
                    showErrorText(true)
                    showProgressbar(false)
                }
                Resource.Status.NOT_AUTHENTICATED -> {
//                        Toast.makeText(activity,"${it.message}",Toast.LENGTH_SHORT).show()
                    showProgressbar(false)
                    sessionManager.logout()
                }
            }
        }
//        })
    }

    private fun subscribeObserver() {
        viewModel.observeMachineList()?.removeObservers(viewLifecycleOwner)
        viewModel.observeMachineList()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        Log.d(TAG, "onChanged: Loading ")
                        showList(false)
                        showErrorText(false)
                        showProgressbar(true)
                    }
                    Resource.Status.SUCCESS -> {
                        Log.d(TAG, "onChanged: got machines ${it.data?.size}")
                        showProgressbar(false)
                        if (it.data!!.isEmpty()) {
                            emptyTextView.text = "Data Not Found"
                            showErrorText(true)
                            showList(false)
//                            emptyTextView.visibility=View.VISIBLE
//                            recyclerView.visibility=View.GONE

                        } else {
                            showErrorText(false)
                            showList(false)
                            machineList = it.data!!
                            setSpinnerData()

//                            emptyTextView.visibility=View.GONE
//                            recyclerView.visibility=View.VISIBLE
                        }


//                        machineRecyclerAdapter.setPosts(it.data!!)
                    }
                    Resource.Status.ERROR -> {
                        Log.d(TAG, "onChanged: Error:${it.message} ")
                        emptyTextView.text = it.message

                        showList(false)
                        showErrorText(true)
                        showProgressbar(false)
                    }
                    Resource.Status.NOT_AUTHENTICATED -> {
//                        Toast.makeText(activity,"${it.message}", Toast.LENGTH_SHORT).show()
                        showProgressbar(false)
                        sessionManager.logout()
                    }
                }
            }
        })
    }

    private fun setSpinnerData() {
        val list = ArrayList<String>()
        if (machineList.isEmpty()) {
            list.add("Select Machine")
            //todo: disable find button
        }
        for (machine in machineList) {
            list.add(machine.name!!)
        }

        var arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, list)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        machineListSpinner.adapter = arrayAdapter
    }

    private fun showProgressbar(isShowing: Boolean) {
        if (isShowing) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun showErrorText(isShowing: Boolean) {
        if (isShowing) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
    }

    private fun showList(isShowing: Boolean) {
        if (isShowing) {
            recyclerView.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.GONE
        }
    }

    override fun onDateChanged(p0: DatePicker?, year: Int, month: Int, day: Int) {
        selectedDate = "${month}/${day}/${year}"
        Log.d(TAG, "selected Data:${selectedDate}")
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = entryRecyclerAdapter
    }

    override fun onClick(view: View?) {

        when (view?.id) {
            R.id.findButton -> {
//                selectedDate = datePickerEditText.text.toString()
                if (machineList.isEmpty()) {
                    selectedMachineId = -1
                } else {
                    selectedMachineId = machineList[machineSpinner.selectedItemPosition].machineId!!
                }

                if (selectedMachineId != -1 && !TextUtils.isEmpty(selectedDate)) {
                    viewModel.observeEntryList(selectedMachineId, selectedDate)
                }
            }
        }

    }


}
