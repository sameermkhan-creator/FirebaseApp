package com.example.firebaseapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseapp.R
import com.example.firebaseapp.model.Post
import com.example.firebaseapp.utils.DateUtils
import com.google.android.gms.common.util.DataUtils
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.MutableData
import kotlinx.android.synthetic.main.post_item.view.*
import com.google.firebase.database.Query
import com.google.firebase.database.Transaction
import com.google.firebase.ktx.Firebase

/*
Data class for home feed
 */
class FeedAdapter(private val dateUtils: DateUtils) : RecyclerView.Adapter<FeedAdapter.PostViewHolder>() {
    private lateinit var database: DatabaseReference

    val uid: String
        get() = Firebase.auth.currentUser!!.uid
    /*
    Feed adapter will show posts created in the app on the home screen
     */
    private val posts = mutableListOf<Post>()
    private val onItemClickLiveData = MutableLiveData<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return PostViewHolder(view, onItemClickLiveData, dateUtils)
    }

    override fun onBindViewHolder(holder: FeedAdapter.PostViewHolder, position: Int) {
        holder.setItem(posts[position])
    }

    override fun getItemCount(): Int = posts.size


    fun onFeedUpdate(posts: List<Post>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    fun onPostItemClick(): LiveData<Post> = onItemClickLiveData


    class PostViewHolder(
        private val view: View,
        private val onItemClickLiveData: MutableLiveData<Post>,
        private val dateUtils: com.example.firebaseapp.utils.DateUtils
    ) : RecyclerView.ViewHolder(view) {

    private lateinit var post: Post

    init {
        view.setOnClickListener { onItemClickLiveData.postValue(post) }
    }

    fun setItem(post: Post) {
        this.post = post
        with(view) {
            author.text = post.author
            content.text = post.content
            time.text = dateUtils.mapToNormalisedDateText(post.timestamp)
            }
        }
    }
/*
    // [START post_stars_transaction]
    private fun onLikesClicked(postRef: DatabaseReference) {
        postRef.runTransaction(object : Transaction.Handler {
            override fun doTransaction(mutableData: MutableData): Transaction.Result {
                val p = mutableData.getValue(Post::class.java)
                    ?: return Transaction.success(mutableData)

                if (p.likes.containsKey(uid)) {
                    // Unstar the post and remove self from stars
                    p.likesCount = p.likesCount - 1
                    p.likes.remove(uid)
                } else {
                    // Star the post and add self to stars
                    p.likesCount = p.likesCount + 1
                    p.likes[uid] = true
                }

                // Set value and report transaction success
                mutableData.value = p
                return Transaction.success(mutableData)
            }

            override fun onComplete(
                databaseError: DatabaseError?,
                committed: Boolean,
                currentData: DataSnapshot?
            ) {
                // Transaction completed
                Log.d(TAG, "postTransaction:onComplete:" + databaseError!!)
            }
        })
    }
    // [END post_stars_transaction]
   */

}