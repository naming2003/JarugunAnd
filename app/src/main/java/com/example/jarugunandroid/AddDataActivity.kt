package com.example.jarugunandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import java.io.File

class AddDataActivity : AppCompatActivity() {
    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_data)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        supportActionBar?.hide()

        //For an synchronous task
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val button1 = findViewById<Button>(R.id.button1)
        val button = findViewById<Button>(R.id.button)
        val comid = findViewById<EditText>(R.id.comid)

        val band = findViewById<EditText>(R.id.band)
        val run = findViewById<EditText>(R.id.run)
        val serial = findViewById<EditText>(R.id.serial)
        val jamnun = findViewById<EditText>(R.id.jamnun)
        val raka = findViewById<EditText>(R.id.raka)
        val cpuspeed = findViewById<EditText>(R.id.cpuspeed)
        val ram = findViewById<EditText>(R.id.ram)
        val disk = findViewById<EditText>(R.id.disk)
        val imageView2 = findViewById<ImageView>(R.id.imageView2)
        val textView27 = findViewById<TextView>(R.id.textView27)

        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                imageUri = data?.data
                imageUri?.let {
                    imageView2.setImageURI(it)
                    // Handle the image URI here if needed (e.g., uploading to a server)
                }
            }
        }
        textView27.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }

        button.setOnClickListener {
            val bandText = band.text.toString()
            val runText = run.text.toString()
            val serialText = serial.text.toString()
            val jamnunText = jamnun.text.toString()
            val rakaText = raka.text.toString()
            val cpuspeedText = cpuspeed.text.toString()
            val ramText = ram.text.toString()
            val diskText = disk.text.toString()

            if(bandText.isEmpty() || runText.isEmpty() || serialText.isEmpty() ||
                jamnunText.isEmpty() || rakaText.isEmpty() || cpuspeedText.isEmpty() || ramText.isEmpty() || diskText.isEmpty()){
                Toast.makeText(applicationContext, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val url = "http://10.13.4.241:3000/api/addproduct"
            val okHttpClient = OkHttpClient()
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("Namebrand", bandText)
                .addFormDataPart("Model", runText)
                .addFormDataPart("Serilnumber", serialText)
                .addFormDataPart("Amount", jamnunText)
                .addFormDataPart("Price", rakaText)
                .addFormDataPart("Cpucom", cpuspeedText)
                .addFormDataPart("Retention", ramText)
                .addFormDataPart("Harddisk", diskText)

            imageUri?.let {
                val file = File(it.path!!)
                val requestFile = file.asRequestBody("image/jpeg".toMediaType())
                builder.addFormDataPart("Image", file.name, requestFile)
            }
            val requestBody = builder.build()

            val request: Request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = okHttpClient.newCall(request).execute()
            if(response.isSuccessful){
                val obj = JSONObject(response.body!!.string())
                val status = obj["status"].toString()
                if (status == "true") {
                    val message = obj["message"].toString()
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                } else {
                    val message = obj["message"].toString()
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }
            }
        }

        button1.setOnClickListener {
            val comids = comid.text.toString()

            if(comids.isEmpty()){
                Toast.makeText(applicationContext, "กรุณากรอกข้อมูลให้ครบ", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val url = "http://10.13.4.241:3000/api/getproduct/"+comids
            val okHttpClient = OkHttpClient()
            val request: Request = Request.Builder()
                .url(url)
                .get()
                .build()
            val response = okHttpClient.newCall(request).execute()
            if(response.isSuccessful){
                val obj = JSONObject(response.body!!.string())
                val status = obj["status"].toString()
                if (status == "true") {
                    val message = obj["message"].toString()
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    val intent = Intent(this, SeeDataActivity::class.java)
                    intent.putExtra("comid", obj["Com_ID"].toString())
                    intent.putExtra("band", obj["Namebrand"].toString())
                    intent.putExtra("run", obj["Model"].toString())
                    intent.putExtra("serial", obj["Serilnumber"].toString())
                    intent.putExtra("quanlity", obj["Amount"].toString())
                    intent.putExtra("price", obj["Price"].toString())
                    intent.putExtra("cpu", obj["Cpucom"].toString())
                    intent.putExtra("ram", obj["Retention"].toString())
                    intent.putExtra("harddisk", obj["Harddisk"].toString())
                    intent.putExtra("image", obj["Image"].toString())
                    startActivity(intent)
                    finish()
                } else {
                    val message = obj["message"].toString()
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                    comid.text.clear()
                }
            }
        }
    }
}