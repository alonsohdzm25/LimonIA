package com.limonia.views

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.limonia.apiplagas.PlagaApiService
import com.limonia.apiplagas.models.AuthResponse
import com.limonia.apiplagas.models.SignUpRequest
import com.limonia.core.CommonFunction.bandWidth
import com.limonia.core.EmailFormat
import com.limonia.core.RetrofitHelper
import com.limonia.databinding.ActivitySignUpBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            var intentIngresar = Intent(this, LoginActivity::class.java)
            startActivity(intentIngresar)
        }

        binding.progressBar.visibility = View.GONE

        binding.btnRegister.setOnClickListener {
            if (bandWidth(applicationContext)) {
                if (verificarCampos()) {
                    hideKeyboard()
                    signUp()
                } else {
                    Log.e("Error", "Verifique los campos ingresados")
                }
            }
        }
    }



    private fun signUp() {
        binding.progressBar.visibility = View.VISIBLE
        var username = binding.txtUsername.text.trim().toString()
        var email = binding.txtEmail.text.trim().toString()
        var pass = binding.txtPassword.text.trim().toString()
        var signUpRequest = SignUpRequest(username,email, pass)
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val call = RetrofitHelper.getRetrofit().create(PlagaApiService::class.java).signUp(signUpRequest)
                val respuesta = call.body() as AuthResponse

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    if(call.isSuccessful){
                        if (respuesta.success == "true") {
                            var intentMain = Intent(this@SignUpActivity, MainActivity::class.java)
                            intentMain.putExtra("token", respuesta.token)
                            startActivity(intentMain)
                        }else {
                            Toast.makeText(this@SignUpActivity, respuesta.message, Toast.LENGTH_LONG).show()
                        }
                    }else {
                        Toast.makeText(this@SignUpActivity,"Ocurrio un error al iniciar sesion", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }catch (e: Exception) {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this,"Error: $e", Toast.LENGTH_LONG).show()
        }

    }

    private fun verificarCampos() : Boolean {
        var username = binding.txtUsername.text.trim()
        var email = binding.txtEmail.text.trim()
        var pass = binding.txtPassword.text.trim()
        if(email.isEmpty() || pass.isEmpty() || username.isEmpty()) {
            Toast.makeText(this,"Campos Vacios", Toast.LENGTH_LONG).show()
            return false
        }else {
            if(username.length <= 5){
                Toast.makeText(this,"El nombre de usuario debe tener mas de 5 caracteres", Toast.LENGTH_LONG).show()
                return false
            } else if(!EmailFormat.emailValidation(email.toString())) {
                Toast.makeText(this,"Formato de correo no valido", Toast.LENGTH_LONG).show()
                return false
            }else if(pass.length < 8) {
                Toast.makeText(this,"La contraseña debe tener 8 caracteres o mas", Toast.LENGTH_LONG).show()
                return false
            } else {
                return true
            }
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken,0)
    }

}