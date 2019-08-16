package com.tfm.equip.Services.Extra

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import com.tfm.equip.BeaconProvider.Beacon
import com.tfm.equip.Database.AppDatabase
import com.tfm.equip.Database.Entities.EquipmentEntity
import com.tfm.equip.Utils.SharedData
import android.content.Context.VIBRATOR_SERVICE
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.support.v4.content.ContextCompat.getSystemService
import android.os.Vibrator
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.tfm.equip.Activities.PrincipalActivity
import com.tfm.equip.Activities.StateActivity
import com.tfm.equip.R


class PlaceEquipmentsStateUpdater(context:Context, maxTimeLastSeenMs:Long) {
    private var equipmentsBeacons:ArrayList<Beacon> = ArrayList()
    private var lastPlace:Beacon? = null
    private var db:AppDatabase
    private var maxTimeLastSeenMs:Long
    private var lastVibrateForEquipment: Boolean
    private var context:Context
    private val NOTIFICATION_CHANNEL_ID_EQUIPMENT_MISSING:String = "com.tfm.equip.Services.Extra.PlaceEquipmentsStateUpdater"
    private val NOTIFICATION_ID:Int = 12352

    init{
        lastVibrateForEquipment = false
        SharedData.PlaceState = null
        SharedData.EquipmentsPlaceStateRequired = ArrayList()

        this.context = context
        this.db = AppDatabase.getInstance(context)
        this.maxTimeLastSeenMs = maxTimeLastSeenMs
    }

    fun updateEquipmentsPlace(equipments:ArrayList<Beacon>, place:Beacon?) {
        for(eq in equipmentsBeacons){
            var eqFind:Beacon? = equipments.find { e -> e.uuid == eq.uuid && e.major === eq.major && e.minor === eq.minor }

            if(eqFind === null && (System.currentTimeMillis() - eq.lastSeen) <= maxTimeLastSeenMs){
                equipments.add(eq)
            }
        }

        equipmentsBeacons = equipments

        if(lastPlace === null && place !== null){
            lastPlace = place
        }
        else if(lastPlace !== null && place !== null && lastPlace!!.uuid == place.uuid && lastPlace!!.major === place.major && lastPlace!!.minor === place.minor){
            lastPlace = place
        }
        else if(lastPlace !== null && place !== null && System.currentTimeMillis() - lastPlace!!.lastSeen > maxTimeLastSeenMs){
            lastPlace = place
        }
        else if(lastPlace !== null && place !== null && place.getCalculatedDistance() < lastPlace!!.getCalculatedDistance()){
            lastPlace = place
        }
        else {
            lastPlace = null
        }
    }

    fun updateState(){
        if(lastPlace !== null){
            var vibrate:Boolean = true
            if(SharedData.PlaceState !== null && lastPlace!!.major === SharedData.PlaceState!!.Major && lastPlace!!.minor === SharedData.PlaceState!!.Minor){
                vibrate = false
            }

            SharedData.PlaceState = db.placeDao().getPlaceByMajorMinor(lastPlace!!.major, lastPlace!!.minor)
            if(SharedData.PlaceState !== null && !SharedData.PlaceState!!.HasPermission && vibrate){
                Log.i("AREA NO PERMISO:", SharedData.PlaceState!!.Name)
            }

            if(SharedData.PlaceState  !== null && SharedData.PlaceState!!.HasPermission){
                SharedData.EquipmentsPlaceStateRequired = db.placeHasEquipmentDao().getEquipmentsForPlace(SharedData.PlaceState!!.id) as ArrayList<EquipmentEntity>
            }
        }

        SharedData.EquipmentsState = ArrayList()
        for (eq in equipmentsBeacons){
            SharedData.EquipmentsState.add(db.equipmentDao().getEquipmentByMajorMinor(eq.major, eq.minor))
        }

        var hasRequiredEquipment:Boolean = true
        var stringEquipmentsMissing:String = ""
        for(eq in SharedData.EquipmentsPlaceStateRequired){
            if(SharedData.EquipmentsState.find{ e -> e.id === eq.id } === null){
                hasRequiredEquipment = false
                stringEquipmentsMissing += eq.Name + ", "
            }
        }

        if(hasRequiredEquipment) {
            lastVibrateForEquipment = false
        }
        else if(!lastVibrateForEquipment){
            lastVibrateForEquipment = true
            vibrate()
            stringEquipmentsMissing = stringEquipmentsMissing.trim(',', ' ')
            showNotification(stringEquipmentsMissing)
        }
    }

    private fun vibrate(vibrationDurationMs:Long = 1000){
        val vibrator = this.context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(vibrationDurationMs, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(vibrationDurationMs)
        }
    }

    private fun showNotification(stringEquipmentsMissing: String){
        val notificationIntent = Intent(this.context, StateActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent = PendingIntent.getActivity(
            this.context, 0,
            notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT
        )

        var builder = NotificationCompat.Builder(this.context, NOTIFICATION_CHANNEL_ID_EQUIPMENT_MISSING)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(context.getString(R.string.equipment_service_equipment_missing_notification_title))
            .setContentText(stringEquipmentsMissing)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        createNotificationChannel()

        var notification = builder.build()

        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Falta equipamento para a area"
            val descriptionText = "Describe o equipamento que falta para poder acceder a certa area"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID_EQUIPMENT_MISSING, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}