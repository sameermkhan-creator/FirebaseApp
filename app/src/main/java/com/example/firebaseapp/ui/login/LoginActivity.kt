package com.example.firebaseapp.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.Signature
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.R
import com.example.firebaseapp.Router
import com.example.firebaseapp.firebase.authentication.AuthenticationManager
import com.example.firebaseapp.firebase.authentication.RC_SIGN_IN
import com.example.firebaseapp.utils.showToast
import com.facebook.*
import com.facebook.login.LoginResult
import com.google.android.gms.ads.*
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_email.*
import kotlinx.android.synthetic.main.activity_email.btn_sign_up
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {
    var firebaseAuth: FirebaseAuth?=null
    var callbackManager:CallbackManager?=null




    //Router is used to do intents between activites
    private val router by lazy { Router() }

    //ref to auth manager class
    private val authenticationManager by lazy { AuthenticationManager() }
    private lateinit var mInterstitialAd: InterstitialAd


    //static function to be used in router
    companion object {
        fun createIntent(context: Context) = Intent(context, LoginActivity::class.java)
    }

    //when activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        FacebookSdk.sdkInitialize(applicationContext)

        firebaseAuth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/1033173712"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show()

            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {

    router.startLoginScreen(this@LoginActivity)
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
            }
        }


        btn_phone.setOnClickListener {
            router.startPhoneLogin(this)

        }

        login_button.setReadPermissions("email")
        login_button.setOnClickListener {
            signIn()
        }
        signinbutton.setOnClickListener {
            router.startLoginScreen2(this)
        }

        printKeyHash()
        initialize()

    }

    private fun signIn() {
        login_button.registerCallback(callbackManager,object:FacebookCallback<LoginResult>{
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
        val credential  = FacebookAuthProvider.getCredential(accessToken!!.token)
        firebaseAuth?.signInWithCredential(credential)
            ?.addOnFailureListener{ e -> Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()

            }
            ?.addOnSuccessListener { result ->
                val email = result.user?.email
                Toast.makeText(this,"You logged with email" + email,Toast.LENGTH_LONG).show()
                router.startHomeScreen(this)


            }

    }


    //once we sign, check to see if results are okay or have failed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager!!.onActivityResult(requestCode,resultCode,data)

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
    private fun printKeyHash()
    {
        try{
            val info = packageManager.getPackageInfo("com.example.firebaseapp",PackageManager.GET_SIGNATURES)
            for(signature in info.signatures)
            {
                val md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray())
                Log.e("KeyHash:",Base64.encodeToString(md.digest(),Base64.DEFAULT))
            }
        }catch(e:PackageManager.NameNotFoundException)
        {

        }
        catch(e:NoSuchAlgorithmException)
        {

        }
    }

    //
    private fun continueToHomeScreenIfUserSignedIn() = if(isUserSignedIn()) router.startHomeScreen(this) else Unit

    private fun setupClickListeners() {
        googleSignInButton.setOnClickListener { authenticationManager.startSignInFlow(this)}
    }

    private fun isUserSignedIn() = authenticationManager.isUserSignedIn()
}