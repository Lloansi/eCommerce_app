package com.example.ecommercemobile.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercemobile.data.model.Order
import com.example.ecommercemobile.data.model.OrderState
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.FragmentCartBinding
import com.example.ecommercemobile.ui.view.adapters.CartAdapter
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerCart
import com.example.ecommercemobile.ui.viewmodel.CartViewModel
import com.example.ecommercemobile.ui.viewmodel.OrdersViewModel
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import com.example.ecommercemobile.utils.ExtensionFunctions.round
import com.example.ecommercemobile.utils.TempProvider
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class CartFragment : Fragment(), OnClickListenerCart {
    private lateinit var binding: FragmentCartBinding
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var orderViewModel: OrdersViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        orderViewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]
        cartViewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]
        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userID = userViewModel.user.value?.userID

        cartViewModel.getCart(userID!!)
        /*
        cartViewModel.vmCart.observe(viewLifecycleOwner){ cart ->
            binding.toolbar.title = "My Cart(${cart!!.productList.size})"
            binding.totalTV.text = "Total: ${cart!!.productList.sumOf { it.price }.round(2)}€"
            setUpRecyclerView(cart!!.productList)
        }
         */

        // Se almacenan los ids de los productos en el carrito del viewModel
        // Aqui se itera sobre los ids y se hace una llamada a la api para obtener los productos
        // que se almacenan en la lista de productos
        binding.toolbar.title = "My Cart(${TempProvider.productList.size})"
        binding.totalTV.text = "Total: ${TempProvider.productList.sumOf { it.price }.round(2)}€"
        setUpRecyclerView(TempProvider.productList)

        binding.checkoutBT.setOnClickListener{
            // Se crea una orden con los productos del carrito
            cartViewModel.vmCart.value?.let { cart ->
                // Get the current date and convert-it to string
                val currentDate = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss")
                val currentDateString = currentDate.format(formatter)
                // Create the order
                orderViewModel.addOrder(Order(0, userID, cart.productList,
                    cart.productList.sumOf { it.price }.round(2),
                    currentDateString, OrderState.ONGOING))
                // Delete the cart
                cartViewModel.deleteCart(userID)
            }
        }
    }


    private fun setUpRecyclerView(productList: List<Product>) {
        cartAdapter = CartAdapter(productList, this)
        linearLayoutManager = LinearLayoutManager(context)
        binding.cartRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = cartAdapter
        }
    }

    override fun onClick(product: Product) {
        // Que vaya al detalle?
        println(product.name)
    }

    override fun onRemove(product: Product) {
        // Remove 1 product from the product list (if there is only 1, remove the product)
        TempProvider.productList.removeAt(TempProvider.productList.indexOf(product))
        cartAdapter.updateProducts()
        setUpRecyclerView(TempProvider.productList)
        binding.toolbar.title  = "My Cart(${TempProvider.productList.size})"
    }

    override fun onAdd(product: Product) {
        // Add 1 product to the product list
        TempProvider.productList.add(product)
        cartAdapter.updateProducts()
        setUpRecyclerView(TempProvider.productList)
        binding.toolbar.title  = "My Cart(${TempProvider.productList.size})"
    }

    override fun onDelete(product: Product) {
        // Remove all products from the product list
        TempProvider.productList.remove(product)
        cartAdapter.updateProducts()
        setUpRecyclerView(TempProvider.productList)
        binding.toolbar.title  = "My Cart(${TempProvider.productList.size})"

    }

}