package com.example.ecommercemobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.FragmentSearchBinding
import com.example.ecommercemobile.ui.view.adapters.ProductsHoritzontalAdapter
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerProduct
import com.example.ecommercemobile.ui.viewmodel.ProductsViewModel

class SearchFragment : Fragment(), OnClickListenerProduct {
    lateinit var binding: FragmentSearchBinding
    lateinit var productsViewModel: ProductsViewModel
    var filteredList = listOf<Product>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        productsViewModel = ViewModelProvider(requireActivity())[ProductsViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.shimmerViewContainer.stopShimmer()
        binding.shimmerViewContainer.visibility = View.GONE

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null){

                    if (productsViewModel.allProducts.value != null && productsViewModel.allProducts.value!!.isNotEmpty() ){
                        binding.shimmerViewContainer.startShimmer()
                        binding.shimmerViewContainer.visibility = View.VISIBLE

                        filteredList = productsViewModel.allProducts.value?.filter { product ->
                             product.name.contains(query, true)
                         }!!
                        println(filteredList)

                        if (filteredList.isNotEmpty()){
                            binding.noProductsFoundTV.visibility = View.GONE
                            binding.shimmerViewContainer.visibility = View.GONE
                            setupRecyclerView(filteredList)
                            binding.shimmerViewContainer.stopShimmer()

                        } else {
                            binding.shimmerViewContainer.stopShimmer()
                            binding.shimmerViewContainer.visibility = View.GONE
                            binding.noProductsFoundTV.visibility = View.VISIBLE
                        }
                    }

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")) {
                    binding.noProductsFoundTV.visibility = View.GONE
                    filteredList = listOf()
                    setupRecyclerView(filteredList)
                } else {
                    this.onQueryTextSubmit(newText)
                }
                return true
            }
        })
    }

    private fun setupRecyclerView(filteredList: List<Product>) {
        val myAdapter = ProductsHoritzontalAdapter(filteredList, this)
        binding.recyclerviewPopular.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL,false)
            adapter = myAdapter
        }
        binding.shimmerViewContainer.visibility = View.GONE
    }

    override fun onClickProduct(product: Product) {
        val toDetail = SearchFragmentDirections.actionSearchFragmentToDetailFragment(product.id)
        findNavController().navigate(toDetail)
    }
}