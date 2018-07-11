package com.ayush171196.Startup

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.*
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_my_trackers.*
import kotlinx.android.synthetic.main.contact_ticket.view.*

class MyTrackers : AppCompatActivity() {

    var adapter:ContactAdapter?=null
    var listOfContact=ArrayList<UserContact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trackers)
        dummyData()
        adapter = ContactAdapter(this,listOfContact)
        lvContactList.adapter = adapter
    }

    //for debug
    fun dummyData() {
        listOfContact.add(UserContact("Ayush", "8178826886"))
        listOfContact.add(UserContact("Anshul", "8447770899"))
        listOfContact.add(UserContact("Papa", "9810024409"))
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
    class ContactAdapter:BaseAdapter {

        var listOfContact=ArrayList<UserContact>()
        var context:Context?=null
        constructor(context:Context,listOfContact:ArrayList<UserContact>){
            this.context=context
            this.listOfContact=listOfContact
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val userContact = listOfContact[position]
            val inflator = context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val contactTicketView = inflator.inflate(R.layout.contact_ticket, null)
            contactTicketView.tvName.text= userContact.name
            contactTicketView.tvPhoneNumber.text = userContact.phoneNumber
            return contactTicketView
        }

        override fun getItem(position: Int): Any {
            return listOfContact[position]
            //we dont need this part
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listOfContact.size
        }
    }
}
