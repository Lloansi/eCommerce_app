package com.example.ecommercemobile.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercemobile.R
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.FragmentHomeBinding
import com.example.ecommercemobile.ui.view.adapters.ProductsHorizontaldapter
import com.example.ecommercemobile.ui.view.adapters.ProductsVerticalAdapter
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerProduct
import com.example.ecommercemobile.ui.viewmodel.ProductsViewModel
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), OnClickListenerProduct {

    lateinit var binding: FragmentHomeBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("HOME ON CREATE VIEW")
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        productsViewModel = ViewModelProvider(requireActivity())[ProductsViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        println("HOME-Auth: ${userViewModel.authResult.value}")
        productsViewModel.allProducts.observe(viewLifecycleOwner) { productList ->
            binding.shimmerViewContainer.startShimmer()
            binding.shimmerViewContainerRecommended.startShimmer()
            binding.shimmerViewContainer.visibility = View.VISIBLE
            binding.shimmerViewContainerRecommended.visibility = View.VISIBLE
            if (productList != null) {
                val popularProducts  = productList!!.sortedBy { product ->
                    product.timesBought
                }.subList(0, 5)
                setupRecyclerRecommended(productList)
                setupRecyclerPopular(popularProducts)
                binding.recyclerviewPopular.visibility = View.VISIBLE
                binding.recyclerviewRecommended.visibility = View.VISIBLE
            } else {
                Toast.makeText(requireContext(), "Couldn't fetch products", Toast.LENGTH_SHORT).show()
            }
            binding.swipelayout.isEnabled = true
        }

        binding.swipelayout.setColorSchemeColors(R.color.white, R.color.orange)

        binding.swipelayout.setOnRefreshListener {
            binding.swipelayout.isRefreshing = false
                binding.shimmerViewContainer.visibility = View.VISIBLE
                binding.recyclerviewPopular.visibility = View.INVISIBLE
                binding.shimmerViewContainerRecommended.visibility = View.VISIBLE
                binding.recyclerviewRecommended.visibility = View.INVISIBLE
                productsViewModel.getAllProducts()
            binding.swipelayout.isEnabled = false
        }

        binding.searchBar.setOnClickListener {
            val toSearch = HomeFragmentDirections.actionHomeFragmentToSearchFragment("")
            findNavController().navigate(toSearch)
        }
        binding.imageView2.setOnClickListener{
            val toSearchVoiceCategory = HomeFragmentDirections.actionHomeFragmentToSearchFragment("Chat")
            findNavController().navigate(toSearchVoiceCategory)
        }
        binding.imageView3.setOnClickListener{
            val toSearchGameCategory = HomeFragmentDirections.actionHomeFragmentToSearchFragment("Game")
            findNavController().navigate(toSearchGameCategory)
        }
        binding.imageView4.setOnClickListener{
            val toSearchBotCategory = HomeFragmentDirections.actionHomeFragmentToSearchFragment("Bot")
            findNavController().navigate(toSearchBotCategory)
        }
        binding.seealltv1.setOnClickListener {
            val toSeeAll = HomeFragmentDirections.actionHomeFragmentToSearchFragment("All")
            findNavController().navigate(toSeeAll)
        }
        binding.seeall2tv.setOnClickListener {
            val toSeeAll = HomeFragmentDirections.actionHomeFragmentToSearchFragment("All")
            findNavController().navigate(toSeeAll)
        }
    }



    private fun setupRecyclerPopular(popularItemsList: List<Product>) {
        val myAdapter = ProductsVerticalAdapter(popularItemsList, this)
        binding.recyclerviewPopular.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
            adapter = myAdapter
        }
        binding.shimmerViewContainer.visibility = View.GONE
        binding.shimmerViewContainer.stopShimmer()
    }
    private fun setupRecyclerRecommended(recommendedItemsList: List<Product>) {
        val myAdapter = ProductsHorizontaldapter(recommendedItemsList, this)
        binding.recyclerviewRecommended.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
            adapter = myAdapter
        }
        binding.shimmerViewContainerRecommended.visibility = View.GONE
        binding.shimmerViewContainerRecommended.stopShimmer()
    }

    override fun onClickProduct(product: Product) {
        val toDetail = HomeFragmentDirections.actionHomeFragmentToDetailFragment(product.id)
        findNavController().navigate(toDetail)
    }
}