package com.algure.musicnotes.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.algure.musicnotes.MainActivity
import com.algure.musicnotes.R
import com.algure.musicnotes.objects.MusicData

class NotifierService  : Service() {
    private var startMode: Int = 0             // indicates how to behave if the service is killed
    private var binder: IBinder? = null        // interface for clients that bind
    private var allowRebind: Boolean = false   // indicates whether onRebind should be used

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

    override fun onCreate() {

    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return startMode
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



}

class NoteCountRunnable(musicData: MusicData, context:Context,  doWhenDone:() ->Void): Runnable {

    private val notificationId: Int = 1001
    private val CHANNEL_ID: String = "NotesAppID"
    private var context: Context

    private var doWhenDone:() -> Void

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

        while(totalSecs < musicData.lengthInSec){
            makeNShowNotificationWithDets(notificationBuiler, totalSecs)
            Thread.sleep(1000)
            totalSecs ++
        }
        doWhenDone.invoke()
    }


    fun makeNShowNotificationWithDets( customNotificationBuilder : NotificationCompat.Builder?, secs:Int = 0) : NotificationCompat.Builder {

        val normIntent = Intent(context, MainActivity::class.java);
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
                .setDefaults(Notification.DEFAULT_SOUND)
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
        return  customNotificationBuilder
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
