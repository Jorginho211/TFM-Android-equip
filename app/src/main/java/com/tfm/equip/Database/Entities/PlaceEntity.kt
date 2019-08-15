package com.tfm.equip.Database.Entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Places")
data class PlaceEntity(
    @PrimaryKey
    val id: Int,

    @NotNull
    val Name: String,

    @NotNull
    val Major: Int,

    @NotNull
    val Minor: Int,

    @NotNull
    val HasPermission: Boolean
) {
    override fun toString(): String {
        return this.Name
    }
}