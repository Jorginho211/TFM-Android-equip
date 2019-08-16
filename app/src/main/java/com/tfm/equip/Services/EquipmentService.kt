package com.tfm.equip.Services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.tfm.equip.BeaconProvider.*
import com.tfm.equip.Utils.Constants
import java.util.*
import kotlin.collections.ArrayList
import android.app.Notification
import android.support.v4.app.NotificationCompat
import com.tfm.equip.Activities.MainActivity
import android.app.PendingIntent
import android.os.VibrationEffect
import android.os.Vibrator
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

        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0,
            notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT
        )
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID_SERVICE)
            .setSmallIcon(android.R.mipmap.sym_def_app_icon)
            .setContentTitle(getString(R.string.equipment_service_notification_title))
            .setContentText(getString(R.string.equipment_service_notification_substitle))
            .setContentIntent(pendingIntent)
            .build()

        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT

        startForeground(SERVICE_ID, notification)
        beaconProvider.start(beaconCallback)
        SharedData.EquipmentServiceIsRunning = true

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
}