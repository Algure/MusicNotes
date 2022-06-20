package com.algure.musicnotes

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.algure.musicnotes.adapters.MusicListAdapter
import com.algure.musicnotes.provider.DataProvider
import com.algure.musicnotes.services.NotifierService


class MainActivity : AppCompatActivity() {
    private lateinit var button: Button
    private var mBound: Boolean = false
    private lateinit var mService: NotifierService


    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            val binder = service as NotifierService.NoticeBinder
            mService = binder.getService()
            mBound = true
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            mBound = false
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.notebut)
        button.setOnClickListener {
            Toast.makeText(applicationContext,"Clicked",Toast.LENGTH_SHORT).show()
            val i = Intent(this, NotifierService::class.java)
            startService(i)
            loadRecyclerView()
        }

    }

    private fun loadRecyclerView() {

        val recyclerview = findViewById<RecyclerView>(R.id.recv)

        recyclerview.layoutManager = LinearLayoutManager(this)

        val adapter = MusicListAdapter((DataProvider.musicTestData), ::playMusic)

        recyclerview.adapter = adapter

        recyclerview.visibility = VISIBLE
        hideButton()
    }

    private fun hideButton() {
        button.visibility = GONE
    }

    fun playMusic(item:Int): Unit {
        NotifierService.setCurrentMusic(item)
        var i = Intent(this, NotifierService::class.java)
        startService(i)
    }
}