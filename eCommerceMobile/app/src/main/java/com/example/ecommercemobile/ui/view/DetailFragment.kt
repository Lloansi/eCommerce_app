package com.example.ecommercemobile.ui.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.data.model.Specs
import com.example.ecommercemobile.databinding.FragmentDetailBinding
import com.example.ecommercemobile.ui.view.adapters.ProductsHorizontaldapter
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerProduct
import com.example.ecommercemobile.ui.viewmodel.CartViewModel
import com.example.ecommercemobile.ui.viewmodel.ProductsViewModel
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import com.example.ecommercemobile.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment(), OnClickListenerProduct {

    lateinit var binding: FragmentDetailBinding
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userViewModel: UserViewModel
    private var recommendedList = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        productsViewModel = ViewModelProvider(requireActivity())[ProductsViewModel::class.java]
        cartViewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBackArrow.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val productID = arguments?.getString("productID")!!
        productsViewModel.getProductByID(productID)

        productsViewModel.product.observe(viewLifecycleOwner){ detailedProduct ->
            setProductInfo(detailedProduct!!)
            recommendedList.clear()
            when (detailedProduct.category){
                "Game" -> {
                    recommendedList = productsViewModel.allProducts.value!!.filter { product ->
                        product.name.contains(detailedProduct.name, true)
                    }.toMutableList()
                    recommendedList.remove(detailedProduct)
                }
                else -> {
                    recommendedList = productsViewModel.allProducts.value!!.filter { product ->
                        product.category == detailedProduct.category
                    }.toMutableList()
                    recommendedList.remove(detailedProduct)
                }
            }
            if (recommendedList.isNotEmpty()){
                binding.shimmerViewContainerRecommended.visibility = View.GONE
                setupRecyclerRecommended(recommendedList)
                binding.shimmerViewContainerRecommended.stopShimmer()
            } else {
                binding.shimmerViewContainerRecommended.stopShimmer()
                binding.shimmerViewContainerRecommended.visibility = View.GONE
            }
        }

        cartViewModel.getCart(userViewModel.user.value!!.userID)

        binding.btnAddCart.setOnClickListener{
            // Get the product list from the cart and add the new product
            val productIDListFromCart = cartViewModel.vmCart.value!!.productList.toMutableList()
            productIDListFromCart.add(productID)
            // Update the cart passing the list of ids of the products
            cartViewModel.updateCart(userViewModel.user.value!!.userID, productIDListFromCart)
            Toast.makeText(requireContext(), "Product added to cart", Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setProductInfo(product: Product){
        binding.tvNameValue.text = product.name
        binding.tvCatchphrase.text = "Disfruta del mejor servidor de ${product.name.split(" ")[0]}"
        Glide.with(this)
            .load("${Constants.PRODUCT_API_URL}products/images/${product.image}")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .circleCrop()
            .into(binding.ivDetail)

        if (product.specs is Specs){
            binding.cpuValueTV.text = " " + ((product.specs!!.cpu) ?: "0")
            binding.ramValueTV.text = " " + (product.specs!!.ram ?: "0")
            binding.ddosProtectionTV.text =  if (product.specs!!.ddosProtect == true) " Yes" else " No" ?: "0"
            binding.ssdStorageTV.text = " " + (product.specs!!.storage ?: " ")
        }
    }

    private fun setupRecyclerRecommended(recommendedProductsList: List<Product>) {
        val myAdapter = ProductsHorizontaldapter(recommendedProductsList, this)
        binding.recyclerviewRecommended.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL,false)
            adapter = myAdapter
        }
        binding.shimmerViewContainerRecommended.visibility = View.GONE
        binding.shimmerViewContainerRecommended.stopShimmer()
    }

    override fun onClickProduct(product: Product) {
        val toDetail = DetailFragmentDirections.actionDetailFragmentSelf(product.id)
        findNavController().navigate(toDetail)
    }
}