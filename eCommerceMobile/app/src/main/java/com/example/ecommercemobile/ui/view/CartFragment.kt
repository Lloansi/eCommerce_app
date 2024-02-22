package com.example.ecommercemobile.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercemobile.R
import com.example.ecommercemobile.data.model.OrderClient
import com.example.ecommercemobile.data.model.OrderState
import com.example.ecommercemobile.data.model.Product
import com.example.ecommercemobile.databinding.FragmentCartBinding
import com.example.ecommercemobile.ui.view.adapters.CartAdapter
import com.example.ecommercemobile.ui.view.adapters.interfaces.OnClickListenerCart
import com.example.ecommercemobile.ui.viewmodel.CartViewModel
import com.example.ecommercemobile.ui.viewmodel.OrdersViewModel
import com.example.ecommercemobile.ui.viewmodel.ProductsViewModel
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import com.example.ecommercemobile.utils.ExtensionFunctions.round
import com.paypal.checkout.approve.OnApprove
import com.paypal.checkout.cancel.OnCancel
import com.paypal.checkout.createorder.CreateOrder
import com.paypal.checkout.createorder.CurrencyCode
import com.paypal.checkout.createorder.OrderIntent
import com.paypal.checkout.createorder.UserAction
import com.paypal.checkout.error.OnError
import com.paypal.checkout.order.Amount
import com.paypal.checkout.order.AppContext
import com.paypal.checkout.order.OrderRequest
import com.paypal.checkout.order.PurchaseUnit
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class CartFragment : Fragment(), OnClickListenerCart {

    lateinit var binding: FragmentCartBinding
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var cartAdapter: CartAdapter
    private lateinit var cartViewModel: CartViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var orderViewModel: OrdersViewModel
    private lateinit var productsViewModel: ProductsViewModel
    private var productListFromCart = mutableListOf<Product>()
    private var mapProduct = mutableMapOf<Product?,Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("CART ON CREATE VIEW")
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        orderViewModel = ViewModelProvider(requireActivity())[OrdersViewModel::class.java]
        cartViewModel = ViewModelProvider(requireActivity())[CartViewModel::class.java]
        productsViewModel = ViewModelProvider(requireActivity())[ProductsViewModel::class.java]

        binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        val userID = userViewModel.user.value?.userID
        binding.paymentButtonContainer.visibility = View.GONE

        cartViewModel.getCart(userID!!)

        cartViewModel.vmCart.observe(viewLifecycleOwner) { cart ->
            if (cart != null){
                productsViewModel.getProductsByID(cart.productList)
                binding.toolbar.title = "My Cart(${cart.productList.size})"
            }
        }

        productsViewModel.productsFromCart.observe(viewLifecycleOwner) { productList ->
            binding.shimmerViewContainer.startShimmer()
            binding.shimmerViewContainer.visibility = View.VISIBLE
            if (!productList.isNullOrEmpty()){
                println(productList)
                productListFromCart = productList.toMutableList()
                mapProduct = setUpCartMap(productListFromCart)

                setUpRecyclerView(mapProduct)
                binding.toolbar.title = "My Cart(${productListFromCart.size})"
                binding.checkoutBT.visibility = View.VISIBLE
                binding.totalTV.visibility = View.VISIBLE
                binding.totalHeaderTV.visibility = View.VISIBLE
                binding.totalTV.text = "${productListFromCart.sumOf { it.price }.round(2)}"
                binding.emptyCartTV.visibility = View.GONE
                binding.paymentButtonContainer.visibility = View.VISIBLE
            } else {
                binding.paymentButtonContainer.visibility = View.GONE
                binding.checkoutBT.visibility = View.GONE
                binding.totalTV.visibility = View.GONE
                binding.totalHeaderTV.visibility = View.GONE
                binding.emptyCartTV.visibility = View.VISIBLE
            }
            binding.shimmerViewContainer.stopShimmer()
            binding.shimmerViewContainer.visibility = View.GONE
            binding.swipelayout.isEnabled = true
        }

        binding.swipelayout.setColorSchemeColors(R.color.white, R.color.orange)
        binding.swipelayout.setOnRefreshListener {
            binding.swipelayout.isRefreshing = false
            binding.shimmerViewContainer.visibility = View.VISIBLE
            binding.cartRecyclerView.visibility = View.INVISIBLE
            binding.emptyCartTV.visibility = View.INVISIBLE
            binding.paymentButtonContainer.visibility = View.INVISIBLE
            cartViewModel.getCart(userID)
            binding.swipelayout.isEnabled = false
        }

        binding.checkoutBT.setOnClickListener{
            // Se crea una orden con los productos del carrito si no está vacío
            cartViewModel.vmCart.value?.let { cart ->
                if (cart.productList.isNotEmpty()){
                    // Get the current date and convert-it to string
                    val currentDate = LocalDateTime.now()
                    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy'T'HH:mm:ss")
                    val currentDateString = currentDate.format(formatter)
                    // Create the order
                    orderViewModel.addOrder(
                        OrderClient(0, userID!!, productListFromCart.map { product -> product.id},
                        productListFromCart.sumOf { it.price }.round(2),
                        currentDateString, OrderState.COMPLETED)
                    )
                    // Delete the cart
                    productsViewModel.updateTimesBought(productListFromCart.map { product -> product.id})
                    cartViewModel.deleteCart(userID)
                    productsViewModel.productsFromCart.postValue(null)
                    setUpRecyclerView(emptyMap())
                    Toast.makeText(requireContext(), "Purchased!", Toast.LENGTH_SHORT).show()
                }
            }
        }
        configurePayPalButton(requireContext())
    }

    private fun setUpRecyclerView(productMap: Map<Product?, Int>) {
        cartAdapter = CartAdapter(productMap, this)
        linearLayoutManager = LinearLayoutManager(context)
        binding.cartRecyclerView.visibility = View.VISIBLE
        binding.cartRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = cartAdapter
        }
    }

    override fun onClick(product: Product) {
        val toDetail = CartFragmentDirections.actionCartFragmentToDetailFragment(product.id)
        findNavController().navigate(toDetail)
    }

    override fun onRemove(product: Product) {
        // Remove 1 product from the product list (if there is only 1, remove the product)
        productListFromCart.removeAt(cartViewModel.vmCart.value!!.productList.indexOf(product.id))
        // Update the cart passing the list of ids of the products
        cartViewModel.updateCart(userViewModel.user.value!!.userID,
            productListFromCart.map { product -> product.id})
        mapProduct = setUpCartMap(productListFromCart)
        cartAdapter.updateProducts()
        setUpRecyclerView(mapProduct)
        binding.toolbar.title  = "My Cart(${productListFromCart.size})"
    }

    override fun onAdd(product: Product) {
        // Add 1 product to the product list
        productListFromCart.add(product)
        // Update the cart passing the list of ids of the products
        cartViewModel.updateCart(userViewModel.user.value!!.userID,
            productListFromCart.map { product -> product.id})
        cartAdapter.updateProducts()
        mapProduct = setUpCartMap(productListFromCart)
        setUpRecyclerView(mapProduct)
        binding.toolbar.title  = "My Cart(${productListFromCart.size})"
    }

    override fun onDelete(product: Product) {
        // Remove all products from the product list
        productListFromCart.remove(product)
        // Update the cart passing the list of ids of the products
        cartViewModel.updateCart(userViewModel.user.value!!.userID,
            productListFromCart.map { product -> product.id})
        cartAdapter.updateProducts()
        mapProduct = setUpCartMap(productListFromCart)
        setUpRecyclerView(mapProduct)
        binding.toolbar.title  = "My Cart(${productListFromCart.size})"

    }

    private fun configurePayPalButton(ctx : Context) {
        binding.paymentButtonContainer.setup(
            createOrder = CreateOrder { createOrderActions ->
                val order = OrderRequest(
                    intent = OrderIntent.CAPTURE,
                    appContext = AppContext(userAction = UserAction.PAY_NOW),
                    purchaseUnitList = listOf(
                        PurchaseUnit(
                            amount = Amount(currencyCode = CurrencyCode.EUR, value = "${productListFromCart.sumOf { it.price }.round(2)}")
                        )
                    )
                )
                createOrderActions.create(order)
            },
            onApprove = OnApprove { approval ->
                approval.orderActions.capture { captureOrderResult ->
                    Log.i("CaptureOrder", "CaptureOrderResult: $captureOrderResult")
                    // Puedes manejar el resultado de la captura aquí
                    Toast.makeText(ctx,"Payment Approved",Toast.LENGTH_SHORT).show()
                    println("Payment Approved")
                }
            },
            onCancel = OnCancel {
                Log.d("OnCancel", "Buyer canceled the PayPal experience.")
                // Puedes manejar la cancelación aquí
                Toast.makeText(ctx,"Payment Canceled",Toast.LENGTH_SHORT).show()
                println("Payment Canceled")
            },
            onError = OnError { errorInfo ->
                Log.d("OnError", "Error: $errorInfo")
                // Puedes manejar errores aquí
                Toast.makeText(ctx,"Payment Error",Toast.LENGTH_SHORT).show()
                print("Payment Error")
            }
        )
    }
    fun setUpCartMap(list: List<Product>):MutableMap<Product?, Int> {
        val mapOfProducts = mutableMapOf<Product?,Int>()
        list.forEach{product->
            mapOfProducts[product] = list.count{it.id==product.id}
        }
        return mapOfProducts
    }
}