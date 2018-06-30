package com.ayush171196.Startup

import android.app.DialogFragment
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import java.sql.Time

class PopTime:DialogFragment(){
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        var myView = inflater!!.inflate(R.layout.pop_time,container,false)
        //will show layout
        var buDone = myView.findViewById(R.id.buDone) as Button
        //when user clicks done button
        var tp1=myView.findViewById(R.id.tp1) as TimePicker
        //the time we pick will display

        buDone.setOnClickListener({
            val ma = activity as MainActivity
            //to call Main Activity
            if(Build.VERSION.SDK_INT>=23) {
                ma.SetTime(tp1.hour, tp1.minute)
                //SetTime will called
            }else{
                ma.SetTime(tp1.currentHour, tp1.currentMinute)
            }
            this.dismiss()
        })

        return myView
    }
}