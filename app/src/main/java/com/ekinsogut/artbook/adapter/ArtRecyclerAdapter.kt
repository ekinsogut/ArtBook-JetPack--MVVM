package com.ekinsogut.artbook.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.ekinsogut.artbook.R
import com.ekinsogut.artbook.roomdb.Art
import javax.inject.Inject

class ArtRecyclerAdapter @Inject constructor(
    val glide : RequestManager //Görsellere ulaşmak için glide tanımlaması
) : RecyclerView.Adapter<ArtRecyclerAdapter.ArtViewHolder>() { //RecyclerView tanımlaması

    class ArtViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) //RecyclerView

    //diffUtil -> İki liste arasında ki farkı hesaplayan ve recyclerView'un sadece gerekli kısımlarını güncelleyen bir sınıf
    private val diffUtil = object : DiffUtil.ItemCallback<Art>() { //Art listesi

        //Gelen memberlar ile eski ve yeni item'ların kontrolü

        override fun areItemsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Art, newItem: Art): Boolean {
            return oldItem == newItem
        }
    }

    //recyclerListDiffer -> İçerisinde adapter'ı ve diffUtil alır
    //Artık Art List oluştururken recyclerListDiffer kullanılabilir
    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)


    var arts: List<Art>
        get() = recyclerListDiffer.currentList //Güncel listeyi getter ile getirme
        set(value) = recyclerListDiffer.submitList(value) //Yeni listeyi ekleme

    //RecyclerViewHolder ile gelen fonksiyon
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.art_row,parent,false) //art_row'u bağlama
        return ArtViewHolder(view) //Bağladığımız art_row view'unu döndürme
    }

    //RecyclerViewHolder ile gelen fonksiyon
    override fun onBindViewHolder(holder: ArtViewHolder, position: Int) {

        //Bilgileri id üzerinden alma

        val imageView = holder.itemView.findViewById<ImageView>(R.id.artRowImageView)
        val nameText = holder.itemView.findViewById<TextView>(R.id.artRowNameText)
        val artistNameText = holder.itemView.findViewById<TextView>(R.id.artRowArtistNameText)
        val yearText = holder.itemView.findViewById<TextView>(R.id.artRowYearText)

        val art = arts[position] //Güncel olan art'ı alma

        //Bilgileri eşitleme

        holder.itemView.apply {
            nameText.text = "Name: ${art.name}"
            artistNameText.text = "Artist Name: ${art.artistName}"
            yearText.text = "Year: ${art.year}"
            glide.load(art.imageUrl).into(imageView) //Glide ile url'in girilmesi
        }
    }

    //RecyclerViewHolder ile gelen fonksiyon
    override fun getItemCount(): Int {
        return arts.size //Listenin boyutu kadar recycler içerisinde row oluşur
    }
}