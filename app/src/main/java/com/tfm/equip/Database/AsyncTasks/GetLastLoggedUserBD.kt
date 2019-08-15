package com.tfm.equip.Database.AsyncTasks

import android.content.Context
import android.os.AsyncTask
import com.tfm.equip.Database.AppDatabase
import com.tfm.equip.Database.Entities.UserEntity
import com.tfm.equip.Utils.CallbackInterface

class GetLastLoggedUserBD(context:Context, callback: CallbackInterface<UserEntity?>): AsyncTask<Void, Void, UserEntity>() {
    private var callback:CallbackInterface<UserEntity?>
    private var db:AppDatabase

    init {
        this.callback = callback
        this.db = AppDatabase.getInstance(context)
    }

    override fun doInBackground(vararg params: Void?): UserEntity? {
        return this.db.userDao().getLoggedUser()
    }

    override fun onPostExecute(result: UserEntity?) {
        super.onPostExecute(result)
        callback.doCallback(result)
    }
}