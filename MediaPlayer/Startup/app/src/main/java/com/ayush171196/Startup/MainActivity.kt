package com.ayush171196.Startup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.view.*

class MainActivity : AppCompatActivity() {

    var ListSongs=ArrayList<songInfo>()
    var adapter:MySongsAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadURLOnline()
        adapter=MySongsAdapter(ListSongs)
        lv_listSongs.adapter=adapter
    }
    fun LoadURLOnline()
    {
        ListSongs.add(songInfo("Tune jo na kaha","Mohit Chauhan","https://dl.mp3slash.xyz/compilations/indian/mohitchauhanbestofme1/[Songs.PK]%2012%20-%20Mohit%20Chauhan%20-%20Tune%20Jo%20Na%20Kaha.mp3"))
        ListSongs.add(songInfo("Hai Junoon","Mohit Chauhan","https://dl.mp3slash.xyz/sound11/indian/newyork/newyork01(www.songs.pk).mp3"))

    }
    //Adapters in Android are basically
    // a bridge between the UI components and the data source that fill data into the UI Component
    inner class MySongsAdapter:BaseAdapter
    {
        var myListSong=ArrayList<songInfo>()
        constructor(myListSong:ArrayList<songInfo>):super()
        {
            this.myListSong=myListSong
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
           //jitni bar getCount nikalega ye utni bar call hoga
            val myView = layoutInflater.inflate(R.layout.song_ticket,null)  //myView jo h hmara ab vo sara layout jo h ticket wala uski info rkega
            val Song= this.myListSong[position]
            myView.tvSongName.text = Song.Title
            myView.tvAuthor.text = Song.Author
            myView.buPlay.setOnClickListener(View.OnClickListener {
                //TODO: it plays song
            })
            return myView
        }

        override fun getItem(position: Int): Any {
            return this.myListSong[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return this.myListSong.size
            //ye count lega ki kitne songs h hmare list me
        }

    }
}
