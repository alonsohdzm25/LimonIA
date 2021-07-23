package com.limonia.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.limonia.apiplagas.PlagaApiService
import com.limonia.apiplagas.models.PlagasResponse
import com.limonia.core.CommonFunction
import com.limonia.core.CommonFunction.bandWidth
import com.limonia.core.RetrofitHelper.getRetrofit
import com.limonia.databinding.ActivityPhotoBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoBinding

    private lateinit var labelPlaga: String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Obtener url de imagen
        val objectIntent: Intent = intent
        labelPlaga = objectIntent.getStringExtra("labelPlaga").toString()

        if(bandWidth(applicationContext)) {
            searchPlagasByName(labelPlaga)
        }

    }

    private fun searchPlagasByName(query: String) {
        binding.progressBar.visibility = View.VISIBLE
        binding.CardView.visibility = View.GONE
        binding.textView5.visibility = View.GONE
        binding.textView7.visibility = View.GONE
        binding.textView9.visibility = View.GONE
        try {
            CoroutineScope(Dispatchers.IO).launch {
                val call = getRetrofit().create(PlagaApiService::class.java).getPlagaByName("api/plagas/$query")
                val plaga = call.body() as PlagasResponse
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    if(call.isSuccessful) {
                        val plagas = plaga.plagas
                        if (plagas.toString() != "[null]" ) {
                            binding.CardView.visibility = View.VISIBLE
                            binding.textView5.visibility = View.VISIBLE
                            binding.textView7.visibility = View.VISIBLE
                            binding.textView9.visibility = View.VISIBLE
                            Picasso.get().load(plagas[0].imgURL).into(binding.imgPlaga)
                            binding.txtNombrePlaga.text = plagas[0].name
                            binding.textViewDescription.text = plagas[0].description
                            binding.textViewDamage.text = plagas[0].damage
                            binding.textViewPrevention.text = plagas[0].prevention
                        } else {
                            Toast.makeText(
                                this@PhotoActivity,
                                "No hay resultados para: $query",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } else {
                        showError()
                    }
                }
            }
        } catch (error: Exception) {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this,"Error: $error", Toast.LENGTH_LONG).show()
        }
    }

    private fun showError() {
        Toast.makeText(this,"Ha ocurrido un error",Toast.LENGTH_SHORT).show()
    }

}