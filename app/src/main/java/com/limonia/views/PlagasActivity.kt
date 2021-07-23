package com.limonia.views

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.limonia.R
import com.limonia.adapters.PlagasAdapter
import com.limonia.apiplagas.PlagaApiService
import com.limonia.apiplagas.models.PlagaResponse
import com.limonia.apiplagas.models.PlagasResponse
import com.limonia.core.CommonFunction
import com.limonia.core.CommonFunction.bandWidth
import com.limonia.core.RetrofitHelper.getRetrofit
import com.limonia.databinding.ActivityPlagasBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class PlagasActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityPlagasBinding

    private lateinit var adapter : PlagasAdapter
    private val plagasResponse = ArrayList<PlagaResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_LimonIA)
        super.onCreate(savedInstanceState)
        binding = ActivityPlagasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.svPlaga.setOnQueryTextListener(this)

        if(bandWidth(applicationContext)) {
            initRecycleView()
            searchPlagas()
        }
    }


    private fun searchPlagas() {
        binding.progressBar.visibility = View.VISIBLE
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val call = getRetrofit().create(PlagaApiService::class.java).getPlagas()
                val plaga = call.body() as PlagasResponse
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    if(call.isSuccessful) {
                        val plagas = plaga.plagas
                        plagasResponse.clear()
                        plagasResponse.addAll(plagas)
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

    private fun searchPlagasByName(query: String) {
        binding.progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                val call = getRetrofit().create(PlagaApiService::class.java).getPlagaByName("api/plagas/$query")
                val plaga = call.body() as PlagasResponse
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    if(call.isSuccessful) {
                        val plagas = plaga.plagas
                        if (plagas.toString() != "[null]" ) {
                            plagasResponse.clear()
                            plagasResponse.addAll(plagas)
                            adapter.notifyDataSetChanged()
                        } else {
                                Toast.makeText(
                                    this@PlagasActivity,
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
        adapter = PlagasAdapter(plagasResponse)
        binding.rvPlaga.layoutManager = LinearLayoutManager(this)
        binding.rvPlaga.adapter = adapter
    }

    private fun showError() {
        Toast.makeText(this,"Ha ocurrido un error",Toast.LENGTH_SHORT).show()
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.viewRoot.windowToken,0)
    }

    override fun onQueryTextSubmit(query: String) : Boolean {
        if(bandWidth(applicationContext)) {
            searchPlagasByName(query.trim())
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText == ""){
            if(bandWidth(applicationContext)) {
                searchPlagas()
            }
        }
        return true
    }
}