package com.tfm.equip.Database.DAOs

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.tfm.equip.Database.Entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM Users WHERE IsLogged = 1")
    fun getLoggedUser(): UserEntity

    @Query("SELECT * FROM Users WHERE id = :id")
    fun getUserById(id: Int): UserEntity

    @Insert
    fun insert(user:UserEntity)

    @Update
    fun update(user: UserEntity)
}