package com.ekinsogut.artbook.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

//Room Dao'su - Oluşturulan room modeli üzerinden fonksiyonların yazılması
@Dao
interface ArtDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //onConflict -> id'ler çakışırsa izleyeceğimiz yol. REPLACE ile üzerine yazar
    suspend fun insertArt(art : Art)

    @Delete
    suspend fun deleteArt(art : Art)

    @Query("SELECT * FROM arts")
    fun observeArts(): LiveData<List<Art>> //LiveData zaten asenkron çalıştığı için suspend kullanılmadı. Liste döndürecek. Art list olacak
}