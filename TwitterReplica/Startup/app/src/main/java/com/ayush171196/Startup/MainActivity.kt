package com.ayush171196.Startup

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_tickets.view.*
import kotlinx.android.synthetic.main.tweets_ticket.view.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference
    var ListTweets=ArrayList<ticket>()
    var adapter:MyTweetAdpater?=null
    var myEmail:String?=null
    var UserUid:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var b:Bundle=intent.extras
        myEmail=b.getString("email")
        UserUid=b.getString("uid")
        //DUMMY DATA
        ListTweets.add(ticket("0","him","url","add"))
        adapter = MyTweetAdpater(this,ListTweets)
        lvTweets.adapter=adapter
        loadPost()
    }
    inner class  MyTweetAdpater:BaseAdapter{
        var listNotesAdpater=ArrayList<ticket>()
        var context:Context?=null
        constructor(context:Context, listNotesAdpater:ArrayList<ticket>):super(){
            this.listNotesAdpater=listNotesAdpater
            this.context=context
        }

        override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {

            var myTweet=listNotesAdpater[p0]
            if(myTweet.tweetPersonUid.equals("add")){
                var myView=layoutInflater.inflate(R.layout.add_tickets,null)
                myView.iv_attach.setOnClickListener(View.OnClickListener {
                loadImage() //jb koi add pr click krega uske bad attack pr click krega to hum loadImage() pr jayege
                })
                myView.iv_post.setOnClickListener(View.OnClickListener {
                    myRef.child("post").push().setValue(
                            PostInfo(UserUid!!,
                            myView.etPost.text.toString(),DownloadUrl!!))
                })
                return myView
            }else if(myTweet.tweetPersonUid.equals("loading")){
                var myView=layoutInflater.inflate(R.layout.loading_ticket,null)
                return myView
            }
            else{
                var myView=layoutInflater.inflate(R.layout.tweets_ticket,null)
                //Loadd tweet ticket
                myView.txt_tweet.setText(myTweet.tweetText)
                //myView.tweet_picture.setImageURI(myTweet.tweetImageUrl)
                Picasso.get().load(myTweet.tweetImageUrl).into(myView.tweet_picture)
                myRef.child("Users").child(myTweet.tweetPersonUid)
                        .addValueEventListener(object :ValueEventListener{
                            override fun onDataChange(dataSnapshot: DataSnapshot?) {
                                try {

                                    var td = dataSnapshot!!.value as HashMap<String,Any>
                                    for (key in td.keys)
                                    {
                                        var userInfo = td[key] as String
                                        if(key.equals("Profile Image")){
                                            Picasso.get().load(userInfo).into(myView.picture_path)
                                        }else{
                                            myView.txtUserName.setText(userInfo)

                                        }
                                    }

                                }catch (ex:Exception)
                                {
                                }
                            }

                            override fun onCancelled(p0: DatabaseError?) {
                            }
                        })
                return myView
            }
        }

        override fun getItem(p0: Int): Any {
            return listNotesAdpater[p0]
        }
        override fun getItemId(p0: Int): Long {
            return p0.toLong()
        }
        override fun getCount(): Int {
            return listNotesAdpater.size
        }
    }
    //LOAD IMAGE
    val PICK_IMAGE_CODE:Int=123
    fun loadImage()
    {
        //there are many ways to load images but the easiest way is too implement via intent
        var intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //ab hum startActivity() ni likh skte jese abi tk likhte aye h content ke liye because startActivity ka mtlb h dusri activity start krlo
        //and finish...but hume iss case me picture select krvakr usse return bi krna h to uske liye hum startActivityForResult call krege
        startActivityForResult(intent,PICK_IMAGE_CODE)
        //startActivity apne ap ovveride ka ek function call krega jo neeche declare kra hai. neeche wala function result jo return hua h usse lega.
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==PICK_IMAGE_CODE && data!=null && resultCode == RESULT_OK) //data!=null means kuch to select kr rkha h user ne mtlb imageview me bs ab display krvani h image humne
        {
            //to display hume apni image ki info and location ko contentResolver m bhejna hota hai.
            val selectedImage = data.data     //data.data will have information about selected image.
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = contentResolver.query(selectedImage,filePathColumn,null,null,null)
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val picturePath = cursor.getString(columnIndex)
            cursor.close()
            UploadImage(BitmapFactory.decodeFile(picturePath))
        }
    }
    var DownloadUrl:String?=null
    fun UploadImage(bitmap: Bitmap)
    {
        ListTweets.add(0,ticket("0","him","url","loading"))
        adapter!!.notifyDataSetChanged()
        val storage = FirebaseStorage.getInstance()     //firebasestorage ki info lene ke liye pehle android me hmesha hum .getinstance lege uska ek variable me
        val storageref = storage.getReferenceFromUrl("gs://tictactoeonline-26f96.appspot.com")      //storage database se link krega ye varaible
        val df = SimpleDateFormat("ddMMyyHHmmss")
        val dataobj = Date()
        val imagePath = splitString(myEmail!!) + "." + df.format(dataobj)+ ".jpg"   //unique path bngya
        val ImageRef = storageref.child("imagePost/"+imagePath)    //ek folder dhundhega image nam ka aur usme ye imagepath daldega
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data = baos.toByteArray()
        val uploadTask =ImageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext,"Failed to upload", Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->
            ListTweets.removeAt(0)
            adapter!!.notifyDataSetChanged()
            DownloadUrl= taskSnapshot.downloadUrl!!.toString()
        }
    }
    fun splitString(email: String):String
    {
        val split = email.split("@")
        return split[0]
    }
    fun loadPost()
    {
        myRef.child("post")
                .addValueEventListener(object :ValueEventListener{
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        try {
                            ListTweets.clear()
                            ListTweets.add(ticket("0","him","url","add"))
                            var td = dataSnapshot!!.value as HashMap<String,Any>
                            for (key in td.keys)
                            {
                                var post = td[key] as HashMap<String,Any>
                                ListTweets.add(ticket(key,
                                        post["text"] as String,
                                        post["postImage"] as String,
                                        post["userUID"] as String))
                            }
                            adapter!!.notifyDataSetChanged()
                        }catch (ex:Exception)
                        {
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {
                    }
                })
    }
}
