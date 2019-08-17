package com.tfm.equip.Services

import android.app.*
import android.arch.persistence.room.Update
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.tfm.equip.BeaconProvider.*
import com.tfm.equip.Utils.Constants
import kotlin.collections.ArrayList
import android.support.v4.app.NotificationCompat
import android.content.Context
import android.os.Build
import com.tfm.equip.Activities.PrincipalActivity
import com.tfm.equip.R
import com.tfm.equip.Services.Extra.PlaceEquipmentsStateUpdater
import com.tfm.equip.Utils.RestApi
import com.tfm.equip.Utils.SharedData
import java.util.*


class EquipmentService: Service() {
    private val beaconProvider: IBeaconProvider = BeaconProvider(this)
    private val beaconTransmiter: IBeaconTransmiter = BeaconTransmiter(this)
    private val NOTIFICATION_CHANNEL_ID_SERVICE:String = "com.tfm.equip.Services.EquipmentService"
    private val SERVICE_ID:Int = 9201

    private var isTransmiting:Boolean = false

    private lateinit var transmitionTimer: Timer
    private lateinit var checkerInterval: Timer
    private lateinit var updateMonitorDataInterval: Timer
    private lateinit var placeEquipmentsStateUpdate: PlaceEquipmentsStateUpdater

    private val beaconCallback: IBeaconCallback = object:IBeaconCallback {
        override fun onBeaconsDetected(beacons: ArrayList<Beacon>) {
            var placeBeacon: Beacon? = null
            var equipmentBeacons:ArrayList<Beacon> = ArrayList()

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
                    equipmentBeacons.add(beacon)
                }
            }

            placeEquipmentsStateUpdate.updateEquipmentsPlace(equipmentBeacons, placeBeacon)

            if(placeBeacon === null || beaconToSend.minor === 0 || placeBeacon.getCalculatedDistance() > Constants.MAX_DISTANCE_PLACE) return

            if(isTransmiting) {
                transmitionTimer.cancel()
            }

            beaconTransmiter.start(beaconToSend)
            isTransmiting = true

            transmitionTimer = Timer()
            transmitionTimer.schedule(object:TimerTask() {
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
            .setContentText(getString(R.string.equipment_service_notification_subtitle))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        createNotificationChannel()

        var notification = builder.build()
        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT

        placeEquipmentsStateUpdate = PlaceEquipmentsStateUpdater(this, Constants.MAX_TIME_LAST_SEEN_MS)
        beaconProvider.start(beaconCallback)
        SharedData.EquipmentServiceIsRunning = true
        checkerInterval = Timer()
        checkerInterval.schedule(object: TimerTask(){
            override fun run() {
                placeEquipmentsStateUpdate.updateState()
            }
        }, Constants.INTERVAL_CHECK_PLACE_EQUIPMENT, Constants.INTERVAL_CHECK_PLACE_EQUIPMENT)

        updateMonitorDataInterval = Timer()
        updateMonitorDataInterval.schedule(object:TimerTask(){
            override fun run() {
                val restApi:RestApi = RestApi()
                var idPlace:Int = -1
                if(SharedData.PlaceState !== null){
                    idPlace = SharedData.PlaceState!!.id
                }

                var equipmentsId:Array<Int> = SharedData.EquipmentsState.map{ e -> e.id}.toTypedArray()
                restApi.uploadMonitorData(SharedData.User!!.id, idPlace, equipmentsId, SharedData.User!!.Token)
            }

        }, SharedData.User!!.FrequencySendData * 1000, SharedData.User!!.FrequencySendData * 1000)

        startForeground(SERVICE_ID, notification)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        beaconTransmiter.stop()
        beaconProvider.stop()
        isTransmiting = false
        SharedData.EquipmentServiceIsRunning = false
        checkerInterval.cancel()
        updateMonitorDataInterval.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.equipment_service_notification_title)
            val descriptionText = getString(R.string.equipment_service_notification_subtitle)
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