package com.limonia.views

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.limonia.apiplagas.PlagaApiService
import com.limonia.apiplagas.models.ChangePasswordResponse
import com.limonia.apiplagas.models.EmailRequest
import com.limonia.core.CommonFunction.bandWidth
import com.limonia.core.EmailFormat
import com.limonia.core.RetrofitHelper
import com.limonia.databinding.ActivityPasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        binding.btnAceptar.setOnClickListener{
            if(bandWidth(applicationContext)) {
                if(verificarEmail()){
                    hideKeyboard()
                    recuperarContraseña()
                } else {
                    Log.e("Error: ", "Verifique los campos ingresados")
                }
            }
        }

    }

    private fun verificarEmail() : Boolean {
        var email = binding.txtEmailRecoverPass.text.trim()
        if(!email.isEmpty()){
            return if(EmailFormat.emailValidation(email.toString())){
                true
            } else {
                Toast.makeText(this,"Ingrese un email valido",Toast.LENGTH_SHORT).show()
                false
            }
        }else {
            Toast.makeText(this,"Email requerido",Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun recuperarContraseña() {
        binding.progressBar.visibility = View.VISIBLE
        var email = binding.txtEmailRecoverPass.text.trim().toString()
        var emailRequest = EmailRequest(email)
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val call = RetrofitHelper.getRetrofit().create(PlagaApiService::class.java).recoveryPassword(emailRequest)
                val respuesta = call.body() as ChangePasswordResponse

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    if(call.isSuccessful){
                        if (respuesta.success == "true") {
                            Toast.makeText(this@PasswordActivity,respuesta.message,Toast.LENGTH_SHORT).show()
                            var intentMain = Intent(this@PasswordActivity, LoginActivity::class.java)
                            startActivity(intentMain)
                        }else {
                            Toast.makeText(this@PasswordActivity, respuesta.message, Toast.LENGTH_LONG).show()
                        }
                    }else {
                        Toast.makeText(this@PasswordActivity,"Ocurrio un error", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }catch (e: Exception) {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this,"Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken,0)
    }

}