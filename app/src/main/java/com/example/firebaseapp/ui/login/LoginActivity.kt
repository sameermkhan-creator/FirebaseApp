package com.example.firebaseapp.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.R
import com.example.firebaseapp.Router
import com.example.firebaseapp.firebase.authentication.AuthenticationManager
import com.example.firebaseapp.firebase.authentication.RC_SIGN_IN
import com.example.firebaseapp.utils.showToast
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {

    //Router is used to do intents between activites
    private val router by lazy { Router() }

    //ref to auth manager class
    private val authenticationManager by lazy { AuthenticationManager() }

    //static function to be used in router
    companion object {
        fun createIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }


    var firebaseAuth: FirebaseAuth? = null
    var callbackManager: CallbackManager? = null


    //when activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        btn_phone.setOnClickListener {
            router.startPhoneLogin(this)

        }

        login_button.setReadPermissions("email")
        login_button.setOnClickListener {

            signIn()

        }
        printKeyHash()
        initialize()

    }

    private fun signIn() {

        login_button.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
            override fun onSuccess(result: LoginResult?) {
                handleFacebookAccessToken(result!!.accessToken)


            }

            override fun onCancel() {
                TODO("Not yet implemented")
            }

            override fun onError(error: FacebookException?) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun handleFacebookAccessToken(accessToken: AccessToken?) {
//Get Credential
        val credential = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth?.signInWithCredential(credential)
            ?.addOnFailureListener { e -> Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()

            }
            ?.addOnSuccessListener { result ->


                //get email
                val email: String? = result.user?.email
                Toast.makeText(this, "You logged in with email: $email", Toast.LENGTH_LONG).show()
                router.startHomeScreen(this)


            }

    }




    private fun printKeyHash() = try {
        val info =
            packageManager.getPackageInfo("com.example.firebaseapp", PackageManager.GET_SIGNATURES)
        for (signature: Signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.e("KEYHASH", Base64.encodeToString(md.digest(), Base64.DEFAULT))

        }
    } catch (e: PackageManager.NameNotFoundException) {

    } catch (e: NoSuchAlgorithmException) {

    }



    //once we sign, check to see if results are okay or have failed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        callbackManager!!.onActivityResult(requestCode, resultCode, data)


        if(requestCode == RC_SIGN_IN) {

            if(resultCode == Activity.RESULT_OK) {
                router.startHomeScreen(this)
            } else {
                showToast("Signed in failed")
            }
        }
    }

    private fun initialize() {
       // setSupportActionBar(loginToolbar)
        continueToHomeScreenIfUserSignedIn()
        setupClickListeners()
    }

    //
    private fun continueToHomeScreenIfUserSignedIn() = if(isUserSignedIn()) router.startHomeScreen(
        this
    ) else Unit

    private fun setupClickListeners() {
        googleSignInButton.setOnClickListener { authenticationManager.startSignInFlow(this)}
    }

    private fun isUserSignedIn() = authenticationManager.isUserSignedIn()

}