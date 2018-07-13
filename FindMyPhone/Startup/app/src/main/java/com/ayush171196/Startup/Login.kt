package com.ayush171196.Startup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.Task
import android.support.annotation.NonNull
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*


class Login : AppCompatActivity() {
    var mAuth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        signInAnonymously()
    }

    fun signInAnonymously(){
        mAuth!!.signInAnonymously()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(applicationContext, "Authentication Succesful.",
                                Toast.LENGTH_SHORT).show()
                        val user = mAuth!!.getCurrentUser()
                    } else {
                        Toast.makeText(applicationContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

    fun buRegisterEvent(view: View){

        val userData = UserData(this)
        userData.savePhone(etPhoneNumber.text.toString())

        //get date
        val df = SimpleDateFormat("yyyy/MMM/dd HH:MM:ss")
        val date = Date()

        //save to firebase
        val mDatabase = FirebaseDatabase.getInstance().reference
        mDatabase.child("Users").child(etPhoneNumber.text.toString()).child("request").setValue(df.format(date).toString())
        mDatabase.child("Users").child(etPhoneNumber.text.toString()).child("finders").setValue(df.format(date).toString())
        finish()
    }
}
