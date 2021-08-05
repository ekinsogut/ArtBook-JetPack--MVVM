package com.ekinsogut.artbook.repo

import androidx.lifecycle.LiveData
import com.ekinsogut.artbook.model.ImageResponse
import com.ekinsogut.artbook.roomdb.Art
import com.ekinsogut.artbook.util.Resource

//ArtRepository içerisinde kullanılacak olan fonksiyonların interface'leri

interface ArtRepositoryInterface {

    suspend fun insertArt(art : Art) //insertArt -Room
    suspend fun deleteArt(art : Art) //deleteArt -Room
    fun getArt() : LiveData<List<Art>> //getArt - LiveData döner, Art list

    //searchImage - İçerisinde String alır, oluşturulan ImageResponse'u döner
    //Dönecek response'un type'ın belirlemek için kullandığımız Resource'un içerisine ImageREsponse yazılır
    suspend fun searchImage(imageString : String) : Resource<ImageResponse>

}

