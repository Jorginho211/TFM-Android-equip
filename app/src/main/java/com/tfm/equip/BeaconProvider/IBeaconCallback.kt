package com.tfm.equip.BeaconProvider

interface IBeaconCallback {
    fun onBeaconsDetected(beacons: ArrayList<Beacon>)
    fun onError()
}