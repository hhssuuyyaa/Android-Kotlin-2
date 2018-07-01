package com.ayush171196.Startup

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class myBroadcastReciever:BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
    //This mehtod gets fired automatically when recieve event happens in system
        if (intent!!.action.equals("com.tester.alarmmanager")){
            var b = intent.extras
            Toast.makeText(context,b.getString("message"),Toast.LENGTH_LONG).show()
        }
    }

}