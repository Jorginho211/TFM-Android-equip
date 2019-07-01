package com.tfm.equip.BeaconProvider

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import org.altbeacon.beacon.*
import org.altbeacon.beacon.Beacon

class BeaconProvider: IBeaconProvider, BeaconConsumer, RangeNotifier {
    private var context: Context
    private lateinit var callback: IBeaconCallback
    private var beaconManager: BeaconManager? = null
    private var region:Region = Region("Region", null, null, null)
    private var running:Boolean = false

    private val IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"

    constructor(context: Context) {
        this.context = context
    }

    override fun getApplicationContext(): Context {
        return context
    }

    override fun onBeaconServiceConnect() {
        this.beaconManager?.startRangingBeaconsInRegion(region)
        this.beaconManager?.addRangeNotifier(this)
    }

    override fun didRangeBeaconsInRegion(p0: MutableCollection<Beacon>?, p1: Region?) {
        if(p0 == null) return

        var beacons:ArrayList<com.tfm.equip.BeaconProvider.Beacon> = ArrayList()

        for(b in p0.iterator()){
            beacons.add(Beacon(b.id1.toUuid().toString(), b.rssi, b.id2.toInt(), b.id3.toInt(), b.txPower, b.distance))
        }

        callback.onBeaconsDetected(beacons)
    }

    override fun start(callback: IBeaconCallback) {
        if(running) return

        running = true
        this.callback = callback

        region = Region(Math.random().toString(), null, null, null)
        this.beaconManager = BeaconManager.getInstanceForApplication(context)
        var beaconParser:BeaconParser = BeaconParser()
            .setBeaconLayout(IBEACON_LAYOUT)

        this.beaconManager!!.beaconParsers.add(beaconParser)
        this.beaconManager!!.foregroundScanPeriod = 1000
        this.beaconManager!!.backgroundScanPeriod = 1000
        this.beaconManager!!.foregroundBetweenScanPeriod = 0
        this.beaconManager!!.backgroundBetweenScanPeriod = 0
        this.beaconManager!!.bind(this)
    }

    override fun stop() {
        if(!running) return

        running = false
        this.beaconManager?.stopRangingBeaconsInRegion(region)
        this.beaconManager?.stopMonitoringBeaconsInRegion(region)
        this.beaconManager?.removeAllRangeNotifiers()
        this.beaconManager?.unbind(this)
    }

    override fun unbindService(p0: ServiceConnection?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean {
        return true
    }
}