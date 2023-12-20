package com.example.ecommercemobile.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.data.model.Specs
import com.example.ecommercemobile.databinding.FragmentDetailBinding
import com.example.ecommercemobile.ui.viewmodel.CartViewModel
import com.example.ecommercemobile.ui.viewmodel.ProductsViewModel
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    lateinit var binding: FragmentDetailBinding
    lateinit var prefs: SharedPreferences
    private lateinit var productsViewModel: ProductsViewModel
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userViewModel: UserViewModel


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

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)

        val productID = arguments?.getString("productID")!!

        productsViewModel.getProductByID(productID)

        productsViewModel.product.observe(viewLifecycleOwner){ product ->
            setProductInfo(product!!)
        }

        cartViewModel.getCart(userViewModel.user.value!!.userID)

        binding.addToCartBTN.setOnClickListener{
            // Get the product list from the cart and add the new product
            val productIDListFromCart = cartViewModel.vmCart.value!!.productList.toMutableList()
            productIDListFromCart.add(productID)
            // Update the cart passing the list of ids of the products
            cartViewModel.updateCart(userViewModel.user.value!!.userID, productIDListFromCart)
        }
    }

    fun setProductInfo(product: Product){
        binding.toolbar.title = product.name
        binding.nameTV.text = product.name
        binding.priceTV.text = product.price.toString()
        Glide.with(this)
            .load("http://10.0.2.2:8000/api/uploads/images/${product.image}")
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .circleCrop()
            .into(binding.productIV)
        binding.categoryTV.text = product.category

        if (product.specs is Specs){
            binding.cpuValueTV.text = product.specs.cpu
            binding.ramValueTV.text = product.specs.ram
            binding.ddosProtectionTV.text = product.specs.ddosProtect.toString()
            binding.ssdStorageTV.text = product.specs.storage
        }
    }
}