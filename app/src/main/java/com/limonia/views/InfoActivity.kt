package com.limonia.views

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.limonia.R
import com.limonia.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.containerEmail.setOnClickListener {
            sendEmail()
        }

    }

    private fun sendEmail() {
        var intent = Intent(Intent.ACTION_SENDTO)
        var uriText = "mailto:"+ Uri.encode("limonia_org@hotmail.com") +
                "?subject=" + Uri.encode("Información")
        var uri = Uri.parse(uriText)
        intent.data = uri
        startActivity(Intent.createChooser(intent, "Elije un cliente de correo"))
    }

}