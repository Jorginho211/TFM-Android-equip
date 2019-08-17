package com.tfm.equip.AsyncTasks

import android.content.Context
import android.os.AsyncTask
import com.tfm.equip.DTOs.UserDTO
import com.tfm.equip.Database.AppDatabase
import com.tfm.equip.Database.Entities.UserEntity
import com.tfm.equip.Utils.CallbackInterface

class InsertOrUpdateUserBD(context: Context, callback:CallbackInterface<UserEntity>):  AsyncTask<UserDTO, Void, UserEntity>() {
    private var db: AppDatabase
    private var callback: CallbackInterface<UserEntity>

    init {
        db = AppDatabase.getInstance(context)
        this.callback = callback
    }

    override fun doInBackground(vararg params: UserDTO?): UserEntity {
        val userDTO:UserDTO = params[0]!!
        var userEntity:UserEntity? = db.userDao().getUserById(userDTO.id)

        if(userEntity != null){
            userEntity.Token = userDTO.token
            userEntity.IsLogged = true
            userEntity.FrequencySendData = userDTO.frequencySendData
            db.userDao().update(userEntity)
        }
        else {
            userEntity = UserEntity(userDTO.id, userDTO.uuid, userDTO.token, true, userDTO.frequencySendData)
            db.userDao().logoutAllUsers()
            db.userDao().insert(userEntity)
        }

        return userEntity
    }

    override fun onPostExecute(result: UserEntity) {
        super.onPostExecute(result)
        callback.doCallback(result)
    }
}