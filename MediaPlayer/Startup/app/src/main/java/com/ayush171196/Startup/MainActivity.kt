package com.ayush171196.Startup

import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_ticket.*
import kotlinx.android.synthetic.main.song_ticket.view.*

class MainActivity : AppCompatActivity() {

    var ListSongs=ArrayList<songInfo>()
    var adapter:MySongsAdapter?=null
    var mp:MediaPlayer?=null        //inbuilt class h mediaplayer to play and manage music
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //LoadURLOnline()
        CheckUserPermsions()

        var tracking = mySongTrack()
        tracking.start()
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
                if(myView.buPlay.text.equals("Stop")) {
                    mp!!.stop()
                    myView.buPlay.text="Start"
                }else
                {
                    mp=MediaPlayer()
                try {
                    mp!!.setDataSource(Song.SongURL)
                    mp!!.prepare()
                    mp!!.start()
                    myView.buPlay.text="Stop"
                    sbProgress.max=mp!!.duration
                }catch (ex:Exception){}
                }
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
    //The below class will take care of progress bar jo song ke sath sath bdega
    inner class mySongTrack():Thread() {

        override fun run() {
            while (true) {
                try {
                    Thread.sleep(1000)
                } catch (ex: Exception) { }
                    runOnUiThread{
                        if (mp!=null){
                            sbProgress.progress=mp!!.currentPosition
                        }
                    }
            }
        }
    }

    fun CheckUserPermsions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        REQUEST_CODE_ASK_PERMISSIONS)
                return
            }
        }

        LoadSong()

    }

    //get acces to location permsion
    private val REQUEST_CODE_ASK_PERMISSIONS = 123


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_ASK_PERMISSIONS -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                LoadSong()
            } else {
                // Permission Denied
                Toast.makeText(this, "denail", Toast.LENGTH_SHORT)
                        .show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    fun   LoadSong() {
        val allSongsURI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val selection = MediaStore.Audio.Media.IS_MUSIC + "!=0"
        val cursor = contentResolver.query(allSongsURI, null, selection, null, null)
        if (cursor != null) {
            if (cursor!!.moveToFirst()) {

                do {

                    val songURL = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DATA))
                    val SongAuthor = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val SongName = cursor!!.getString(cursor!!.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME))
                    ListSongs.add(songInfo(SongName, SongAuthor, songURL))
                } while (cursor!!.moveToNext())


            }
            cursor!!.close()
            adapter=MySongsAdapter(ListSongs)
            lv_listSongs.adapter=adapter

        }
    }
}
