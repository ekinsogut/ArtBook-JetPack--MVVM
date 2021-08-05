package com.ekinsogut.artbook.view

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.ekinsogut.artbook.R
import com.ekinsogut.artbook.adapter.ImageRecyclerAdapter
import com.ekinsogut.artbook.databinding.FragmentImageApiBinding
import com.ekinsogut.artbook.util.Status
import com.ekinsogut.artbook.viewmodel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ApiFragment @Inject constructor(
    private val imageRecyclerAdapter: ImageRecyclerAdapter //Oluşturulan imageRecyclerAdapter'ı tanımlama
) : Fragment(R.layout.fragment_image_api) { //fragment_image_api'nin bağlanması

    lateinit var viewModel: ArtViewModel //ViewModel'ı alma

    private var fragmentBinding : FragmentImageApiBinding? = null //Viewbinding oluşturulması

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java) //ViewModel'ı fragment'a bağlama ve içerisinde ki fonksiyonlara erişme

        val binding = FragmentImageApiBinding.bind(view) //Oluşturulan bindingi view'a bağlamak
        fragmentBinding = binding //Binding'i eşitleme

        var job: Job? = null

        //Arama kısmında her yazı yazıldığında 1sn delay verir. Boş değil ise String'i alır
        binding.searchText.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch {
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        subscribeToObservers()

        binding.imageRecyclerView.adapter = imageRecyclerAdapter //recyclerView'a ismi ile ulaşıp inject ettiğimiz imageRecyclerAdapter ile bağlanır
        binding.imageRecyclerView.layoutManager = GridLayoutManager(requireContext(),3) //GridLayout olarak hizalama ve layoutManager tanımlaması
        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack() //Detay sayfasına dönme
            viewModel.setSelectedImage(it) //Görsel'e tıklandığın da url'i alma
        }
    }

    //viewModel ile içerisinde ki fonksiyonlara ulaşma
    private fun subscribeToObservers() {

        //Seçilen görsel'in resource type'ına göre yapılacak işlemlerin belirlenmesi
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
            when(it.status) {
                Status.SUCCESS -> {
                    val urls = it.data?.hits?.map { imageResult ->  imageResult.previewURL } //Eğer seçme işlemi başarılı ise nullable gelen data'nın url'i alınır
                    imageRecyclerAdapter.images = urls ?: listOf() //Url'i verme
                    fragmentBinding?.progressBar?.visibility = View.GONE //ProgressBar gözükmez

                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show() //Eğr seçme işlemi başarısız ise hata mesajı gözükür
                    fragmentBinding?.progressBar?.visibility = View.GONE //ProgressBar gözükmez

                }

                Status.LOADING -> {
                    fragmentBinding?.progressBar?.visibility = View.VISIBLE //ProgressBar gözükür

                }
            }

        })
    }


}