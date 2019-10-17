package projects.vikky.com.dhyan.ui.main.posts

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.schedulers.Schedulers
import projects.vikky.com.dhyan.SessionManager
import projects.vikky.com.dhyan.models.Post
import projects.vikky.com.dhyan.network.main.MainApi
import projects.vikky.com.dhyan.ui.main.Resource
import javax.inject.Inject

class PostViewModel @Inject constructor(val sessionManager: SessionManager, val mainApi: MainApi) :
    ViewModel() {

    private val TAG: String = "PostViewModel"

    private var posts: MediatorLiveData<Resource<List<Post>>>? = null

    init {
        Log.d(TAG, "${TAG}: viewmolde is working ")
    }

    fun observePosts(): LiveData<Resource<List<Post>>> {
        if (posts == null) {
            posts = MediatorLiveData()
            posts!!.value = Resource.loading(null)

            val source: LiveData<Resource<List<Post>>> = LiveDataReactiveStreams.fromPublisher(
                mainApi.getPostFromUser(sessionManager.getAuthUser().value!!.data!!.userData!!.userId!!)
                    .onErrorReturn {
                        Log.d(TAG, "apply: error ")
                        var post = Post()
                        val arrayList = ArrayList<Post>()
                        arrayList.add(post)
                        arrayList
                    }.map {
                        if (it.size > 0) {
                            if (it.get(0).id == -1) {
                                Resource.error("Something went wrong", null)
                            }
                        }
                        Resource.success(it)
                    }.subscribeOn(Schedulers.io())

            )

            posts!!.addSource(source, Observer {
                posts!!.value = it
                posts!!.removeSource(source)
            })
        }


        return posts!!
    }
}
