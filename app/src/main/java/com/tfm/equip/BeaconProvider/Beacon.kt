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


    constructor(uuid: String, rssi: Int, major: Int, minor: Int, txPower: Int, distance: Double) {
        this.uuid = uuid
        this.rssi = rssi
        this.major = major
        this.minor = minor
        this.txPower = txPower
        this.distance = distance
    }

    constructor(parcel: Parcel) {
        this.uuid = parcel.readString()!!
        this.rssi = parcel.readInt()
        this.major = parcel.readInt()
        this.minor = parcel.readInt()
        this.txPower = parcel.readInt()
        this.distance = parcel.readDouble()
    }

    fun getCalculatedDistance(): Double {
        val n: Int = 2

        val distance:Double =  Math.pow(10.0, ((this.txPower - this.rssi)/(10.0 * n)))
        return distance
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(uuid)
        dest.writeInt(rssi)
        dest.writeInt(major)
        dest.writeInt(minor)
        dest.writeInt(txPower)
        dest.writeDouble(distance)
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
