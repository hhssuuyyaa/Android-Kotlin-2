package com.ayush171196.Startup

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MyTrackers : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trackers)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        //Loads menu in activity when this method calls up
        val inflater = menuInflater
        inflater.inflate(R.menu.tracker_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId){
            R.id.finishActivity ->{
                finish()
            }
            R.id.addContact ->{
                //TODO:: ask new Contact
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }
}
