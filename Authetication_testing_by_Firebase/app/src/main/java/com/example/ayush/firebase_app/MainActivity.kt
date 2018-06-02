package com.example.ayush.firebase_app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Authentication
        mAuth = FirebaseAuth.getInstance()

    }

    fun buLoginEvent(view:View)
    {
        var email = etext.text.toString()
        var password = ptext.text.toString()
        loginToFirebase(email,password)
    }

    fun loginToFirebase(email:String,password:String)
    {
        mAuth!!.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->
                    if(task.isSuccessful)
                    {
                        Toast.makeText(applicationContext,"Succesful Login",Toast.LENGTH_LONG).show()
                        var currentUser = mAuth!!.currentUser
                        Log.d("Login:",currentUser!!.uid)
                    }else{
                        Toast.makeText(applicationContext,"Failed Login",Toast.LENGTH_LONG).show()

                    }
                }
    }


}

