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
           // Toast.makeText(context,b.getString("message"),Toast.LENGTH_LONG).show()
            val notifyMe = Notifications()
            notifyMe.Notify(context!!,b.getString("message"),10)
        }else if(intent!!.action.equals("android.permission.RECEIVE_BOOT_COMPLETED")){
        // We need to set time but we cant directly call SetTime because we cannot pass minute
            //and seconds here. So either we can use Sqlite, Firebase, or sharedPreference to save time
            //in phone storage. In this we will use sharedPreference
            val saveData = SaveData(context!!)
            saveData.setAlarm()
        }
    }

}