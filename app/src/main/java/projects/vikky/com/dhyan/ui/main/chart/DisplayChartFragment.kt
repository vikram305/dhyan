package projects.vikky.com.dhyan.ui.main.chart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.android.support.DaggerFragment
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.chart.ChartDM
import projects.vikky.com.dhyan.ui.main.Resource
import projects.vikky.com.dhyan.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class DisplayChartFragment : DaggerFragment() {

    private val TAG: String = "DisplayChartFragment"

    private lateinit var viewModel: DisplayChartViewModel
    //    private lateinit var horizontalBarChart: HorizontalBarChart
    private lateinit var horizontalBarChart: BarChart
    private lateinit var totalCountText: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorTextView: TextView

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    lateinit var sessionManager: SessionManager

    private var selectedMachineId: Int = -1
    private var selectedDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_display_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        horizontalBarChart = view.findViewById(R.id.horizontal_chart)
        totalCountText = view.findViewById(R.id.totalText)
        errorTextView = view.findViewById(R.id.errorText)
        progressBar = view.findViewById(R.id.chart_progress_bar)
        viewModel =
            ViewModelProviders.of(this, providerFactory).get(DisplayChartViewModel::class.java)
        Log.d(TAG, "date :${arguments?.getString("date")} ")
        Log.d(TAG, "machineId :${arguments?.getString("machineId")} ")
        selectedDate = arguments?.getString("date")!!
        selectedMachineId = arguments?.getString("machineId")!!.toInt()

        subscribeEntryObserver()
    }

    private fun subscribeEntryObserver() {
        viewModel.observeEntryList(selectedMachineId, selectedDate)
            ?.removeObservers(viewLifecycleOwner)
        viewModel.observeEntryList(selectedMachineId, selectedDate)
            ?.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    when (it.status) {
                        Resource.Status.LOADING -> {
                            Log.d(TAG, "onChanged: Loading ")
                            showErrorText(false)
                            showProgressbar(true)
                            showChart(false)
                            showMachineText(false)
                        }
                        Resource.Status.SUCCESS -> {
                            Log.d(TAG, "onChanged: got entries ${it.data?.size}")
                            showProgressbar(false)
                            if (it.data!!.isEmpty()) {
                                errorTextView.text = "Data Not Found "
                                showErrorText(true)
                                showChart(false)
                                showMachineText(false)
//                            emptyTextView.visibility=View.VISIBLE
//                            recyclerView.visibility=View.GONE

                            } else {
                                totalCountText.text =
                                    "Machine Name: ${it.data!![0].machineName}, Total Count:${it.data!![it.data.size - 1].total_count}"
                                showErrorText(false)
                                showChart(true)
                                showMachineText(true)
//                                initializeGraph(it.data!!)
                                createGraph(it.data!!)
                            }
//                        initializeGraph(it.data!!)
//                    entryRecyclerAdapter.setPosts(it.data!!)
//                        machineRecyclerAdapter.setPosts(it.data!!)
                        }
                        Resource.Status.ERROR -> {
                            Log.d(TAG, "onChanged: Error:${it.message} ")
                            errorTextView.text = it.message
                            showErrorText(true)
                            showMachineText(false)
                            showProgressbar(false)
                            showChart(false)
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

    private fun initializeGraph(data: List<ChartDM>) {
        var time: ArrayList<String> = ArrayList()
        for (i in 0..data.size - 1) {
            time.add("${data[i].hour}")
        }
        var barDataSet = BarDataSet(getData(data), "Count")
        barDataSet.setDrawValues(true)
        barDataSet.color = R.color.colorAccent
        var barData: BarData = BarData(barDataSet)
        var xAxis: XAxis = horizontalBarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = time.size
        var formatter: IndexAxisValueFormatter = IndexAxisValueFormatter(time)
        xAxis.granularity = 1f
        xAxis.valueFormatter = formatter
        var yAxis = horizontalBarChart.axisRight
        yAxis.axisMaximum = 6000f
        yAxis.axisMaximum = 0f
//        horizontalBarChart.xAxis.axisMinimum=0f
        horizontalBarChart.data = barData
        horizontalBarChart.setFitBars(true)

        horizontalBarChart.animateXY(2000, 2000)
        horizontalBarChart.description.isEnabled = false
        horizontalBarChart.legend.isEnabled = false
//        horizontalBarChart.setDrawValueAboveBar(true)

//        horizontalBarChart.getAxisLeft().setEnabled(false);
//        horizontalBarChart.getAxisRight().setEnabled(false);
        horizontalBarChart.invalidate()


    }

    private fun createGraph(data: List<ChartDM>) {

        var time: ArrayList<String> = ArrayList()
        for (i in 0..data.size - 1) {
            time.add("${data[i].hour}")
        }
        var barDataSet = BarDataSet(getData(data), "Count")
        barDataSet.setDrawValues(true)

        var barData = BarData(barDataSet)
        var xAxis: XAxis = horizontalBarChart.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.labelCount = time.size
        var formatter: IndexAxisValueFormatter = IndexAxisValueFormatter(time)
        xAxis.granularity = 1f
        xAxis.valueFormatter = formatter
        var yAxis = horizontalBarChart.axisRight
        yAxis.setDrawLabels(false)
        yAxis.axisMinimum = 0f

        horizontalBarChart.data = barData
        horizontalBarChart.setFitBars(false)
        horizontalBarChart.minimumWidth = 2

        horizontalBarChart.animateXY(1000, 1000)
        horizontalBarChart.description.isEnabled = false
        horizontalBarChart.legend.isEnabled = false
        horizontalBarChart.setDrawValueAboveBar(true)

        horizontalBarChart.invalidate()

    }


    private fun getData(entries: List<ChartDM>): ArrayList<BarEntry> {
        var barEntry: ArrayList<BarEntry> = ArrayList()
        for (i in 0..entries.size - 1) {
            Log.d(TAG, "value of i :${i} and data ${entries.get(i).hour} ")
            barEntry.add(BarEntry(i.toFloat(), entries[i].field0?.toFloat()!!))
        }


        return barEntry
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
            errorTextView.visibility = View.VISIBLE
        } else {
            errorTextView.visibility = View.GONE
        }
    }

    private fun showChart(isShowing: Boolean) {
        if (isShowing) {
            horizontalBarChart.visibility = View.VISIBLE
        } else {
            horizontalBarChart.visibility = View.GONE
        }
    }

    private fun showMachineText(isShowing: Boolean) {
        if (isShowing) {
            totalCountText.visibility = View.VISIBLE
        } else {
            totalCountText.visibility = View.GONE
        }
    }
}