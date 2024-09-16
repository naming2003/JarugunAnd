package com.example.jarugunandroid

import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class SeeDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_see_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main122)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()

        //For an synchronous task
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        if (intent.getStringExtra("comid").isNullOrEmpty()) {
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
            finish()
        }


        val back_buttom = findViewById<Button>(R.id.back_buttom)
        val band_text = findViewById<TextView>(R.id.band_text)
        val model_text = findViewById<TextView>(R.id.model_text)
        val serial_text = findViewById<TextView>(R.id.serial_text)
        val quanlity_text = findViewById<TextView>(R.id.quanlity_text)
        val price_text = findViewById<TextView>(R.id.price_text)
        val cpu_text = findViewById<TextView>(R.id.cpu_text)
        val ram_text = findViewById<TextView>(R.id.ram_text)
        val harddisk_text = findViewById<TextView>(R.id.harddisk_text)
        val imageViewFile = findViewById<ImageView>(R.id.imageView)

        band_text.text = intent.getStringExtra("band")
        model_text.text = intent.getStringExtra("run")
        serial_text.text = intent.getStringExtra("serial")
        quanlity_text.text = intent.getStringExtra("quanlity")
        price_text.text = intent.getStringExtra("price")
        cpu_text.text = intent.getStringExtra("cpu")
        ram_text.text = intent.getStringExtra("ram")
        harddisk_text.text = intent.getStringExtra("harddisk")
        val Image = intent.getStringExtra("image")

        if (Image != null) {
            val url = "http://10.13.4.241:3000" + Image.toString()
            // Load image using Glide
            Glide.with(this)
                .load(url)
                .into(imageViewFile)
        }

        back_buttom.setOnClickListener {
            val intent = Intent(this, AddDataActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}