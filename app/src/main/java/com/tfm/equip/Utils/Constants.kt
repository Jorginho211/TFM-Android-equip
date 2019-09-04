package com.tfm.equip.Utils

class Constants {
    companion object {
        const val UUID_EQUIPMENT:String = "fffffffe-ffff-fffe-ffff-fffefffffffe"
        const val UUID_PLACE:String = "fffffffe-ffff-fffe-ffff-fffefffffffd"

        const val MAX_DISTANCE_EQUIPMENT:Double = 1.0
        const val MAX_DISTANCE_PLACE:Double = 2.0

        const val MAX_TIME_LAST_SEEN_MS:Long = 3000
        const val INTERVAL_CHECK_PLACE_EQUIPMENT:Long = 1000
    }
}