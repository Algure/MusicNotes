package com.algure.musicnotes

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.algure.musicnotes.objects.MusicData


class MainActivity : AppCompatActivity() {
    private val notificationId: Int = 900
    private val CHANNEL_ID: String = "CHANNEL_U"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.notebut)
        button.setOnClickListener{
            Toast.makeText(applicationContext,"Clicked",Toast.LENGTH_SHORT).show()
            showNotification();
        }
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    fun showNotification(){
        showNotificationWithDets("Hi world", "cscs sfsnflj sdfsfjf disjdcij")
    }


    @SuppressLint("RemoteViewLayout")
    fun showNotificationWithDets(textContent:String, textTitle:String){
        val musicData = MusicData(
            title = "Test title",
            description = "srfes fdvsefr sfsfs fsefk srfserf fesrf sfrserf knnkkk kk ",
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_IMMUTABLE)

        createNotificationChannel()

        val notificationLayout = RemoteViews(packageName, R.layout.notification_small)
        val notificationLayoutExpanded = RemoteViews(packageName, R.layout.notification_large)

//        loadSmallNotification(not ificationLayout, musicData)
//        loadExpandedDetails(notificationLayoutExpanded, musicData)
        notificationLayout.setTextViewText(R.id.notification_title, musicData.title)

        notificationLayoutExpanded.setTextViewText(R.id.title, musicData.title)
        notificationLayoutExpanded.setTextViewText(R.id.subtitle, musicData.description)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationLayoutExpanded.setColorAttr(R.id.line1,  "",  musicData.color)
        }

        val customNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("My notification")
//            .setContentText("Hello World!")
//            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setContent(notificationLayoutExpanded)
//            .setCustomContentView(notificationLayoutExpanded)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
//            .setCustomBigContentView(notificationLayoutExpanded)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setLights(Color.WHITE, 500, 500)
            .build()

//        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
//            .setSmallIcon(R.drawable.notification_icon)
//            .setContentTitle("My notification")
//            .setContentText("Hello World!")
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setContentIntent(pendingIntent)
//            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, customNotification)
        }
    }

    private fun loadSmallNotification(notificationLayout: RemoteViews, musicData: MusicData) {
        notificationLayout.setTextViewText(R.id.notification_title, musicData.title)
    }

    private fun loadExpandedDetails(notificationLayoutExpanded: RemoteViews, musicData: MusicData){
        notificationLayoutExpanded.setTextViewText(R.id.title, musicData.title)
        notificationLayoutExpanded.setTextViewText(R.id.subtitle, musicData.description)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notificationLayoutExpanded.setColorAttr(R.id.line1,  "",  musicData.color)
        }
    }

}