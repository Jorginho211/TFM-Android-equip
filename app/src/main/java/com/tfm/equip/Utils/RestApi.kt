package com.tfm.equip.Utils

import com.tfm.equip.DTOs.EquipmentDTO
import com.tfm.equip.DTOs.PlaceDTO
import com.tfm.equip.DTOs.UserDTO
import org.json.JSONObject

class RestApi {
    val URI_BASE:String = "http://10.42.0.1:3001"

    companion object{
        private var INSTANCE:RestApi? = null

        fun getInstance(): RestApi {
            if(INSTANCE == null){
                INSTANCE = RestApi()
            }

            return INSTANCE!!
        }
    }


    fun login(username:String, password:String, callback: CallbackInterface<UserDTO?>) {
        val resource:String = "/api/v1/user/login"

        val headers:Map<String,String> = mapOf("username" to username, "password" to password)


        khttp.async.get(URI_BASE + resource, headers) {
            if(this.statusCode == 401){
                callback.doCallback(null)
            }
            else {
                val userJson:JSONObject = this.jsonObject

                var user = UserDTO()
                user.id = userJson.getJSONObject("user").getInt("id")
                user.token = userJson.getString("token")
                user.uuid = userJson.getJSONObject("user").getString("uuid")

                callback.doCallback(user)
            }
        }
    }

    fun getAllPlacesSync(token:String): ArrayList<PlaceDTO>? {
        val resource:String = "/api/v1/place/"

        val headers:Map<String,String> = mapOf("Authorization" to "Bearer " + token)
        val response = khttp.get(URI_BASE + resource, headers)

        if(response.statusCode != 200){
            return null
        }

        var placesDto:ArrayList<PlaceDTO> = ArrayList()

        for(i in 0 until response.jsonArray.length()){
            val placeJSON = response.jsonArray.getJSONObject(i)

            var placeDTO:PlaceDTO = PlaceDTO()
            placeDTO.id = placeJSON.getInt("id")
            placeDTO.major = placeJSON.getInt("major")
            placeDTO.minor = placeJSON.getInt("minor")
            placeDTO.name = placeJSON.getString("name")

            placesDto.add(placeDTO)
        }

        return placesDto
    }

    fun getPlacesSync(idUser:Int, token:String): ArrayList<PlaceDTO>? {
        val resource:String = "/api/v1/user/" + idUser + "/places"

        val headers:Map<String,String> = mapOf("Authorization" to "Bearer " + token)
        val response = khttp.get(URI_BASE + resource, headers)

        if(response.statusCode != 200){
            return null
        }

        var placesDto:ArrayList<PlaceDTO> = ArrayList()

        for(i in 0 until response.jsonArray.length()){
            val placeJSON = response.jsonArray.getJSONObject(i)

            var placeDTO:PlaceDTO = PlaceDTO()
            placeDTO.id = placeJSON.getInt("id")
            placeDTO.major = placeJSON.getInt("major")
            placeDTO.minor = placeJSON.getInt("minor")
            placeDTO.name = placeJSON.getString("name")

            placesDto.add(placeDTO)
        }

        return placesDto
    }

    fun getEquipmentsSync(token:String): ArrayList<EquipmentDTO>? {
        val resource:String = "/api/v1/equipment"

        val headers:Map<String,String> = mapOf("Authorization" to "Bearer " + token)
        val response = khttp.get(URI_BASE + resource, headers)

        if(response.statusCode != 200){
            return null
        }

        var equipmentsDTO:ArrayList<EquipmentDTO> = ArrayList()

        for(i in 0 until response.jsonArray.length()){
            val equipmentJSON = response.jsonArray.getJSONObject(i)

            var equipmentDTO:EquipmentDTO = EquipmentDTO()
            equipmentDTO.id = equipmentJSON.getInt("id")
            equipmentDTO.major = equipmentJSON.getInt("major")
            equipmentDTO.minor = equipmentJSON.getInt("minor")
            equipmentDTO.name = equipmentJSON.getString("name")

            equipmentsDTO.add(equipmentDTO)
        }

        return equipmentsDTO
    }

    fun getPlaceEquipmentsSync(idPlace:Int, token:String): ArrayList<EquipmentDTO>? {
        val resource:String = "/api/v1/place/" + idPlace + "/equipments";

        val headers:Map<String,String> = mapOf("Authorization" to "Bearer " + token)
        val response = khttp.get(URI_BASE + resource, headers)

        if(response.statusCode != 200){
            return null
        }

        var equipmentsDTO:ArrayList<EquipmentDTO> = ArrayList()

        for(i in 0 until response.jsonArray.length()){
            val equipmentJSON = response.jsonArray.getJSONObject(i)

            var equipmentDTO:EquipmentDTO = EquipmentDTO()
            equipmentDTO.id = equipmentJSON.getInt("id")
            equipmentDTO.major = equipmentJSON.getInt("major")
            equipmentDTO.minor = equipmentJSON.getInt("minor")
            equipmentDTO.name = equipmentJSON.getString("name")

            equipmentsDTO.add(equipmentDTO)
        }

        return equipmentsDTO
    }

    fun getPlace(idPlace:Int, token:String, callback: CallbackInterface<PlaceDTO?>) {
        val resource:String = "/api/v1/place/" + idPlace;

        val headers:Map<String,String> = mapOf("Authorization" to "Bearer " + token)

        khttp.async.get(URI_BASE + resource, headers, onError = {
            callback.doCallback(null)
        }) {
            if(this.statusCode == 401){
                callback.doCallback(null)
            }
            else {
                val placeJSON:JSONObject = this.jsonObject

                var placeDTO:PlaceDTO = PlaceDTO()
                placeDTO.id = placeJSON.getInt("id")
                placeDTO.major = placeJSON.getInt("major")
                placeDTO.minor = placeJSON.getInt("minor")
                placeDTO.name = placeJSON.getString("name")
                placeDTO.blueprint = placeJSON.getString("blueprint")

                callback.doCallback(placeDTO)
            }
        }
    }
}