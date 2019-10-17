package projects.vikky.com.dhyan.ui.main.posts

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.android.support.DaggerFragment
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.ui.main.Resource
import projects.vikky.com.dhyan.viewmodels.ViewModelProviderFactory
import javax.inject.Inject

class PostsFragment : DaggerFragment() {

    private val TAG: String = "PostFragment"

    private lateinit var viewModel: PostViewModel
    private lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory

    @Inject
    lateinit var postRecyclerAdapter: PostRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recycler_view)

        viewModel = ViewModelProviders.of(this, providerFactory).get(PostViewModel::class.java)
        initRecyclerView()
        subscribeObserver()
    }

    private fun subscribeObserver() {
        viewModel.observePosts().removeObservers(viewLifecycleOwner)
        viewModel.observePosts().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d(TAG, "onChanged:in post: ${it.data}")
                when (it.status) {
                    Resource.Status.LOADING -> {
                        Log.d(TAG, "onChanged: Loading ")
                    }
                    Resource.Status.SUCCESS -> {
                        Log.d(TAG, "onChanged: got posts")
                        postRecyclerAdapter.setPosts(it.data!!)
                    }
                    Resource.Status.ERROR -> {
                        Log.d(TAG, "onChanged: Error:${it.message} ")
                    }
                }
            }
        })
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = postRecyclerAdapter
    }
}
