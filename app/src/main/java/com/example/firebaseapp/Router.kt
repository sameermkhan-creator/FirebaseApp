package com.example.firebaseapp



import android.app.Activity
import android.content.Intent
import com.example.firebaseapp.model.Post
import com.example.firebaseapp.ui.EmailLogin.EmailActivity
import com.example.firebaseapp.ui.addPost.AddPostActivity

import com.example.firebaseapp.ui.home.HomeActivity
import com.example.firebaseapp.ui.login.LoginActivity


import com.example.firebaseapp.ui.PhoneLogin.PhoneLoginActivity
import com.example.firebaseapp.ui.Photos.PhotoActivity
import com.example.firebaseapp.ui.postDetails.PostDetailsActivity



class Router {

    companion object {
        private const val IMAGE_TYPE = "image/jpeg"
    }

    fun startHomeScreen(activity: Activity) {
        val home = HomeActivity.createIntent(activity)
        activity.startActivity(home)
    }

    fun startLoginScreen(activity: Activity) {
        val intent = LoginActivity.createIntent(activity)
        activity.startActivity(intent)
    }


    fun startPhoneLogin(activity: Activity)
    {
        val intent = PhoneLoginActivity.createIntent(activity)


        activity.startActivity(intent)

    }


    fun startLoginScreen2(activity: Activity) {
        val intent = EmailActivity.createIntent(activity)
        activity.startActivity(intent)
    }


    fun startUploadPhotoActivity(activity: Activity)
    {
        val intent = PhotoActivity.createIntent(activity)
        activity.startActivity(intent)


    }




    /*
    fun startsignupScreen2(activity: Activity) {
        val intent = EmailActivity.createIntent(activity)
        activity.startActivity(intent)
    }
*/

    fun showImagePicker(activity: Activity) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = IMAGE_TYPE
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        activity.startActivityForResult(Intent.createChooser(intent, activity.getString(R.string.image_chooser_title)), PhotoActivity.CHOOSE_IMAGE_REQUEST_CODE)
    }




    fun startAddPostScreen(activity: Activity) {
        val intent = AddPostActivity.createIntent(activity)
        activity.startActivity(intent)
    }

    fun startPostDetailsActivity(activity: Activity, post: Post) {
        val intent = PostDetailsActivity.createIntent(activity, post)
        activity.startActivity(intent)
    }
}