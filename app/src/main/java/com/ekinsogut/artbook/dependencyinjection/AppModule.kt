package com.ekinsogut.artbook.dependencyinjection

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ekinsogut.artbook.R
import com.ekinsogut.artbook.api.RetrofitAPI
import com.ekinsogut.artbook.repo.ArtRepository
import com.ekinsogut.artbook.repo.ArtRepositoryInterface
import com.ekinsogut.artbook.roomdb.ArtDao
import com.ekinsogut.artbook.roomdb.ArtDatabase
import com.ekinsogut.artbook.util.Util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

//Dependency Injection tanımlamaları, Module - InstallIn
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //Room Database'i oluşturulması
    @Singleton
    @Provides
    fun injectRoomDatabase(
        @ApplicationContext context: Context) = Room.databaseBuilder(
        context,ArtDatabase::class.java,"ArtBookDB" //Database ismi
        ).build()

    //Room Database'e erişme
    @Singleton
    @Provides
    fun injectDao(database : ArtDatabase) = database.artDao()

    //Retrofit Api'ye erişme
    @Singleton
    @Provides
    fun injectRetrofitAPI() : RetrofitAPI { //Oluşturulan retrofit api'yi döndürür

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create()) //Gson konfigürasyonu
            .baseUrl(BASE_URL) //Oluşturulan base url'in girilmesi
            .build()
            .create(RetrofitAPI::class.java) //Kullanılacak servis oluşturulan RetrofitAPI
    }


    //Repository
    @Singleton
    @Provides
    fun injectNormalRepo(dao : ArtDao, api: RetrofitAPI) = ArtRepository(dao,api) as ArtRepositoryInterface

    //Glide oluşturma
    @Singleton
    @Provides
    fun injectGlide(@ApplicationContext context: Context) = Glide
        .with(context).setDefaultRequestOptions(
            RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
                .error(R.drawable.ic_launcher_foreground)
        )


}