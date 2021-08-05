package com.ekinsogut.artbook.repo

import androidx.lifecycle.LiveData
import com.ekinsogut.artbook.api.RetrofitAPI
import com.ekinsogut.artbook.model.ImageResponse
import com.ekinsogut.artbook.roomdb.Art
import com.ekinsogut.artbook.roomdb.ArtDao
import com.ekinsogut.artbook.util.Resource
import javax.inject.Inject

//Room ve retrofit işlemlerinin gerçekleştiği ara sınıf, (test'lerin karmaşıklığını azalmak için)

class ArtRepository @Inject constructor( //@Inject cont -> Hilt

    //ArtDao ve Reftofit API'nin constructor içerisinde tanımlanması, böylelikle Hilt ile Room ve Retrofit için oluşturulan fonksiyonlara erişilebilir
    private val artDao: ArtDao,
    private val retrofitAPI: RetrofitAPI

    ) : ArtRepositoryInterface { //ArtRepositoryInterface içerisinde oluşturulan fonksiyonları ovveride edilmesi

    override suspend fun insertArt(art: Art) { //ArtRepositoryInterface
        artDao.insertArt(art) //ArtDao
    }

    override suspend fun deleteArt(art: Art) { //ArtRepositoryInterface
        artDao.deleteArt(art) //ArtDao
    }

    override fun getArt(): LiveData<List<Art>> { //ArtRepositoryInterface
        return artDao.observeArts() //ArtDao
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> { //ArtRepositoryInterface

        return try {

            val response = retrofitAPI.imageSearch(imageString) //RetrofitAPI

            if (response.isSuccessful) { //Dönen response'un başarılı olma durumu
                response.body()?.let {
                    return@let Resource.success(it) //Eğer body alınabiliyorsa it yani imageResponse'u return eder
                } ?: Resource.error("Error",null) //Oluşturulan resource ile dönen response'ta bir hata varsa error döner
            } else {
                Resource.error("Error",null) //Oluşturulan resource ile dönen response'ta bir hata varsa error döner
            }

        }catch (e: Exception) {
            Resource.error("No Data!",null) //Oluşturulan resource ile dönen response'ta bir hata varsa error döner
        }
    }
}