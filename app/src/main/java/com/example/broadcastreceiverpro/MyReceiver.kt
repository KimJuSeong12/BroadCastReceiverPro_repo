package com.example.broadcastreceiverpro

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class MyReceiver : BroadcastReceiver() {
    lateinit var manager: NotificationManager

    override fun onReceive(context: Context, intent: Intent) {
        //1.notificationCompat.Builder 객체참조변수
        val builder: NotificationCompat.Builder
        manager =
            context.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        //2.channel 객체참조변수를 만든다. (API 26버전 이상부터 채널을 만들어줘야됨)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //3.26버전 이상 채널객체참조변수
            val channelID: String = "kjs-channel"
            val channelName = "MY KJS Channel"
            val channel =
                NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH)
            //채널에 대한 정보등록
            channel.description + "My KJS Description"
            channel.setShowBadge(true)
            //알림음오디오 설정
            val notificationUri: Uri =
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributesBuilder = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()
            channel.setSound(notificationUri, audioAttributesBuilder)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

            //4. 채널을 notificationManager 등록
            manager.createNotificationChannel(channel)
            //5.채널아이디를 이용해서 빌더생성
            builder = NotificationCompat.Builder(context, channelID)
        } else {
            //채널아이디를 이용하지 않고 빌더생성
            builder = NotificationCompat.Builder(context)
        }

        //6. builder 알림창이 어떤 방법으로 구현할지 보여주는것
        builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
        builder.setWhen(System.currentTimeMillis())
        builder.setContentTitle("Battery Information")
        builder.setContentText("Battery Percent = ${intent.getStringExtra("batteryPercent")}")

        //10. manager 알림발생
        manager.notify(11, builder.build())

    }
}