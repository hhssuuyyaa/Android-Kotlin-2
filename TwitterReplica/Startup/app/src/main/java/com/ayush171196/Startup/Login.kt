package com.ayush171196.Startup

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.Image
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.ivimagePerson
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Login : AppCompatActivity() {

    private var mAuth:FirebaseAuth?=null
    private var database= FirebaseDatabase.getInstance()
    private var myRef=database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth= FirebaseAuth.getInstance()
        //sbse pehle jb user imageview pr click krega to ye function call hoga.
        ivimagePerson.setOnClickListener(View.OnClickListener {
            checkPermission()
            //checkPermission naam ka function neeche call hojayega
        })



    }
    fun LoginToFireBase(email:String,password:String)
    {
        mAuth!!.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){ task ->

                    if (task.isSuccessful)
                    {
                        Toast.makeText(applicationContext,"Successful login",Toast.LENGTH_LONG).show()
                        SaveImageInFirebase()
                    }else
                    {
                        Toast.makeText(applicationContext,"fail login",Toast.LENGTH_LONG).show()
                    }
                }
    }
    fun SaveImageInFirebase()
    {
        var currentUser =mAuth!!.currentUser    //current user ki info leli
        val email:String = currentUser!!.email.toString()   //email acquire krli uski
        val storage = FirebaseStorage.getInstance()     //firebasestorage ki info lene ke liye pehle android me hmesha hum .getinstance lege uska ek variable me
        val storageref = storage.getReferenceFromUrl("gs://tictactoeonline-26f96.appspot.com")      //storage database se link krega ye varaible
        val df = SimpleDateFormat("ddMMyyHHmmss")       //ye hum basically unique path dene ke liye bna re h
        val dataobj = Date()
        val imagePath = splitString(email) + "." + df.format(dataobj)+ ".jpg"   //unique path bngya
        val ImageRef = storageref.child("images/"+imagePath)    //ek folder dhundhega image nam ka aur usme ye imagepath daldega
        ivimagePerson.isDrawingCacheEnabled=true
        ivimagePerson.buildDrawingCache()

        val drawable = ivimagePerson.drawable as BitmapDrawable
        val bitmap = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos)
        val data = baos.toByteArray()
        val uploadTask =ImageRef.putBytes(data)
        uploadTask.addOnFailureListener{
            Toast.makeText(applicationContext,"Failed to upload",Toast.LENGTH_LONG).show()
        }.addOnSuccessListener { taskSnapshot ->
            var DownloadUrl= taskSnapshot.downloadUrl!!.toString()
            myRef.child("Users").child(currentUser.uid).child("Email").setValue(currentUser.email)
            myRef.child("Users").child(currentUser.uid).child("Profile Image").setValue(DownloadUrl)
            LoadTweets()

        }
    }
    fun splitString(email: String):String
    {
        val split = email.split("@")
        return split[0]
    }

    override fun onStart() {
        super.onStart()
        LoadTweets()
    }

    fun LoadTweets()
    {
        var currentUser =mAuth!!.currentUser

        if(currentUser!=null) {


            var intent = Intent(this,MainActivity::class.java)
            intent.putExtra("email", currentUser.email)
            intent.putExtra("uid", currentUser.uid)
            startActivity(intent)

        }
    }

    val READIMAGE:Int=253
    fun checkPermission()
    {
        //ab ye check krega ki hmara device ka version agr jelly bean se zyada h to usko permission mili hui h ya ni. agr ni mili to request permission
        //aur agr mili hui h to if loop bypass hojayega aur seedha load image call hojayega
        if(Build.VERSION.SDK_INT>=23)
        {
            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),READIMAGE)
            //requestPermission ek aur function call krega neeche bnaya h jo ovveride krke
            return
        }
         loadImage()
    }
//neeche wala function will called up jb user accept ya reject krega permission ko jo popup hokr ayegi.

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode)
        {
            READIMAGE->{
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    loadImage()
                }else{
                    Toast.makeText(this,"Cannot access your images",Toast.LENGTH_LONG).show()
                }
                //ye neeche wala kbhi call ni hoga bs declare kra h jisse error na aaye predefined aya tha humne ni kra.
            }
            else-> super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        }

    }
    val PICK_IMAGE_CODE:Int=123
    fun loadImage()
    {
        //there are many ways to load images but the easiest way is too implement via intent
        var intent = Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //ab hum startActivity() ni likh skte jese abi tk likhte aye h content ke liye because startActivity ka mtlh h dusri activity start krlo
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
          ivimagePerson.setImageBitmap(BitmapFactory.decodeFile(picturePath))
        }
    }
    fun loginbuttonclicked(view:View)
    {
        LoginToFireBase(emailtext.text.toString(),passwordtext.text.toString())
    }
}
