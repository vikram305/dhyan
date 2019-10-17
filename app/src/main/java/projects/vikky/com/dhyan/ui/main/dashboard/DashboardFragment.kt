package projects.vikky.com.dhyan.ui.main.dashboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dagger.android.support.DaggerFragment
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.ui.main.Resource
import projects.vikky.com.dhyan.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class DashboardFragment : DaggerFragment() {
    private val TAG: String = "DashboardFragment"
    private lateinit var viewModel: DashboardViewModel

    private lateinit var progressBar: ProgressBar
    private lateinit var countText: TextView

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.dashboard_progress_bar)
        countText = view.findViewById(R.id.total_count)
        viewModel = ViewModelProviders.of(this, providerFactory).get(DashboardViewModel::class.java)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.observeMachineCount().removeObservers(viewLifecycleOwner)
        viewModel.observeMachineCount().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d(TAG, "onChanged:in machine count: ${it.data?.machineCount?.total_count}")
                when (it.status) {
                    Resource.Status.LOADING -> {
                        Log.d(TAG, "onChanged: Loading ")
                        showProgressbar(true)
                    }
                    Resource.Status.SUCCESS -> {
                        Log.d(TAG, "onChanged: got posts")
                        showProgressbar(false)
                        setCount(it.data?.machineCount?.total_count)
                    }
                    Resource.Status.ERROR -> {
                        Toast.makeText(activity, "${it.message}", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onChanged: Error:${it.message} ")
                        showProgressbar(false)
                    }

                    Resource.Status.NOT_AUTHENTICATED -> {
//                        Toast.makeText(activity,"${it.message}",Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "onChanged: session timeout:${it.message} ")
                        sessionManager.logout()

                    }

                }
            }
        })
    }

    private fun setCount(totalCount: Int?) {
        if (totalCount == -1) {
            countText.text = "There is some error please try after sometime"
            return
        }
        countText.text = totalCount.toString()
    }

    private fun showProgressbar(isShowing: Boolean) {
        if (isShowing) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}