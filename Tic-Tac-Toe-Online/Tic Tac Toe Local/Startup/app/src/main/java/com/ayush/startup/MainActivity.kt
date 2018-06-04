package com.ayush.startup

import android.graphics.Color
import com.google.firebase.analytics.FirebaseAnalytics
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    //database instance
    private var database = FirebaseDatabase.getInstance()
    private var myRef = database.reference
    var myEmail:String? = null

    private  var mFirebaseAnalytics:FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var b:Bundle=intent.extras
        myEmail=b.getString("email")
        incomingCall()
    }
    fun buClick(view: View){
        val buSelected= view as Button
        var cellId=0
        when(buSelected.id){
            R.id.bu1-> cellId=1
            R.id.bu2-> cellId=2
            R.id.bu3-> cellId=3
            R.id.bu4-> cellId=4
            R.id.bu5-> cellId=5
            R.id.bu6-> cellId=6
            R.id.bu7-> cellId=7
            R.id.bu8-> cellId=8
            R.id.bu9-> cellId=9
        }
        //Toast.makeText(this,"Id:"+cellId,Toast.LENGTH_LONG).show()
        myRef.child("PlayerOnline").child(sessionId).child(cellId.toString()).setValue(myEmail)
    }

    var player1=ArrayList<Int>()
    var player2=ArrayList<Int>()
    var activePlayer=1

    fun playGame(cellID:Int,buSelected:Button)
    {
        if(activePlayer==1){
            buSelected.text="O"
            buSelected.setBackgroundColor(Color.GREEN)
            player1.add(cellID)
            activePlayer=2
        }else
            {   buSelected.text="X"
                buSelected.setBackgroundColor(Color.RED)
                player2.add(cellID)
                activePlayer=1

        }
        buSelected.isEnabled=false
        findWinner()
    }

    fun findWinner(){
        var winner=-1

        //row1
        if(player1.contains(1) && player1.contains(2) && player1.contains(3)){
            winner=1 }

        if(player2.contains(1) && player2.contains(2) && player2.contains(3)){
            winner=2 }

        //row2
        if(player1.contains(4) && player1.contains(5) && player1.contains(6)) {
            winner=1 }

        if(player2.contains(4) && player2.contains(5) && player2.contains(6)) {
            winner=2 }

        //row3
        if(player1.contains(7) && player1.contains(8) && player1.contains(9)) {
            winner=1 }

        if(player2.contains(7) && player2.contains(8) && player2.contains(9)) {
            winner=2 }

        //column1
        if(player1.contains(1) && player1.contains(4) && player1.contains(7)) {
            winner=1 }

        if(player2.contains(1) && player2.contains(4) && player2.contains(7)) {
            winner=2 }

        //column2
        if(player1.contains(2) && player1.contains(5) && player1.contains(8)) {
            winner=1 }

        if(player2.contains(2) && player2.contains(5) && player2.contains(8)) {
            winner=2 }

        //column3
        if(player1.contains(3) && player1.contains(6) && player1.contains(9)){
            winner=1 }

        if(player2.contains(3) && player2.contains(6) && player2.contains(9)){
            winner=2 }

        //diagonal1
        if(player1.contains(1) && player1.contains(5) && player1.contains(9)){
            winner=1 }

        if(player2.contains(1) && player2.contains(5) && player2.contains(9)){
            winner=2 }

        //diagonal2
        if(player1.contains(3) && player1.contains(5) && player1.contains(7)){
            winner=1 }

        if(player2.contains(3) && player2.contains(5) && player2.contains(7)) {
            winner=2 }

        if(winner!=-1)
        {
            if(winner==1) {
                Toast.makeText(this, "PLAYER 1 WINS", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "PLAYER 2 WINS", Toast.LENGTH_LONG).show()
            }
        }

    }

    fun autoPlay(cellID: Int)
    {
        var buSelect:Button?
        when(cellID)
        {
            1->buSelect=bu1
            2->buSelect=bu2
            3->buSelect=bu3
            4->buSelect=bu4
            5->buSelect=bu5
            6->buSelect=bu6
            7->buSelect=bu7
            8->buSelect=bu8
            9->buSelect=bu9
            else->buSelect=bu1      //this case will never happens. it is saying that what if user didnt select any
        }
        playGame(cellID,buSelect)
    }
    fun requestEvent(view: View)
    {
        var userEmail = emailotherplayer.text.toString()
        //yha pr suppose suchitra ne app kholi apni email daali aur intent ke through myEmail me uski email aagyi. then fir jb button click kregi
        //suchitra apne device me to fir vo next activity me likhegi ki kis user ka nam chahiye to usme unhone anshul likha aur userEmail me
        //anshul aagya then fir neeche wali line ke through mRef ki help se Users ka child bnega userEmail joki anshul hai to vo database me anshul
        //ko pkdega and uski uid me email daal dega suchitra ki. means request button se dusre bnde pr jayegi info khud pr ni.
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
        playerOnline(splitString(myEmail!!)+splitString(userEmail))
        playerSymbol="X"
    }
    fun acceptEvent(view: View)
    {
        var userEmail = emailotherplayer.text.toString()
        myRef.child("Users").child(splitString(userEmail)).child("Request").push().setValue(myEmail)
        playerOnline(splitString(userEmail)+splitString(myEmail!!))
        playerSymbol="O"
    }
var sessionId:String?=null
    var playerSymbol:String?=null
    fun playerOnline(sessionId:String)
    {
        this.sessionId=sessionId
        myRef.child("PlayerOnline").removeValue()
        myRef.child("PlayerOnline").child(sessionId)
                .addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {
                    }
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        try {
                            player1.clear()
                            player2.clear()
                            val td = dataSnapshot!!.value as HashMap<String,Any>
                            if(td!=null)
                            {
                                var value:String
                                for (key in td.keys){
                                    value=td[key] as String
                                if(value!=myEmail)
                                {
                                    activePlayer= if(playerSymbol==="X") 1 else 2
                                }else
                                {
                                    activePlayer= if(playerSymbol==="X") 2 else 1

                                }
                                    autoPlay(key.toInt())
                                }
                            }
                        }catch (ex:Exception)
                        {}
                    }
                })
    }
    fun incomingCall()
    {
        //Suchitra ne apni email ko @ se pehle tk... Users->Request->me addValueEventListener
        myRef.child("Users").child(splitString(myEmail!!)).child("Request")
                .addValueEventListener(object :ValueEventListener{
                    override fun onCancelled(p0: DatabaseError?) {

                    }
                    override fun onDataChange(dataSnapshot: DataSnapshot?) {
                        try {
                            val td = dataSnapshot!!.value as HashMap<String,Any>
                            if(td!=null)
                            {
                                var value:String
                                for (key in td.keys){
                                    value=td[key] as String
                                    //neeche wali line anshul ke phone me suchitra show kregi for accepting invitation
                                    emailotherplayer.setText(value)
                                    myRef.child("Users").child(splitString(myEmail!!)).child("Request").setValue(true)
                                    break

                                }
                            }
                        }catch (ex:Exception)
                        {}
                    }
                })
    }
    fun splitString(str:String):String{
        var split = str.split("@")
        return split[0]
    }
}
