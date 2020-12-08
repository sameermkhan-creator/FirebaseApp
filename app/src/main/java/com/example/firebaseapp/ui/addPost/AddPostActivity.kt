package com.example.firebaseapp.ui.addPost

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.R
import com.example.firebaseapp.firebase.realTimeDatabase.RealtimeDatabaseManager
import com.example.firebaseapp.utils.showToast
import kotlinx.android.synthetic.main.activity_add_post.*

class AddPostActivity : AppCompatActivity() {

    //static function to be used in router
    companion object {
        fun createIntent(context: Context) = Intent(context, AddPostActivity::class.java)
    }

    //ref to realtime database
    private val realtimeDatabaseManager by lazy { RealtimeDatabaseManager() }

    //on create function
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_post)
        initialize()
    }

    //
    private fun initialize() {
        setupClickListener()
        focusPostMessageInput()
    }

    private fun setupClickListener() {
        //set on click listener
        addPostButton.setOnClickListener {
            addPostIfNotEmpty()
        }
    }

    private fun addPostIfNotEmpty() {
        val postMessage = postText.text.toString().trim()
        if (postMessage.isNotEmpty()) {
            realtimeDatabaseManager.addPost(postMessage,
                { showToast(getString(R.string.posted_successfully)) },
                { showToast(getString(R.string.posting_failed)) })
            finish()
        } else {
            showToast(getString(R.string.empty_post_message))
        }
    }

    private fun focusPostMessageInput() {
        //Allow user to type in keyboard
        postText.requestFocus()
    }
}