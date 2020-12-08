package com.example.firebaseapp.ui.PhoneLogin



import android.content.ComponentCallbacks
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firebaseapp.R
import com.example.firebaseapp.ui.addPost.AddPostActivity
import com.example.firebaseapp.ui.home.HomeActivity
import com.example.firebaseapp.ui.login.LoginActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_phone_login.*
import java.util.concurrent.TimeUnit

class PhoneLoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var storedVerificationId: String
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks


    companion object {
        fun createIntent(context: Context) = Intent(context, PhoneLoginActivity::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_login)
        auth = FirebaseAuth.getInstance()

        // Reference
        val Login = findViewById<Button>(R.id.btn_phone_login)

        var currentUser = auth.currentUser
        if (currentUser != null) {
            startActivity(Intent(this, HomeActivity::class.java))

        }
        Login.setOnClickListener {
            login()

        }
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
                finish()
            }

            override fun onVerificationFailed(e: FirebaseException) {

                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()

            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                Log.d("TAG","onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token

                var intent = Intent(applicationContext,VerifyActivity::class.java)
                intent.putExtra("storedVerificationId",storedVerificationId)
                startActivity(intent)
            }

        }


        btn_back.setOnClickListener{
            startActivity(Intent(applicationContext,VerifyActivity::class.java))

        }


    }

    /*
    EMULATOR DOES NOT SUPPORT TEXT VERIFICATION SO I ADDED A TEST NUMBER WITH A VERIFICATION CODE ONTO FIREBASE
    NUMBER:6505554567
    PIN VERIFICATION: 123456
     */

    private fun login() {
        val mobileNumber=findViewById<EditText>(R.id.phoneNumber)
        var number=mobileNumber.text.toString().trim()



        if(number.isEmpty()){
            number= "+16505554567"
            sendVerificationcode (number)
        }else{
            Toast.makeText(this,"Enter mobile number",Toast.LENGTH_SHORT).show()
        }
    }

    private fun sendVerificationcode(number: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }
}