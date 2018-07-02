package com.ayush171196.Startup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ayush171196.Startup.R.id.tvShowTime
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val saveData = SaveData(applicationContext)
        tvShowTime.text=saveData.getHour().toString() + ":" + saveData.getMinute().toString()
    }
    fun BuSetTime(view:View){
        val popTime = PopTime()         //Made Object of PopTime Class
        val fm = fragmentManager
        popTime.show(fm,"Select Time")
    }
    fun SetTime(Hours:Int,Minute:Int){
        tvShowTime.text=Hours.toString() + ":" + Minute.toString()
        val saveData = SaveData(applicationContext)
        saveData.SaveData(Hours,Minute)
        saveData.setAlarm()
    }

}
