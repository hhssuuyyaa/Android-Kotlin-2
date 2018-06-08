package com.ayush171196.Startup

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var ListTweets=ArrayList<ticket>()
    var adapter:MyTweetAdpater?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //DUMMY DATA
        ListTweets.add(ticket("0","him","url","add"))
        ListTweets.add(ticket("0","him","url","ayush"))
        ListTweets.add(ticket("0","him","url","ayush"))
        ListTweets.add(ticket("0","him","url","ayush"))
        adapter = MyTweetAdpater(this,ListTweets)
        lvTweets.adapter=adapter
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
                //Load add ticket
                //TODO: work
                return myView
            }else{
                var myView=layoutInflater.inflate(R.layout.tweets_ticket,null)
                //Loadd tweet ticket
                //TODO: work
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
}
