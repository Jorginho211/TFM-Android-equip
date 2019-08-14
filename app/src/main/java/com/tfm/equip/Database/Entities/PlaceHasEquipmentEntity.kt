package com.tfm.equip.Database.Entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey

@Entity(tableName = "Place_has_Equipment",
    primaryKeys = arrayOf("idPlace","idEquipment"),
    foreignKeys = arrayOf(
        ForeignKey(onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            entity = PlaceEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idPlace")),
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
            entity = EquipmentEntity::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("idEquipment"))
    )
)
data class PlaceHasEquipmentEntity (
    val idPlace: Int,
    val idEquipment: Int
)