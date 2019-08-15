package com.tfm.equip.Database.AsyncTasks

import android.content.Context
import android.os.AsyncTask
import com.tfm.equip.Database.AppDatabase
import com.tfm.equip.Database.Entities.UserEntity

class LogoutUserBD(context: Context): AsyncTask<UserEntity, Void, Void>() {
    private var db: AppDatabase

    init {
        db = AppDatabase.getInstance(context)
    }

    override fun doInBackground(vararg params: UserEntity?): Void? {
        db.userDao().logoutAllUsers()

        return null
    }
}