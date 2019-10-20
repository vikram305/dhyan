package projects.vikky.com.dhyan.ui.main.chart

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_entries.*
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.machine_list.Machine
import projects.vikky.com.dhyan.ui.main.Resource
import projects.vikky.com.dhyan.viewmodels.ViewModelProviderFactory
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class ChartFragment : DaggerFragment(), DatePicker.OnDateChangedListener, View.OnClickListener {
    private val TAG: String = "ChartFragment"

    private lateinit var viewModel: ChartViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var machineSpinner: Spinner
    //    private lateinit var datePicker: DatePicker
    private lateinit var datePickerEditText: EditText
    private lateinit var findButton: Button
    private lateinit var emptyTextView: TextView
    private lateinit var selectedDate: String
    private var selectedMachineId: Int = -1
    private var machineList: List<Machine> = ArrayList<Machine>()
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    lateinit var sessionManager: SessionManager

    private lateinit var fragmentView: View

    val myCalander: Calendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        return inflater.inflate(R.layout.fragment_entries,container,false)
        fragmentView = inflater.inflate(R.layout.fragment_entries, container, false)
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.entries_progress_bar)
        machineSpinner = view.findViewById(R.id.machineListSpinner)
        datePickerEditText = view.findViewById(R.id.datePickerEditText)
        findButton = view.findViewById(R.id.findButton)
        emptyTextView = view.findViewById(R.id.emptyText)
        myCalander.time = Date(System.currentTimeMillis())
//        datePickerEditText.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
        datePickerEditText.setText(SimpleDateFormat("dd/MM/yyyy").format(myCalander.time))
        Log.d(TAG, "in start edit text data :${datePickerEditText.text} ");
        selectedDate = SimpleDateFormat("MM/dd/yyyy").format(myCalander.time)
        Log.d(TAG, "in start selected date data:${selectedDate} ")

        findButton.setOnClickListener(this)
        findButton.isEnabled = false

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
                Log.d(TAG, "in dialog edit text data :${datePickerEditText.text} ")
                Log.d(TAG, "in dialog selected date data:${selectedDate} ")
            }
        datePickerEditText.setOnClickListener {
            DatePickerDialog(
                activity, date,
                myCalander.get(Calendar.YEAR),
                myCalander.get(Calendar.MONTH),
                myCalander.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        viewModel = ViewModelProviders.of(this, providerFactory).get(ChartViewModel::class.java)

        subscribeObserver()
    }



    private fun subscribeObserver() {
        viewModel.observeMachineList()?.removeObservers(viewLifecycleOwner)
        viewModel.observeMachineList()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        Log.d(TAG, "onChanged: Loading ")
                        showErrorText(false)
                        showProgressbar(true)
                    }
                    Resource.Status.SUCCESS -> {
                        Log.d(TAG, "onChanged: got machines ${it.data?.size}")
                        showProgressbar(false)
                        if (it.data!!.isEmpty()) {
                            emptyTextView.text = "Data Not Found "
                            showErrorText(true)
//                            emptyTextView.visibility=View.VISIBLE
//                            recyclerView.visibility=View.GONE

                        } else {
                            showErrorText(false)
                            machineList = it.data!!
                            setSpinnerData()

//                            emptyTextView.visibility=View.GONE
//                            recyclerView.visibility=View.VISIBLE
                        }
                    }
                    Resource.Status.ERROR -> {
                        Log.d(TAG, "onChanged: Error:${it.message} ")
                        emptyTextView.text = it.message

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
            findButton.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            findButton.isEnabled = true
        }
    }

    private fun showErrorText(isShowing: Boolean) {
        if (isShowing) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.GONE
        }
    }

    override fun onDateChanged(p0: DatePicker?, year: Int, month: Int, day: Int) {
        selectedDate = "${month}/${day}/${year}"
        Log.d(TAG, "selected Data:${selectedDate}")
    }


    override fun onClick(v: View?) {

        when (v?.id) {
            R.id.findButton -> {


//                selectedDate = datePickerEditText.text.toString()
                selectedMachineId = machineList[machineSpinner.selectedItemPosition].machineId!!
                if (selectedMachineId != -1 && !TextUtils.isEmpty(selectedDate)) {
//                    datePickerEditText.setText(SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis()))
//                    var day:Int=SimpleDateFormat("dd").format(System.currentTimeMillis()).toInt()
//                    var month:Int=SimpleDateFormat("MM").format(System.currentTimeMillis()).toInt()
//                    var year:Int=SimpleDateFormat("yyyy").format(System.currentTimeMillis()).toInt()
//                    Log.d(TAG, "data :${day}/${month}/${year} ")
//                    SimpleDateFormat("dd/MM/yyyy").format(System.currentTimeMillis())


                    Log.d(TAG, "data :${myCalander.time} ")
                    Log.d(TAG, "in click edit text date data:${datePickerEditText.text} ")
                    Log.d(TAG, "in click selected date data:${selectedDate} ")
//                    viewModel.observeEntryList(selectedMachineId,selectedDate)
                    Log.d(TAG, "in button click")
                    val bundle = Bundle()
                    bundle.putString("date", selectedDate)
                    bundle.putString("machineId", "${selectedMachineId}")
                    Navigation.findNavController(fragmentView)
                        .navigate(R.id.action_chartFragment_to_displayFragment, bundle)
                    myCalander.time = Date(System.currentTimeMillis())
                    datePickerEditText.setText(SimpleDateFormat("dd/MM/yyyy").format(myCalander.time))
                    selectedDate = SimpleDateFormat("MM/dd/yyyy").format(myCalander.time)
                }
            }
        }

    }


}