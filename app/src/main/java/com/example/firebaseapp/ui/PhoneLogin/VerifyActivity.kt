package com.example.firebaseapp.ui.PhoneLogin



import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.R
import com.example.firebaseapp.ui.home.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class VerifyActivity :AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify)

        auth= FirebaseAuth.getInstance()
        val storedVerificationId=intent.getStringExtra("storedVerificationId")

        //Reference
        val verify = findViewById<Button>(R.id.verify_btn)
        val otpGiven = findViewById<EditText>(R.id.id_otp)

        verify.setOnClickListener{
            var otp = otpGiven.text.toString().trim()
            if(!otp.isEmpty())
            {
                val credential : PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId.toString(),otp)
                signInWithPhoneAuthCredential(credential)


            }else{
                Toast.makeText(this,"Enter OTP",Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential)
    {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->


                if(task.isSuccessful){
                    startActivity(Intent(applicationContext,HomeActivity::class.java))
                    finish()
                }else{
                    if(task.exception is FirebaseAuthInvalidCredentialsException){
                        Toast.makeText(this,"Invalid OTP",Toast.LENGTH_SHORT).show()

                    }
                }


            }
    }
}