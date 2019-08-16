package com.tfm.equip.Utils

import com.tfm.equip.Database.Entities.UserEntity

class SharedData {
    companion object{
        var User:UserEntity? = null
        var EquipmentServiceIsRunning: Boolean = false
    }
}