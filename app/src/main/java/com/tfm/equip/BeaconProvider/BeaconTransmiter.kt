package com.tfm.equip.BeaconProvider

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.content.Context
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconTransmitter


class BeaconTransmiter: IBeaconTransmiter {
    private var context:Context
    private var beaconTransmitter: BeaconTransmitter? = null
    private var running:Boolean = false

    private val IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"

    constructor(context: Context) {
        this.context = context
    }


    override fun start(beacon: Beacon) {
        if(running) return

        running = true
        var beaconNative:org.altbeacon.beacon.Beacon = org.altbeacon.beacon.Beacon.Builder()
            .setId1(beacon.uuid)
            .setId2(beacon.major.toString())
            .setId3(beacon.minor.toString())
            .setManufacturer(0x004c) //
            .setTxPower(beacon.txPower)
            .build()

        if(beaconTransmitter == null){
            val beaconParser:BeaconParser = BeaconParser()
                .setBeaconLayout(IBEACON_LAYOUT)

            beaconTransmitter = BeaconTransmitter(this.context, beaconParser)
        }

        beaconTransmitter?.stopAdvertising()
        beaconTransmitter?.advertiseMode = AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY
        beaconTransmitter?.advertiseTxPowerLevel = AdvertiseSettings.ADVERTISE_TX_POWER_HIGH
        beaconTransmitter?.startAdvertising(beaconNative, object:AdvertiseCallback() {
            override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
                super.onStartSuccess(settingsInEffect)
            }

            override fun onStartFailure(errorCode: Int) {
                super.onStartFailure(errorCode)
            }
        })
    }

    override fun stop() {
        if(!running) return

        running = false
        beaconTransmitter?.stopAdvertising()

    }
}