package com.tfm.equip.Services

import android.app.*
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.tfm.equip.BeaconProvider.*
import com.tfm.equip.Utils.Constants
import java.util.*
import kotlin.collections.ArrayList
import android.support.v4.app.NotificationCompat
import android.content.Context
import android.os.Build
import com.tfm.equip.Activities.PrincipalActivity
import com.tfm.equip.R
import com.tfm.equip.Utils.SharedData


class EquipmentService: Service() {
    private var equipmentBeacons: ArrayList<Beacon> = ArrayList()
    private val beaconProvider: IBeaconProvider = BeaconProvider(this)
    private val beaconTransmiter: IBeaconTransmiter = BeaconTransmiter(this)
    val NOTIFICATION_CHANNEL_ID_SERVICE:String = "com.tfm.equip.Services.EquipmentService"
    val SERVICE_ID:Int = 9201

    private var isTransmiting:Boolean = false

    private lateinit var timer: Timer

    private val beaconCallback: IBeaconCallback = object:IBeaconCallback {
        override fun onBeaconsDetected(beacons: ArrayList<Beacon>) {
            var placeBeacon: Beacon? = null
            var beaconToSend: Beacon = Beacon(SharedData.User?.uuid!!, 10, 1, 0, -80, 0.0)
            for(beacon in beacons){
                Log.i("Beacon", "UUID: " + beacon.uuid + "\tMAJOR: " + beacon.major + "\tMINOR: " + beacon.minor + "\tTXPOWER: " + beacon.txPower + "\t" + "RSSI: " + beacon.rssi + "\tDISTANCE2: " + beacon.getCalculatedDistance() + "\tDISTANCE 1: " + beacon.distance)
                if(!beacon.uuid.equals(Constants.UUID_EQUIPMENT) && !beacon.uuid.equals(Constants.UUID_PLACE)) continue

                if(beacon.uuid.equals(Constants.UUID_PLACE)){
                    if((placeBeacon != null && placeBeacon.distance > beacon.getCalculatedDistance()) || placeBeacon == null){
                        placeBeacon = beacon
                    }
                }
                else if(beacon.uuid.equals(Constants.UUID_EQUIPMENT) && beacon.getCalculatedDistance() <= Constants.MAX_DISTANCE_EQUIPMENT){
                    beaconToSend.minor = beaconToSend.minor or beacon.minor
                }
            }

            if(placeBeacon === null || beaconToSend.minor === 0 || placeBeacon.getCalculatedDistance() > Constants.MAX_DISTANCE_PLACE) return

            if(isTransmiting) {
                timer.cancel()
            }

            beaconTransmiter.start(beaconToSend)
            isTransmiting = true

            timer = Timer()
            timer.schedule(object:TimerTask() {
                override fun run() {
                    isTransmiting = false
                    beaconTransmiter.stop()
                }
            }, 3000)
        }

        override fun onError() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        val notificationIntent = Intent(applicationContext, PrincipalActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0,
            notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT
        )

        var builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_SERVICE)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.equipment_service_notification_title))
            .setContentText(getString(R.string.equipment_service_notification_substitle))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        createNotificationChannel()

        var notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT

        beaconProvider.start(beaconCallback)
        SharedData.EquipmentServiceIsRunning = true

        startForeground(SERVICE_ID, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconTransmiter.stop()
        beaconProvider.stop()
        isTransmiting = false
        SharedData.EquipmentServiceIsRunning = false
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.equipment_service_notification_title)
            val descriptionText = getString(R.string.equipment_service_notification_substitle)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}