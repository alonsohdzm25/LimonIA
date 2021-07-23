package com.limonia.views

import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.limonia.R
import com.limonia.core.CommonFunction.bandWidth
import com.limonia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.setNavigationItemSelectedListener(this)

        setSupportActionBar(binding.appBar.toolbarMain)

        binding.container.setOnClickListener{
            var intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBar.toolbarMain,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_plagaDetection -> {
                var intent = Intent(this, CameraActivity::class.java)
                startActivity(intent)
            }
            R.id.item_plagas -> {
                if(bandWidth(applicationContext)) {
                    var intent = Intent(this, PlagasActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.item_productos -> {
                if(bandWidth(applicationContext)) {
                    var intent = Intent(this, ProductoActivity::class.java)
                    startActivity(intent)
                }
            }
            R.id.item_faq -> {
                var intent = Intent(this, FaqActivity::class.java)
                startActivity(intent)
            }
            R.id.item_info -> {
                var intent = Intent(this, InfoActivity::class.java)
                startActivity(intent)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else {
            val alert : AlertDialog.Builder? = this?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    setPositiveButton(R.string.si
                    ) { dialog, id ->
                        var intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    setNegativeButton(R.string.no
                    ) { dialog, id ->
                        Log.d("Mensaje","Cancelacion de cierre de sesion")
                    }
                }
            }

            alert?.setMessage("¿Seguro que desea cerrar su sesión?")
                    ?.setTitle("Cerrar Sesión")

            val dialog: AlertDialog? = alert?.create()
            dialog?.show()

        }
    }
}