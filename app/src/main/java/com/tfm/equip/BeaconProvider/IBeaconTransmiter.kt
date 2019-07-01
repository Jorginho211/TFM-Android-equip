package com.tfm.equip.BeaconProvider

interface IBeaconTransmiter {
    fun start(beacon:Beacon)
    fun stop()
}