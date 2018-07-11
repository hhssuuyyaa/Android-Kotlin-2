package com.ayush171196.Startup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userData = UserData(this)
        userData.loadPhoneNumber()
    }

    //To use menu in our project we need two methods and one of them is below
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //Loads menu in activity when this method calls up
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){
            R.id.addTracker ->{
                val intent = Intent(this,MyTrackers::class.java)
                startActivity(intent)
            }
            R.id.help ->{
                //TODO:: ask for help from friend
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }
}
