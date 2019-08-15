package com.tfm.equip.Utils

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
}