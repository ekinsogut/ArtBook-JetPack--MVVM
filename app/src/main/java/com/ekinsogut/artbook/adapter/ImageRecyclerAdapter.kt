package com.ekinsogut.artbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ekinsogut.artbook.R
import com.ekinsogut.artbook.roomdb.Art
import javax.inject.Inject

class ImageRecyclerAdapter @Inject constructor(

    val glide : RequestManager //Görsellere ulaşmak için glide tanımlaması

    ) : RecyclerView.Adapter<ImageRecyclerAdapter.ImageViewHolder>() { //RecyclerView tanımlaması

    class ImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) //RecyclerView

    private var onItemClickListener : ((String) -> Unit) ?= null //Arama sonrasında görsel seçildikten sonra url'ini aktarmak için onClickListener

    //diffUtil -> İki liste arasında ki farkı hesaplayan ve recyclerView'un sadece gerekli kısımlarını güncelleyen bir sınıf
    private val diffUtil = object : DiffUtil.ItemCallback<String>() { //Url listesi

        //Gelen memberlar ile eski ve yeni item'ların kontrolü

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    //recyclerListDiffer -> İçerisinde adapter'ı ve diffUtil alır
    //Artık Art List oluştururken recyclerListDiffer kullanılabilir
    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var images: List<String>
        get() = recyclerListDiffer.currentList //Güncel listeyi getter ile getirme
        set(value) = recyclerListDiffer.submitList(value) //Yeni listeyi ekleme

    //RecyclerViewHolder ile gelen fonksiyon
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.image_row,parent,false) //image_row'u bağlama
        return ImageViewHolder(view) //Bağladığımız image_row view'unu döndürme
    }

    fun setOnItemClickListener(listener : (String) -> Unit) { //Görsele tıklama işlemi için setOnItemClickListener oluşturma
        onItemClickListener = listener
    }

    //RecyclerViewHolder ile gelen fonksiyon
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.singleArtImageView) //ImageView'u id üzerinden alma
        val url = images[position] //url'i alma
        holder.itemView.apply {
            glide.load(url).into(imageView) //Glide ile url'in girilmesi
            setOnClickListener {
                onItemClickListener?.let {
                    it(url) //Seçilen görsele tıklanıldığın da detay sayfasına url'i aktarma
                }
            }
        }
    }

    //RecyclerViewHolder ile gelen fonksiyon
    override fun getItemCount(): Int {
        return images.size //Listenin boyutu kadar recycler içerisinde row oluşur
    }
}