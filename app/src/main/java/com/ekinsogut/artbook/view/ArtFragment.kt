package com.ekinsogut.artbook.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ekinsogut.artbook.R
import com.ekinsogut.artbook.adapter.ArtRecyclerAdapter
import com.ekinsogut.artbook.databinding.FragmentArtsBinding
import com.ekinsogut.artbook.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtFragment @Inject constructor(
    val artRecyclerAdapter: ArtRecyclerAdapter //Oluşturulan ArtRecyclerAdapter'ı tanımlama
): Fragment(R.layout.fragment_arts) { //fragment_arts'ın bağlanması

    private var fragmentBinding : FragmentArtsBinding? = null //Viewbinding oluşturulması
    lateinit var viewModel: ArtViewModel //ViewModel'ı alma

    //Sola kaydırarak silme işlemin de swipe kullanma - Hep sol hemde sağa kaydırarak silme
    private val swipeCallBack = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val layoutPosition = viewHolder.layoutPosition //Hangi art seçildiyse position'ı belirlenir
            val selectedArt = artRecyclerAdapter.arts[layoutPosition] //Seçilen art'ı alma
            viewModel.deleteArt(selectedArt) //Silme
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java) //ViewModel'ı fragment'a bağlama ve içerisinde ki fonksiyonlara erişme

        val binding = FragmentArtsBinding.bind(view) //Oluşturulan bindingi view'a bağlamak
        fragmentBinding = binding //Binding'i eşitleme

        subscribeToObservers() //ViewModel içerisinde eriştiğimiz subscribeToObservers

        binding.recyclerViewArt.adapter = artRecyclerAdapter //recyclerView'a ismi ile ulaşıp inject ettiğimiz artRecyclerAdapter ile bağlanır
        binding.recyclerViewArt.layoutManager = LinearLayoutManager(requireContext()) //Linear olarak hizalama ve layoutManager tanımlaması
        ItemTouchHelper(swipeCallBack).attachToRecyclerView(binding.recyclerViewArt) //Swipe ile silme için ItemTouchHelper oluşturma ve recyclerViewArt tanımlaması

        //Binding ile fab'a erişme, onClick ve navigation ile Detay sayfasına gitme
        binding.fab.setOnClickListener {
            findNavController().navigate(ArtFragmentDirections.actionArtFragmentToArtDetailsFragment())
        }
    }

    //viewModel içerisinde ki Art List'e erişme
    private fun subscribeToObservers() {
        viewModel.artList.observe(viewLifecycleOwner, Observer {
            artRecyclerAdapter.arts = it
        })
    }

    //Görünüm destroy olduğunda binding'i null'a eşitleme
    override fun onDestroyView() {
        fragmentBinding = null
        super.onDestroyView()
    }

}