package com.tfm.equip.Utils

import com.tfm.equip.Database.Entities.EquipmentEntity
import com.tfm.equip.Database.Entities.PlaceEntity
import com.tfm.equip.Database.Entities.UserEntity

class SharedData {
    companion object{
        var User:UserEntity? = null
        var EquipmentServiceIsRunning: Boolean = false

        var PlaceState: PlaceEntity? = null
        var EquipmentsPlaceStateRequired: ArrayList<EquipmentEntity> = ArrayList()
        var EquipmentsState: ArrayList<EquipmentEntity> = ArrayList()
    }
}