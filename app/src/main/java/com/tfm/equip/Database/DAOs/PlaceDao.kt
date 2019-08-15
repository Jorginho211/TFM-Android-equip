package com.tfm.equip.Database.DAOs

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.tfm.equip.Database.Entities.PlaceEntity

@Dao
interface PlaceDao {
    @Query("SELECT * FROM Places WHERE HasPermission = 1")
    fun getAllPlaces(): List<PlaceEntity>

    @Query("SELECT * FROM Places WHERE id = :id")
    fun getPlaceById(id: Int): PlaceEntity

    @Query("SELECT * FROM Places WHERE Major = :major AND Minor = :minor")
    fun getPlaceByMajorMinor(major:Int, minor:Int): PlaceEntity

    @Insert
    fun insert(place: PlaceEntity)

    @Query("DELETE FROM Places")
    fun deleteAll()
}