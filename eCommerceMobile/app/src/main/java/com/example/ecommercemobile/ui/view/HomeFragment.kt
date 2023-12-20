package com.example.ecommercemobile.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.FragmentHomeBinding
import com.example.ecommercemobile.ui.view.adapters.ProductsHoritzontalAdapter
import com.example.ecommercemobile.ui.view.adapters.ProductsVerticalAdapter
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerProduct
import com.example.ecommercemobile.ui.viewmodel.ProductsViewModel
import com.google.android.material.search.SearchView
import com.google.android.material.search.SearchView.TransitionState
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment(), OnClickListenerProduct {

    lateinit var binding: FragmentHomeBinding
    lateinit var prefs: SharedPreferences
    lateinit var productsViewModel: ProductsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        productsViewModel = ViewModelProvider(requireActivity())[ProductsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)

        productsViewModel.allProducts.observe(viewLifecycleOwner) { productList ->
            binding.shimmerViewContainer.visibility = View.VISIBLE
            binding.shimmerViewContainerRecommended.visibility = View.VISIBLE
            if (productList != null) {
                val popularProducts  = productList!!.sortedBy { product ->
                    product.timesBought
                }.subList(0, 5)
                setupRecyclerRecommended(productList)
                setupRecyclerPopular(popularProducts)
            } else {
                Toast.makeText(requireContext(), "Couldn't fetch products", Toast.LENGTH_SHORT).show()
            }

        }
        binding.searchBar.setOnClickListener {
            val toSearch = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(toSearch)
        }




    }



    private fun setupRecyclerPopular(popularItemsList: List<Product>) {
        val myAdapter = ProductsHoritzontalAdapter(popularItemsList, this)
        binding.recyclerviewPopular.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
            adapter = myAdapter
        }
        binding.shimmerViewContainer.visibility = View.GONE
    }
    private fun setupRecyclerRecommended(recommendedItemsList: List<Product>) {
        val myAdapter = ProductsVerticalAdapter(recommendedItemsList, this)
        binding.recyclerviewRecommended.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
            adapter = myAdapter
        }
        binding.shimmerViewContainerRecommended.visibility = View.GONE

    }

    override fun onClickProduct(product: Product) {
        val toDetail = HomeFragmentDirections.actionHomeFragmentToDetailFragment(product.id)
        findNavController().navigate(toDetail)
    }
}