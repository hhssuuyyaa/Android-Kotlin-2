package com.ayush171196.Startup

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.BaseAdapter
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.contact_ticket.view.*

class MainActivity : AppCompatActivity() {
    var adapter: MyTrackers.ContactAdapter?=null
    var listOfContact=ArrayList<UserContact>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userData = UserData(this)
        userData.loadPhoneNumber()

        dummyData()

        adapter = MyTrackers.ContactAdapter(this, listOfContact)
        lvContactList.adapter = adapter
        lvContactList.onItemClickListener= AdapterView.OnItemClickListener{ parent, view, position, id ->
            val userInfo = listOfContact[position]
            UserData.myTrackers.remove(userInfo.phoneNumber)
        }
    }

    //for debug
    fun dummyData() {
        listOfContact.add(UserContact("Ayush", "8178826886"))
        listOfContact.add(UserContact("Anshul", "8447770899"))
        listOfContact.add(UserContact("Papa", "9810024409"))

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

    class ContactAdapter: BaseAdapter {

        var listOfContact=ArrayList<UserContact>()
        var context: Context?=null
        constructor(context: Context, listOfContact:ArrayList<UserContact>){
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
