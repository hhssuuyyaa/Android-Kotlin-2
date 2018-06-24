package com.ayush171196.Startup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    fun BuSetTime(view:View){
        val popTime = PopTime()
        val fm = fragmentManager
        popTime.show(fm,"Select Time")
    }
    fun SetTime(Hours:Int,Minute:Int){
        tvShowTime.text=Hours.toString() + ":" + Minute.toString()
    }

}
