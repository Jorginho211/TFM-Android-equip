package com.tfm.equip.Database.Entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "Users")
data class UserEntity(
    @PrimaryKey
    val id: Int,

    @NotNull
    val uuid:String,

    var Token: String,
    var IsLogged: Boolean
)
