package com.tfm.equip.BeaconProvider

import android.os.Parcel
import android.os.Parcelable


class Beacon: Parcelable {
    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var uuid:String
    var rssi:Int
    var major:Int
    var minor:Int
    var txPower: Int
    var distance: Double
    var lastSeen: Long


    constructor(uuid: String, rssi: Int, major: Int, minor: Int, txPower: Int, distance: Double) {
        this.uuid = uuid
        this.rssi = rssi
        this.major = major
        this.minor = minor
        this.txPower = txPower
        this.distance = distance
        this.lastSeen = System.currentTimeMillis()
    }

    constructor(parcel: Parcel) {
        this.uuid = parcel.readString()!!
        this.rssi = parcel.readInt()
        this.major = parcel.readInt()
        this.minor = parcel.readInt()
        this.txPower = parcel.readInt()
        this.distance = parcel.readDouble()
        this.lastSeen = System.currentTimeMillis()
    }

    fun getCalculatedDistance(): Double {
        var distance:Double

        if (rssi === 0) {
            return -1.0
        }

        val ratio = rssi * 1.0 / txPower
        if (ratio < 1.0) {
            distance = Math.pow(ratio, 10.0)
        } else {
            distance = 0.89976 * Math.pow(ratio, 7.7095) + 0.111
        }

        return distance
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(uuid)
        dest.writeInt(rssi)
        dest.writeInt(major)
        dest.writeInt(minor)
        dest.writeInt(txPower)
        dest.writeDouble(distance)
        dest.writeLong(lastSeen)
    }

    companion object CREATOR : Parcelable.Creator<Beacon> {
        override fun createFromParcel(parcel: Parcel): Beacon {
            return createFromParcel(parcel)
        }

        override fun newArray(size: Int): Array<Beacon?> {
            return arrayOfNulls(size)
        }
    }
}
