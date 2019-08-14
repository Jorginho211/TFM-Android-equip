package com.tfm.equip.Database.Entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "MonitorData",
    foreignKeys = arrayOf(
        ForeignKey(
            onDelete = ForeignKey.CASCADE,
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
data class MonitorDataEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val idPlace: Int,
    val idEquipment: Int,
    val Date: Int
)