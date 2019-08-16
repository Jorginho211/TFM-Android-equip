package com.tfm.equip.Utils

class Constants {
    companion object {
        const val UUID_EQUIPMENT:String = "fffffffe-ffff-fffe-ffff-fffefffffffe"
        const val UUID_PLACE:String = "fffffffe-ffff-fffe-ffff-fffefffffffd"

        const val MAX_DISTANCE_EQUIPMENT:Int = 2
        const val MAX_DISTANCE_PLACE:Int = 5

        const val MAX_TIME_LAST_SEEN_MS:Long = 3000
        const val INTERVAL_CHECK_PLACE_EQUIPMENT:Long = 1000
    }
}