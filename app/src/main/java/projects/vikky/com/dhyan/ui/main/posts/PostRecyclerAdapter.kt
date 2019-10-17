package projects.vikky.com.dhyan.ui.main.posts

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import projects.vikky.com.dhyan.R
import projects.vikky.com.dhyan.models.Post

class PostRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "RecyclerViewAdapter"

    private var posts: List<Post> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.posts_list_item, parent, false)
        Log.d(TAG, "viewCreated")
        return PostViewHolder(view)
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as PostViewHolder).bind(posts[position])
        Log.d(TAG, "bind called${position}")
    }

    fun setPosts(posts: List<Post>) {
        this.posts = posts
        Log.d(TAG, "posts list size:${posts.size}")
        notifyDataSetChanged()
    }

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var title: TextView

        init {
            title = itemView.findViewById(R.id.title)
        }

        fun bind(post: Post) {
            title.text = post.title
        }
    }

}