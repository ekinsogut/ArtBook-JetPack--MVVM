package com.ekinsogut.artbook.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.ekinsogut.artbook.R
import com.ekinsogut.artbook.databinding.FragmentArtDetailsBinding
import com.ekinsogut.artbook.util.Status
import com.ekinsogut.artbook.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide : RequestManager //Görsellere ulaşmak için glide tanımlaması
): Fragment(R.layout.fragment_art_details){ //fragment_art_details'in bağlanması

    lateinit var  viewModel : ArtViewModel //ViewModel'ı alma

    private var fragmentBinding : FragmentArtDetailsBinding? = null //Viewbinding oluşturulması

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java) ////ViewModel'ı fragment'a bağlama ve içerisinde ki fonksiyonlara erişme

        val binding = FragmentArtDetailsBinding.bind(view) //Oluşturulan bindingi view'a bağlamak
        fragmentBinding = binding //Binding'i eşitleme

        subscribeToObservers()

        //Binding ile artImageView'a erişme, onClick ve navigation ile görsel araması yapacağımız api sayfasına gitme
        binding.artImageView.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToApiFragment())
        }


        //Kullanıcı geri tuşuna bastığında bi önce ki stack'e yani fragment_arts'a gider
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        //Oluşturulan callback'i onBackPressedDispatcher'a ekliyoruz.
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        //Save butonuna basıldığında yapılacak işlem
        binding.saveButton.setOnClickListener {

            //Girilen blgileri alma ve model oluşturma
            viewModel.makeArt(
                binding.nameText.text.toString(),
                binding.artistText.text.toString(),
                binding.yearText.text.toString())
        }

    }

    //viewModel ile içerisinde ki fonksiyonlara ulaşma
    private fun subscribeToObservers() {

        //viewBinding ile görsel url'ini glide'a tanımlama
        viewModel.selectedImageUrl.observe(viewLifecycleOwner, Observer { url ->
            fragmentBinding?.let {
                glide.load(url).into(it.artImageView)
            }
        })

        //insetArtMessage ile gösterdiğimiz resource type'larını durumlarına göre Toast Mesajı ile gösterme
        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    Toast.makeText(requireActivity(),"Success",Toast.LENGTH_LONG).show()
                    findNavController().popBackStack() //Eğer ekleme işlemi başarılı ise bi önce ki sayfa geri gider
                    viewModel.resetInsertArtMsg() //Mesajı boşaltma
                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(),it.message ?: "Error",Toast.LENGTH_LONG).show()
                }

                Status.LOADING -> {

                }
            }
        })
    }

    //Görünüm destroy olduğunda binding'i null'a eşitleme
    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

}