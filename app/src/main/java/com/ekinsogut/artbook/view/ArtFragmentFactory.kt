package com.ekinsogut.artbook.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.bumptech.glide.RequestManager
import com.ekinsogut.artbook.adapter.ArtRecyclerAdapter
import com.ekinsogut.artbook.adapter.ImageRecyclerAdapter
import javax.inject.Inject

//RecyclerAdapter'ların fragment'lara constructor'lar aracılığı ile bağlanması için gerekli olan Factory

class ArtFragmentFactory @Inject constructor(

    private val artRecyclerAdapter: ArtRecyclerAdapter, //Oluşturulan ArtRecyclerAdapter
    private val glide : RequestManager, //Görsellere ulaşmak için glide tanımlaması
    private val imageRecyclerAdapter: ImageRecyclerAdapter //Oluşturulan ImageRecyclerAdapter

    ) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when(className) {
            ArtFragment::class.java.name -> ArtFragment(artRecyclerAdapter) //Oluşturulan artRecyclerAdapter'ın ile Artfragment bağlantısı
            ArtDetailsFragment::class.java.name -> ArtDetailsFragment(glide) //ArtDetailsFragment içerisinde Inject cont ile istenen glide'ın verilmesi
            ApiFragment::class.java.name -> ApiFragment(imageRecyclerAdapter) //Oluşturulan imageRecyclerAdapter'ın ile Apifragment bağlantısı
            else -> super.instantiate(classLoader, className)
        }
    }
}
