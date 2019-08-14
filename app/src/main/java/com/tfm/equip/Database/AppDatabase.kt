package com.tfm.equip.Database

import android.arch.persistence.room.Database
import android.content.Context
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.tfm.equip.Database.DAOs.*
import com.tfm.equip.Database.Entities.*
import com.tfm.equip.Database.Utils.Converters


@Database(entities = arrayOf(UserEntity::class, PlaceEntity::class, EquipmentEntity::class, PlaceHasEquipmentEntity::class, MonitorDataEntity::class), version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun placeDao(): PlaceDao
    abstract fun equipmentDao(): EquipmentDao
    abstract fun placeHasEquipmentDao(): PlaceHasEquipmentDao
    abstract fun monitorDataDao(): MonitorDataDao

    companion object {
        private var INSTANCE:AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = buildDatabase(context)
            }

            return INSTANCE as AppDatabase
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "equip"
            ).build()
        }
    }
}