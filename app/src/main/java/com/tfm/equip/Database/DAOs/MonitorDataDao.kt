package com.tfm.equip.Database.DAOs

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.tfm.equip.Database.Entities.MonitorDataEntity

@Dao
interface MonitorDataDao {
    @Query("SELECT * FROM MonitorData WHERE Date = (SELECT Max(Date) FROM MonitorData)")
    fun getLastMonitorData(): MonitorDataEntity

    @Query("DELETE FROM MonitorData")
    fun deleteAll()

    @Insert
    fun insert(monitorData: MonitorDataEntity)
}