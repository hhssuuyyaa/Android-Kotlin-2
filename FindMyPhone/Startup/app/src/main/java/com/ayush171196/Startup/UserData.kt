package com.ayush171196.Startup

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences

class UserData{
    //Context act as remote of tv where tv is variables and fields
    var context:Context?=null
    var sharedRef:SharedPreferences?=null
    constructor(context: Context){
        this.context=context
        this.sharedRef=context.getSharedPreferences("userData",Context.MODE_PRIVATE)
    }

    fun savePhone(phoneNumber:String){

        val editor=sharedRef!!.edit()
        editor.putString("phoneNumber",phoneNumber)
        editor.commit()
    }

    fun loadPhoneNumber():String{

        val phoneNumber = sharedRef!!.getString("phoneNumber","empty")
        if (phoneNumber.equals("empty")){
            val intent=Intent(context,Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context!!.startActivity(intent)
        }

        return phoneNumber
    }

}