package com.example.firebaseapp.ui.EmailLogin


import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.R
import com.example.firebaseapp.Router
import com.example.firebaseapp.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_phone_login.*

import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {
    private val router by lazy { Router() }
    private lateinit var auth: FirebaseAuth
    //static function to be used in router
    companion object {
        fun createIntent(context: Context) = Intent(context, SignupActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()

        btn_back3.setOnClickListener{
            startActivity(Intent(applicationContext,EmailActivity::class.java))

        }


        btn_sign_up.setOnClickListener {
            signUpUser()
        }

    }

    private fun signUpUser() {
        if (tv_username.text.toString().isEmpty()) {
            tv_username.error = "Please enter email"
            tv_username.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(tv_username.text.toString()).matches()) {
            tv_username.error = "Please enter valid email"
            tv_username.requestFocus()
            return
        }

        if (tv_password.text.toString().isEmpty()) {
            tv_password.error = "Please enter password"
            tv_password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(tv_username.text.toString(), tv_password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()
                                ?.addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        startActivity(Intent(this, LoginActivity::class.java))
                                        finish()
                                        Toast.makeText(
                                                baseContext, "Signup Successful",
                                                Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                    } else {
                        Toast.makeText(
                                baseContext, "Their is already an account associated with that email",
                                Toast.LENGTH_SHORT
                        ).show()
                    }
                }
    }
}

