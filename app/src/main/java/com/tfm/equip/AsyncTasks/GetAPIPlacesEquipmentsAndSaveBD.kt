package com.tfm.equip.AsyncTasks

import android.content.Context
import android.os.AsyncTask
import com.tfm.equip.DTOs.EquipmentDTO
import com.tfm.equip.DTOs.PlaceDTO
import com.tfm.equip.Database.AppDatabase
import com.tfm.equip.Database.Entities.EquipmentEntity
import com.tfm.equip.Database.Entities.PlaceEntity
import com.tfm.equip.Database.Entities.PlaceHasEquipmentEntity
import com.tfm.equip.Database.Entities.UserEntity
import com.tfm.equip.Utils.CallbackInterface
import com.tfm.equip.Utils.RestApi

class GetAPIPlacesEquipmentsAndSaveBD(context: Context, callback:CallbackInterface<Boolean>): AsyncTask<UserEntity,  Void, Boolean>(){
    val db:AppDatabase
    val callback:CallbackInterface<Boolean>

    init {
        this.db = AppDatabase.getInstance(context)
        this.callback = callback
    }

    override fun doInBackground(vararg params: UserEntity?): Boolean {
        val userEntity:UserEntity = params[0]!!
        val restApi = RestApi()

        val placesDTO: ArrayList<PlaceDTO>? = restApi.getPlacesSync(userEntity.id, userEntity.Token!!)
        if(placesDTO === null){
            return false
        }

        val allPlacesDTO:ArrayList<PlaceDTO>? = restApi.getAllPlacesSync(userEntity.Token!!)
        if(allPlacesDTO === null){
            return false
        }

        var equipmentsDTO: ArrayList<EquipmentDTO>? = restApi.getEquipmentsSync(userEntity.Token!!)
        if(equipmentsDTO === null){
            return false
        }

        db.placeDao().deleteAll()
        db.equipmentDao().deleteAll()

        for(placeDTO in allPlacesDTO){
            var hasPermission:Boolean = false

            if(placesDTO.find { p -> p.id === placeDTO.id } !== null){
                hasPermission = true
            }

            db.placeDao().insert(PlaceEntity(placeDTO.id, placeDTO.name, placeDTO.major, placeDTO.minor, hasPermission))
        }

        for(equipmentDTO in equipmentsDTO){
            db.equipmentDao().insert(EquipmentEntity(equipmentDTO.id, equipmentDTO.name, equipmentDTO.major, equipmentDTO.minor))
        }

        for(placeDTO in placesDTO){
            equipmentsDTO = restApi.getPlaceEquipmentsSync(placeDTO.id, userEntity.Token!!)

            if(equipmentsDTO === null) continue

            for(equipmentDTO in equipmentsDTO){
                db.placeHasEquipmentDao().insert(PlaceHasEquipmentEntity(placeDTO.id, equipmentDTO.id))
            }
        }

        return true
    }


    override fun onPostExecute(result: Boolean) {
        super.onPostExecute(result)
        callback.doCallback(result)
    }
}