package com.algure.musicnotes

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.algure.musicnotes.objects.MusicData
import androidx.core.app.NotificationManagerCompat
import com.algure.musicnotes.services.NotifierService


class MainActivity : AppCompatActivity() {
    private var mBound: Boolean = false
    private val notificationId: Int = 900
    private val CHANNEL_ID: String = "CHANNEL_U"
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

        val button = findViewById<Button>(R.id.notebut)
        button.setOnClickListener {
            Toast.makeText(applicationContext,"Clicked",Toast.LENGTH_SHORT).show()
            val i = Intent(this, NotifierService::class.java)
            startService(i)
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "getString(R.string.channel_name)"
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    private fun loadSmallNotification(notificationLayout: RemoteViews, musicData: MusicData) {
        notificationLayout.setTextViewText(R.id.notification_title, musicData.title)
    }


    private fun loadExpandedDetails(notificationLayoutExpanded: RemoteViews, musicData: MusicData){
        notificationLayoutExpanded.setTextViewText(R.id.title, musicData.title)
        notificationLayoutExpanded.setTextViewText(R.id.subtitle, musicData.description)
        notificationLayoutExpanded.setInt(R.id.line1, "setBackgroundColor", musicData.color)
    }

}