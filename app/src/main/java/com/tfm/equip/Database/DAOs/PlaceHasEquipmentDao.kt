package com.tfm.equip.Database.DAOs

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.tfm.equip.Database.Entities.EquipmentEntity
import com.tfm.equip.Database.Entities.PlaceEntity
import com.tfm.equip.Database.Entities.PlaceHasEquipmentEntity

@Dao
interface PlaceHasEquipmentDao {
    @Insert
    fun insert(placeHasEquipment: PlaceHasEquipmentEntity)

    @Query("""
           SELECT * FROM Places
           INNER JOIN Place_has_Equipment
           ON Places.id=Place_has_Equipment.idPlace
           WHERE Place_has_Equipment.idEquipment = :idEquipment
           """)
    fun getPlacesForEquipment(idEquipment: Int): List<PlaceEntity>

    @Query("""
           SELECT * FROM Equipments
           INNER JOIN Place_has_Equipment
           ON Equipments.id=Place_has_Equipment.idEquipment
           WHERE Place_has_Equipment.idPlace = :idPlace
           """)
    fun getEquipmentsForPlace(idPlace: Int): List<EquipmentEntity>
}