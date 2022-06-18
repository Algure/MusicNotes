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
import androidx.core.app.NotificationCompat
import com.algure.musicnotes.objects.MusicData
import androidx.core.app.NotificationManagerCompat





class MainActivity : AppCompatActivity() {
    private val notificationId: Int = 900
    private val CHANNEL_ID: String = "CHANNEL_U"

    val musicTestData:List<MusicData> = mutableListOf(
        MusicData(title = "Test title 1", description = "dfvcdfvdfvd fvdfnv", color = Color.BLACK),
        MusicData(title = "Test title 2", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#FF0000")),
        MusicData(title = "Test title 3", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#11AA00")),
        MusicData(title = "Test title 4", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#22AA99")),
        MusicData(title = "Test title 5", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#22AA55")),
        MusicData(title = "Test title 6", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#1100FF")),
        MusicData(title = "Test title 7", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#FF00FF")),
        MusicData(title = "Test title 8", description = "dfvcdfvdfvd fvdfnv", color = Color.parseColor("#AA00FF")),
    )


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


    fun showNotification(){
        showNotificationWithDets("Hi world", "cscs sfsnflj sdfsfjf disjdcij")
    }


    fun showNotificationWithDets(textContent:String, textTitle:String){
        val musicData = MusicData(
            title = "Test title",
            description = "srfes fdvsefr sfsfs fsefk srfserf fesrf sfrserf knnkkk kk ",
            color = Color.CYAN
        )

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0,
            intent, PendingIntent.FLAG_IMMUTABLE)

        createNotificationChannel()

        val notificationLayout = RemoteViews(getPackageName(), R.layout.notification_small)
        val notificationLayoutExpanded = RemoteViews(getPackageName(), R.layout.notification_large)

        loadSmallNotification(notificationLayout, musicData)
        loadExpandedDetails(notificationLayoutExpanded, musicData)

        notificationLayout.setTextViewText(R.id.notification_title, musicData.title)

        val customNotification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("My notification")
            .setContentText("Hello World!")
            .setContent(notificationLayout)
            .setCustomContentView(notificationLayout)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setCustomBigContentView(notificationLayoutExpanded)
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setColor(musicData.color)
            .setColorized(true)
            .setLights(musicData.color, 500, 500)
            .build()



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
        notificationLayoutExpanded.setInt(R.id.line1, "setBackgroundColor", musicData.color)
    }

}