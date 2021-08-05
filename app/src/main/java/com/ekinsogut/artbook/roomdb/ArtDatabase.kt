package com.ekinsogut.artbook.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase

//Room Database'i
@Database(entities = [Art::class] , version = 1, exportSchema = false) //entities -> Oluşturulan art modeli
abstract class ArtDatabase: RoomDatabase() {
    abstract fun artDao(): ArtDao //Oluşturulan Dao objesinin dönen abstract fonksiyon
}