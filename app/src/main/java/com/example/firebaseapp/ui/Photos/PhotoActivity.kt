package com.example.firebaseapp.ui.Photos

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.view.Menu
import android.view.MenuItem
import com.example.firebaseapp.R
import com.example.firebaseapp.Router
import com.example.firebaseapp.firebase.authentication.AuthenticationManager


import com.example.firebaseapp.utils.ImageLoader
import android.content.Context
import com.example.firebaseapp.firebase.cloud.CloudStorageManager


import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_home.addPostFab
import kotlinx.android.synthetic.main.activity_photo.*

class PhotoActivity : AppCompatActivity() {
    private val authenticationManager by lazy { AuthenticationManager() }
    private val cloudStorageManager by lazy { CloudStorageManager() }

    private val router by lazy { Router() }
    private val imageLoader by lazy { ImageLoader() }

    companion object {
        const val CHOOSE_IMAGE_REQUEST_CODE = 123
        fun createIntent(context: Context) = Intent(context, PhotoActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        initialize()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_home, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        when (item?.itemId) {
            R.id.action_logout -> {
                authenticationManager.signOut(this)

                router.startLoginScreen(this)
                finish()
                true
            }
            else -> {
                if (item != null) {
                    super.onOptionsItemSelected(item)
                }
                true
            }
        }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CHOOSE_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri = data?.data ?: return
            cloudStorageManager.uploadPhoto(selectedImageUri, ::onPhotoUploadSuccess)


        }
    }








    private fun initialize() {


        addPostFab2.setOnClickListener { router.showImagePicker(this) }

    }

    private fun onPhotoUploadSuccess(url: String) {
        imageLoader.loadImage(this, url, imageView)
    }



}