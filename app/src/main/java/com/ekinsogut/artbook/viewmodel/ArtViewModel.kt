package com.ekinsogut.artbook.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekinsogut.artbook.model.ImageResponse
import com.ekinsogut.artbook.repo.ArtRepositoryInterface
import com.ekinsogut.artbook.roomdb.Art
import com.ekinsogut.artbook.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel //ViewModel olduğunun belirtilmesi -Dependency Injection
class ArtViewModel @Inject constructor(

    //ArtRepositoryInterface'in tanımlanması
    //Interface içerisinde ki fonksiyonlar ArtRepository içerisinde yazıldığı ve inject edildiği için interface üzerinden erişilebilir
    private val repository : ArtRepositoryInterface

    ) : ViewModel() {

    //Art Fragment
    val artList = repository.getArt() //fragment_arts içerisinde kayıt edilen artları liste halinde döner

    //Image API Fragment
    private val images = MutableLiveData<Resource<ImageResponse>>() //Apiden gelen response'a ulaşabileceğimiz değiştirilebilir LiveData
    val imageList : LiveData<Resource<ImageResponse>> //Gelen liste'ye sadece getter'ı ile ulaşabileceğimiz LiveData
        get() = images

    private val selectedImage = MutableLiveData<String>() //Seçilen görsel'i almak için değiştirilebilir LiveData
    val selectedImageUrl : LiveData<String> //Alınan string'e sadece getter'ı ile ulaşabileceğimiz LiveData
        get() = selectedImage

    //Art Details Fragment
    private var insertArtMsg = MutableLiveData<Resource<Art>>() //Insert mesajı için değiştirilebilir LiveData
    val insertArtMessage : LiveData<Resource<Art>> //Alınan mesaj'ı sadece getter'ı ile ulaşabileceğimiz LiveData
        get() = insertArtMsg


    fun resetInsertArtMsg(){ //Alınan mesajı sıfırlayan fonksiyon
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImage(url: String) {
        selectedImage.postValue(url) //Seçilen görsel'in url'ini alma
    }

    fun insertArt(art: Art) = viewModelScope.launch {
        repository.insertArt(art) //Art ekleme - suspend olduğu için viewModelScope ile coroutine içerisinde yazılır
    }

    fun deleteArt(art: Art) = viewModelScope.launch {
        repository.deleteArt(art) //Art silme - suspend olduğu için viewModelScope ile coroutine içerisinde yazılır
    }

    fun makeArt(name: String,artistName: String,year: String) { //İsim, tablo ismi ve yıl alma
        if (name.isEmpty() || artistName.isEmpty() || year.isEmpty()) { //Eğer girilen değerlerden herhangi biri boş ise oluşturulan resource ile hata mesajı döndürür
            insertArtMsg.postValue(Resource.error("Enter name,artist,year",null))
            return
        }

        val yearInt = try {
            year.toInt() //Gelen veriyi Integer'a çevirme
        } catch (e: Exception) {
            insertArtMsg.postValue(Resource.error("Year should be number",null)) //Eğer hata olursa oluşturulan resource ile hata mesajı döndürür
            return
        }

        val art = Art(name,artistName,yearInt,selectedImage.value ?: "") //Model oluşturulur. Eğer seçilen resim yoksa boş bi şekilde yine döndürür
        insertArt(art) //Database'e ekleme
        setSelectedImage("") //setSelectedImage'i boş hale getirme
        insertArtMsg.postValue(Resource.success(art)) //Oluşturulan resource dosyası ile success fonksiyonu içerisinde data'nın döndürülmesi
    }

    fun searchForImage(searchString: String) { //Search için String alma
        if (searchString.isEmpty()) { //Eğer boşsa bi işlem yapmaz
            return
        }
        images.value = Resource.loading(null) //Oluşturulan resource ile arama yaparken gelen response null'a eşitlenir
        viewModelScope.launch {
            val response = repository.searchImage(searchString) //Art arama -suspend olduğu için viewModelScope ile coroutine içerisinde yazılır
            images.value = response //Dönen response'u oluşturulan mutableLiveData'ya eşitleme
        }
    }
}