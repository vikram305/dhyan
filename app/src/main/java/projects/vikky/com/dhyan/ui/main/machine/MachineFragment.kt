package projects.vikky.com.dhyan.ui.main.machine

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.ui.main.Resource
import projects.vikky.com.dhyan.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class MachineFragment : DaggerFragment(){

    private val TAG: String = "Machine Fragment"
    private lateinit var viewModel: MachineViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView
    private lateinit var progressBar: ProgressBar
    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var machineRecyclerAdapter: MachineRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_machine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.machineListRecyclerView)
        emptyTextView = view.findViewById(R.id.emptyText)
        progressBar = view.findViewById(R.id.machine_progress_bar)

        viewModel = ViewModelProviders.of(this, providerFactory).get(MachineViewModel::class.java)
        initRecyclerView()
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.observeMachineList()?.removeObservers(viewLifecycleOwner)
        viewModel.observeMachineList()?.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                when (it.status) {
                    Resource.Status.LOADING -> {
                        Log.d(TAG, "onChanged: Loading ")
                        showProgressbar(true)
                        showList(false)
                        showErrorText(false)
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
                            machineRecyclerAdapter.setPosts(it.data!!)
                            showErrorText(false)
                            showList(true)
//                            emptyTextView.visibility=View.GONE
//                            recyclerView.visibility=View.VISIBLE
                        }

//                        postRecyclerAdapter.setPosts(it.data!!)
                    }
                    Resource.Status.ERROR -> {
                        Log.d(TAG, "onChanged: Error:${it.message} ")
                        emptyTextView.text = it.message

                        showList(false)
                        showErrorText(true)
                        showProgressbar(false)
//                        recyclerView.visibility=View.GONE
//                        emptyTextView.visibility=View.VISIBLE

                    }
                    Resource.Status.NOT_AUTHENTICATED -> {
                        showProgressbar(false)
//                        Toast.makeText(activity,"${it.message}",Toast.LENGTH_SHORT).show()
                        sessionManager.logout()
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = machineRecyclerAdapter
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
}