package com.tfm.equip.Database.DAOs

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.tfm.equip.Database.Entities.EquipmentEntity

@Dao
interface EquipmentDao {
    @Query("SELECT * FROM Equipments")
    fun getAllEquipments(): List<EquipmentEntity>

    @Query("SELECT * FROM Equipments WHERE id = :id")
    fun getPlaceById(id: Int): EquipmentEntity

    @Query("SELECT * FROM Equipments WHERE Major = :major AND Minor = :minor")
    fun getPlaceByMajorMinor(major:Int, minor:Int): EquipmentEntity

    @Insert
    fun insert(equipment: EquipmentEntity)

    @Query("DELETE FROM Equipments")
    fun deleteAll()
}