package com.tfm.equip

import android.Manifest
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tfm.equip.BeaconProvider.*
import com.tfm.equip.Utils.Constants
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private var equipmentBeacons: ArrayList<Beacon> = ArrayList()
    private val beaconProvider: IBeaconProvider = BeaconProvider(this)
    private val beaconTransmiter: IBeaconTransmiter = BeaconTransmiter(this)

    private var isTransmiting:Boolean = false

    private lateinit var timer:Timer

    private val beaconCallback: IBeaconCallback = object:IBeaconCallback {
        override fun onBeaconsDetected(beacons: ArrayList<Beacon>) {
            equipmentBeacons.clear()
            var placeBeacon: Beacon? = null
            var beaconToSend: Beacon = Beacon(Constants.UUID_WORKER, 10, 1, 0, -80, 0.0)
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

            if(placeBeacon === null || beaconToSend.minor === 0 || placeBeacon.getCalculatedDistance() > Constants.MAX_DISTANCE_PLACE) return
            //beaconProvider.stop()

            if(isTransmiting) {
                timer?.cancel()
            }

            beaconTransmiter.start(beaconToSend)
            isTransmiting = true

            timer = Timer()
            timer.schedule(object:TimerTask() {
                override fun run() {
                    isTransmiting = false
                    beaconTransmiter.stop()
                }
            }, 2000)
        }

        override fun onError() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissionAndRequest()
    }

    fun checkPermissionAndRequest(){
        if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        }
        else {
            beaconProvider.start(beaconCallback)
            //beaconTransmiter.start(Beacon(Constants.UUID_WORKER, 10, 1, 3, -65, 0.0))
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1 -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    beaconProvider.start(beaconCallback)
                    //beaconTransmiter.start(Beacon(Constants.UUID_WORKER, 10, 1, 3, -65, 0.0))
                }
            }
        }
    }
}
