package com.ayush171196.Startup

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.ActivityCompat
import android.text.Layout
import android.view.*
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.BaseAdapter
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_my_trackers.*
import kotlinx.android.synthetic.main.contact_ticket.view.*

class MyTrackers : AppCompatActivity() {

    var adapter:ContactAdapter?=null
    var listOfContact=ArrayList<UserContact>()
    var userData:UserData?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_trackers)
        userData = UserData(applicationContext)
       // dummyData()
        adapter = ContactAdapter(this,listOfContact)
        lvContactList.adapter = adapter
        lvContactList.onItemClickListener= AdapterView.OnItemClickListener{ parent, view, position, id ->
            val userInfo = listOfContact[position]
            UserData.myTrackers.remove(userInfo.phoneNumber)
            refreshData()

            //saved to shared reference
            userData!!.saveContactInfo()

            //remove from realtime database
            val mDatabase = FirebaseDatabase.getInstance().reference
            val userData = UserData(applicationContext)
            mDatabase.child("Users").child(userInfo.phoneNumber!!).child("finders").child(userData.loadPhoneNumber()).removeValue()
        }
        userData!!.loadContactInfo()
        refreshData()
    }

    //for debug
    /*fun dummyData() {
        listOfContact.add(UserContact("Ayush", "8178826886"))
        listOfContact.add(UserContact("Anshul", "8447770899"))
        listOfContact.add(UserContact("Papa", "9810024409"))

    */

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
                checkPermission()
            }
            else ->{
                return super.onOptionsItemSelected(item)
            }
        }
        return true
    }

    val CONTACT_CODE =123
    fun checkPermission(){

        if(Build.VERSION.SDK_INT>=23){

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) !=
                    PackageManager.PERMISSION_GRANTED ){

                requestPermissions(arrayOf(android.Manifest.permission.READ_CONTACTS), CONTACT_CODE)
                return
            }
        }
        pickContact()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        when (requestCode){
            PICK_CODE ->{
                if (resultCode== Activity.RESULT_OK){

                    val contactData=data!!.data
                    val c= contentResolver.query(contactData,null,null,null,null)

                    if (c.moveToFirst()){

                        val id= c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID))
                        val hasPhone= c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                        if (hasPhone.equals("1")){
                            val phones= contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null
                                    , ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id, null,null)

                            phones.moveToFirst()
                            var phoneNumber = phones.getString(phones.getColumnIndex("data1"))
                            val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                            phoneNumber = UserData.formatPhoneNumber(phoneNumber)
                            UserData.myTrackers.put(phoneNumber,name)
                            refreshData()
                            //saved to share reference
                            userData!!.saveContactInfo()

                            //save to realtime database
                            val mDatabase = FirebaseDatabase.getInstance().reference
                            val userData = UserData(applicationContext)
                            mDatabase.child("Users").child(phoneNumber).child("finders").child(userData.loadPhoneNumber()).setValue(true)
                        }

                    }

                }

            }
            else ->{
                super.onActivityResult(requestCode, resultCode, data)
            }
        }

    }

    val PICK_CODE=1234
    fun pickContact() {
        val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
        startActivityForResult(intent, PICK_CODE)
    }

    fun refreshData(){
        listOfContact.clear()
        for ((key,value) in UserData.myTrackers){
            listOfContact.add(UserContact(value,key))
        }
        adapter!!.notifyDataSetChanged()
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
