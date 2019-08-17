package com.tfm.equip.Database.Entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Equipments")
class EquipmentEntity(){
    @PrimaryKey
    var id: Int = 0

    @NotNull
    var Name: String = ""

    @NotNull
    var Major: Int = 0

    @NotNull
    var Minor: Int = 0

    @Ignore
    var IsEquip: Boolean = true

    //Ñapa para que Java non se queixe ao non ter un constructor
    constructor(id:Int, name:String, major:Int, minor:Int) : this() {
        this.id = id
        this.Name = name
        this.Major = major
        this.Minor = minor
    }

    override fun toString(): String {
        return this.Name
    }
}