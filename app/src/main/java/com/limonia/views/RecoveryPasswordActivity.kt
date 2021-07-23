package com.limonia.views

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.limonia.apiplagas.PlagaApiService
import com.limonia.apiplagas.models.ChangePasswordRequest
import com.limonia.apiplagas.models.ChangePasswordResponse
import com.limonia.core.CommonFunction
import com.limonia.core.CommonFunction.bandWidth
import com.limonia.core.RetrofitHelper
import com.limonia.databinding.ActivityRecoveryPasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecoveryPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecoveryPasswordBinding
    private lateinit var token : String

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecoveryPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handleIntent()

        binding.progressBar.visibility = View.GONE

        binding.btnCancelar.setOnClickListener {
            Log.d("Mensaje: ","Proceso de recuperacion de contraseña cancelado")
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnAceptar.setOnClickListener {
            hideKeyboard()
            if(bandWidth(applicationContext)) {
                if (verificarContrasenas()) {
                    actualizarPassword(token)
                } else {
                    Log.e("Error: ", "Algo ha salido mal")
                }
            }
        }
    }

    private fun handleIntent() {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data

        if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.lastPathSegment?.also {
                    token ->
                this.token = token
            }
        }
      }

    private fun actualizarPassword(t: String) {
        binding.progressBar.visibility = View.VISIBLE
        var pass = binding.txtPassword.text.trim()
        var changePasswordRequest = ChangePasswordRequest(pass.toString())
        try{
            CoroutineScope(Dispatchers.IO).launch {
                val call = RetrofitHelper.getRetrofit().create(PlagaApiService::class.java).updateUser("api/user/$t", changePasswordRequest)
                val respuesta = call.body() as ChangePasswordResponse

                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    if(call.isSuccessful){
                        if (respuesta.success == "true") {
                            Toast.makeText(this@RecoveryPasswordActivity,respuesta.message, Toast.LENGTH_LONG).show()
                            var intentMain = Intent(this@RecoveryPasswordActivity, LoginActivity::class.java)
                            startActivity(intentMain)
                        }else {
                            Toast.makeText(this@RecoveryPasswordActivity, respuesta.message, Toast.LENGTH_LONG).show()
                        }
                    }else {
                        Toast.makeText(this@RecoveryPasswordActivity,"Ocurrio un error al intentar actualizar la contraseña", Toast.LENGTH_LONG).show()
                    }
                }

            }
        }catch (e: Exception) {
            binding.progressBar.visibility = View.GONE
            Toast.makeText(this,"Error: $e", Toast.LENGTH_LONG).show()
        }
    }

    private fun verificarContrasenas() : Boolean {
        val pass = binding.txtPassword.text.trim().toString()
        val repeatPass = binding.txtPasswordRepeat.text.trim().toString()

        if (pass.length >= 8) {
            if (pass == repeatPass) {
                Log.d("","Pass: $pass == PassR: $repeatPass")
                return true
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                return false
            }
        }else {
            Toast.makeText(this, "La contraseña debe tener 8 caracteres o mas", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun hideKeyboard() {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken,0)
    }

}