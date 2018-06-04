package com.ayush.startup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    private var mAuth:FirebaseAuth?= null
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        mAuth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    fun buLoginEvent(view: View)
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
                    {   var currentUser = mAuth!!.currentUser
                        Toast.makeText(applicationContext,"Successful Login",Toast.LENGTH_LONG).show()
                        //Save in database
                        if (currentUser!=null) {
                            //Firebase cannot add '.' in node name isliye humne uid ko as a variable name rkha aur email ko value
                            //isliye humne split krliya apni email ki string ko @ se pehle
                            myRef.child("Users").child(splitString(currentUser.email.toString())).child("Request").setValue(currentUser.uid)
                        }
                            loadMain()
                    }
                    else{
                        Toast.makeText(applicationContext,"Login Failed",Toast.LENGTH_LONG).show()
                    }
                }
    }
//jese hi app start hgi onstart call hoga neeche wala aur vo load main me bhej dega if hmara user already created hga
    //to vo intent ke through 2nd activity m chla jayega. but if koi user ni hga to loadMain wala if loop
    //fail hojayega and login ka xml display hojayega.
    override fun onStart() {
        super.onStart()
        loadMain()
    }

    fun loadMain()
    {
        var currentUser = mAuth!!.currentUser
        if (currentUser!=null) {
            var intent = Intent(this, MainActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)
            startActivity(intent)
        }
    }
    fun splitString(str:String):String{
        var split = str.split("@")
        return split[0]
    }
}
