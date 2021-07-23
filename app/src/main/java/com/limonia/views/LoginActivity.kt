package com.limonia.views

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.limonia.R
import com.limonia.apiplagas.PlagaApiService
import com.limonia.apiplagas.models.AuthResponse
import com.limonia.apiplagas.models.SignInRequest
import com.limonia.core.CommonFunction.bandWidth
import com.limonia.core.EmailFormat
import com.limonia.core.RetrofitHelper.getRetrofit
import com.limonia.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_LimonIA)
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.progressBar.visibility = View.GONE

        binding.btnLoginEmail.setOnClickListener {
            if(bandWidth(applicationContext)) {
                if (verificarCampos()) {
                    hideKeyboard()
                    signIn()
                } else {
                    Log.e("Error", "Verifique los campos ingresados")
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            var intentRegister = Intent(this, SignUpActivity::class.java)
            startActivity(intentRegister)
        }
        binding.btnForgetPass.setOnClickListener{
            var intentPass = Intent(this, PasswordActivity::class.java)
            startActivity(intentPass)
        }

    }

    private fun verificarCampos() : Boolean {
        var email = binding.txtEmail.text.trim()
        var pass = binding.txtPassword.text.trim()
        if(email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this,"Correo / Contraseña requerido", Toast.LENGTH_LONG).show()
            return false
        }else {
            if(!EmailFormat.emailValidation(email.toString())) {
                Toast.makeText(this,"Formato de correo no valido", Toast.LENGTH_LONG).show()
                return false
            }else {
                return true
            }
        }
    }



    private fun signIn() {
        binding.progressBar.visibility = View.VISIBLE
        var email = binding.txtEmail.text.trim().toString()
        var pass = binding.txtPassword.text.trim().toString()
        var signInRequest = SignInRequest(email, pass)
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val call = getRetrofit().create(PlagaApiService::class.java).signIn(signInRequest)
                val respuesta = call.body() as AuthResponse

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    if(call.isSuccessful){
                        if (respuesta.success == "true") {
                            var intentMain = Intent(this@LoginActivity, MainActivity::class.java)
                            intentMain.putExtra("token", respuesta.token)
                            startActivity(intentMain)
                        }else {
                            Toast.makeText(this@LoginActivity, respuesta.message, Toast.LENGTH_LONG).show()
                        }
                    }else {
                        Toast.makeText(this@LoginActivity,"Ocurrio un error al iniciar sesion", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }catch (e: Throwable) {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this,"Error: $e", Toast.LENGTH_LONG).show()
        }

    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken,0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

}