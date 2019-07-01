package com.tfm.equip.BeaconProvider

interface IBeaconProvider {
    fun start(callback: IBeaconCallback)
    fun stop()
}