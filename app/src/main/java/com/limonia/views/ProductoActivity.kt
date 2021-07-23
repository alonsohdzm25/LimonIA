package com.limonia.views

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.limonia.adapters.ProductsAdapter
import com.limonia.apiplagas.PlagaApiService
import com.limonia.apiplagas.models.ProductResponse
import com.limonia.apiplagas.models.ProductsResponse
import com.limonia.core.CommonFunction.bandWidth
import com.limonia.core.RetrofitHelper
import com.limonia.databinding.ActivityProductoBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductoActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityProductoBinding

    private lateinit var adapter : ProductsAdapter
    private val productsResponse = ArrayList<ProductResponse>()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.svProducto.setOnQueryTextListener(this)

        if(bandWidth(applicationContext)) {
            initRecycleView()
            searchProducts()
        }

    }

    private fun searchProducts() {
        binding.progressBar.visibility = View.VISIBLE
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val call = RetrofitHelper.getRetrofit().create(PlagaApiService::class.java).getProducts()
                val product = call.body() as ProductsResponse
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    if(call.isSuccessful) {
                        val products = product.product
                        productsResponse.clear()
                        productsResponse.addAll(products)
                        adapter.notifyDataSetChanged()
                    } else {
                        showError()
                    }
                }
            }
        } catch (e: Exception){
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this,"Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    private fun searchProductsByName(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val call = RetrofitHelper.getRetrofit().create(PlagaApiService::class.java).getProductByName("api/prohibitedproduct/$query")
            val product = call.body() as ProductsResponse
            runOnUiThread {
                binding.progressBar.visibility = View.GONE
                if(call.isSuccessful) {
                    val products = product.product
                    if (products.toString() != "[null]" ) {
                        productsResponse.clear()
                        productsResponse.addAll(products)
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(
                            this@ProductoActivity,
                            "No hay resultados para: $query",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    showError()
                }
                hideKeyboard()
            }
        }
    }

    private fun initRecycleView() {
        adapter = ProductsAdapter(productsResponse)
        binding.rvProduct.layoutManager = LinearLayoutManager(this)
        binding.rvProduct.adapter = adapter
    }

    private fun showError() {
        Toast.makeText(this,"Ha ocurrido un error",Toast.LENGTH_SHORT).show()
    }
    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken,0)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onQueryTextSubmit(query: String): Boolean {
        if(bandWidth(applicationContext)) {
            searchProductsByName(query.trim())
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText == ""){
            if(bandWidth(applicationContext)) {
                searchProducts()
            }
        }
        return true
    }
}