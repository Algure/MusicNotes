package com.algure.musicnotes.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.algure.musicnotes.MainActivity
import com.algure.musicnotes.R
import com.algure.musicnotes.objects.MusicData
import com.algure.musicnotes.provider.DataProvider


class NotifierService  : Service() {
    private var startMode: Int = 0
    private var binder: IBinder? = null
    private var allowRebind: Boolean = false


    companion object {
        private var currentMusic = 0

        fun getCurrentMusic(): Int {
            return currentMusic
        }

        fun setCurrentMusic(i:Int) {
            currentMusic = i
        }
    }


    override fun onCreate() {

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var todo = ""
        if (intent != null) {
            todo = intent.getBundleExtra("whattodo").toString()
            if (todo == "null") {
                runNotes(currentMusic)
            } else if (todo == "next") {
                runNotes(++currentMusic)
            } else if (todo == "prev") {
                runNotes(--currentMusic)
            } else {
                runNotes(currentMusic)
            }
        } else {
            runNotes(currentMusic)
        }
        Log.i("todo", "todo: $todo")

        Toast.makeText(applicationContext, "Started service ", Toast.LENGTH_LONG).show()
        return startMode
    }


    private fun runNotes(currentMusic: Int) {
        Log.i("currentMusic", "currentMusic: $currentMusic")
        if (currentMusic >= DataProvider.musicTestData.size || currentMusic < 0) {
            this.stopSelf()
        } else {
            Thread(
                NoteCountRunnable(musicData = DataProvider.musicTestData[currentMusic],
                    context = this,
                    doWhenDone = { onNoteRan() })
            ).run()
        }
    }


    private fun onNoteRan() {
        runNotes(++currentMusic)
    }

    override fun onBind(intent: Intent): IBinder? {
        return binder
    }


    override fun onUnbind(intent: Intent): Boolean {
        return allowRebind
    }


    override fun onRebind(intent: Intent) {

    }

    override fun onDestroy() {

    }


    inner class NoticeBinder : Binder() {
        fun getService(): NotifierService = this@NotifierService
    }
}


class NoteCountRunnable(musicData: MusicData, context:Context,  doWhenDone:()-> Unit): Runnable {
    private val notificationId: Int = 1001
    private val CHANNEL_ID: String = "NotesAppID"
    private var context: Context

    private var doWhenDone:() -> Unit

    var musicData:MusicData

    init {
        this.musicData = musicData
        this.context = context

        this.doWhenDone = doWhenDone
    }


    override fun run() {
        println("${Thread.currentThread()} has run.")
        var notificationBuiler = makeNShowNotificationWithDets(null, 0)

        var totalSecs = 0

        makeNShowNotificationWithDets(notificationBuiler, totalSecs)
        Thread.sleep((1000 *  musicData.lengthInSec).toLong())
        if (DataProvider.musicTestData[NotifierService.getCurrentMusic()] != musicData){
           return
        }
        doWhenDone.invoke()
    }


    fun makeNShowNotificationWithDets( customNotificationBuilder : NotificationCompat.Builder?, secs:Int = 0) :
            NotificationCompat.Builder {
        val normIntent = Intent(context, MainActivity::class.java)
        val nextIntent = Intent(context, NotifierService::class.java)
        val prevIntent = Intent(context, NotifierService::class.java)

        nextIntent.putExtra("whattodo", "next")
        prevIntent.putExtra("whattodo", "prev")

        val pendingIntent: PendingIntent = PendingIntent.getService(context, 0,
            normIntent, PendingIntent.FLAG_IMMUTABLE)
        val prevPendingIntent: PendingIntent = PendingIntent.getService(context, 0,
            prevIntent, PendingIntent.FLAG_IMMUTABLE)
        val nextPendingIntent: PendingIntent = PendingIntent.getService(context, 0,
            nextIntent, PendingIntent.FLAG_IMMUTABLE)


        createNotificationChannel()
        val notificationLayout = RemoteViews(context.getPackageName(), R.layout.notification_small)
        val notificationLayoutExpanded = RemoteViews(context.getPackageName(), R.layout.notification_large)

        loadSmallNotification(notificationLayout, musicData)
        loadExpandedDetails(notificationLayoutExpanded, musicData, secs)

        notificationLayout.setOnClickPendingIntent(R.id.backbtn, prevPendingIntent)
        notificationLayout.setOnClickPendingIntent(R.id.nextbtn, nextPendingIntent)

        notificationLayout.setTextViewText(R.id.notification_title, musicData.title)

        if(customNotificationBuilder == null) {
            var noteBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setContent(notificationLayout)
                .setCustomContentView(notificationLayout)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setCustomBigContentView(notificationLayoutExpanded)
                .setAutoCancel(true)
                .setColor(musicData.color)
                .setColorized(true)
                .setLights(musicData.color, 500, 500)
            return noteBuilder
        }
        customNotificationBuilder.setCustomBigContentView(notificationLayoutExpanded)
        customNotificationBuilder.setContent(notificationLayout)
        customNotificationBuilder.setCustomContentView(notificationLayout)

        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, customNotificationBuilder.build())
        }
        return customNotificationBuilder
    }


    private fun loadSmallNotification(notificationLayout: RemoteViews, musicData: MusicData) {
        notificationLayout.setTextViewText(R.id.notification_title, musicData.title)
    }


    private fun loadExpandedDetails(
        notificationLayoutExpanded: RemoteViews,
        musicData: MusicData,
        secs: Int
    ){
        notificationLayoutExpanded.setTextViewText(R.id.title, musicData.title)
        notificationLayoutExpanded.setTextViewText(R.id.subtitle, musicData.description)
        notificationLayoutExpanded.setProgressBar(R.id.progress, musicData.lengthInSec, secs, true)
        notificationLayoutExpanded.setInt(R.id.line1, "setBackgroundColor", musicData.color)
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "getString(R.string.channel_name)"
            val descriptionText = "getString(R.string.channel_description)"
            val  importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}
