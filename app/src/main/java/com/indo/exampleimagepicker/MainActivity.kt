package com.indo.exampleimagepicker

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.indo.imagepicker.ImagePickerSelectActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val imagePicker = ImagePickerSelectActivity(
            activity = this,
            onImageSelected = { imagePath ->
                // Handle selected image
                Log.d("TAG"," path image ${imagePath}")
                findViewById<TextView>(R.id.path).text = imagePath
            },
            onError = { errorMessage ->
                // Handle errors
                Log.d("TAG"," error message ${errorMessage}")
            }
        )
        findViewById<Button>(R.id.btn_image).setOnClickListener {
            imagePicker.showImagePickerDialog(supportFragmentManager)
        }
    }
}