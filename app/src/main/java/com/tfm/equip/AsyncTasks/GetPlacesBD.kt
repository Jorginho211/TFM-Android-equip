package com.tfm.equip.AsyncTasks

import android.content.Context
import android.os.AsyncTask
import com.tfm.equip.Database.AppDatabase
import com.tfm.equip.Database.Entities.PlaceEntity
import com.tfm.equip.Database.Entities.UserEntity
import com.tfm.equip.Utils.CallbackInterface

class GetPlacesBD(context: Context, callback: CallbackInterface<ArrayList<PlaceEntity>>): AsyncTask<Void, Void, ArrayList<PlaceEntity>>() {
    private var callback:CallbackInterface<ArrayList<PlaceEntity>>
    private var db: AppDatabase

    init {
        this.callback = callback
        this.db = AppDatabase.getInstance(context)
    }

    override fun doInBackground(vararg params: Void?): ArrayList<PlaceEntity> {
        return ArrayList<PlaceEntity>(db.placeDao().getAllPlaces())
    }

    override fun onPostExecute(result: ArrayList<PlaceEntity>) {
        super.onPostExecute(result)
        callback.doCallback(result)
    }
}