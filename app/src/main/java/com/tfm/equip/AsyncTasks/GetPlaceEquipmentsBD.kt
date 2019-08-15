package com.tfm.equip.AsyncTasks

import android.content.Context
import android.os.AsyncTask
import com.tfm.equip.Database.AppDatabase
import com.tfm.equip.Database.Entities.EquipmentEntity
import com.tfm.equip.Database.Entities.PlaceEntity
import com.tfm.equip.Utils.CallbackInterface

class GetPlaceEquipmentsBD(context: Context, callback: CallbackInterface<ArrayList<EquipmentEntity>>): AsyncTask<Int, Void, ArrayList<EquipmentEntity>>() {
    private var callback:CallbackInterface<ArrayList<EquipmentEntity>>
    private var db: AppDatabase

    init {
        this.callback = callback
        this.db = AppDatabase.getInstance(context)
    }

    override fun doInBackground(vararg params: Int?): ArrayList<EquipmentEntity> {
        val idPlace:Int = params[0]!!

        return ArrayList(db.placeHasEquipmentDao().getEquipmentsForPlace(idPlace))
    }

    override fun onPostExecute(result: ArrayList<EquipmentEntity>) {
        super.onPostExecute(result)
        callback.doCallback(result)
    }
}