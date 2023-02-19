package com.escalantedanny.candesk.dogs.activities

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import coil.load
import com.escalantedanny.candesk.databinding.ActivityWholeImageBinding
import java.io.File

class WholeImageActivity : AppCompatActivity() {

    companion object {
        const val PHOTO_PATH_KEY = "photo_path_key"
    }

    lateinit var binding: ActivityWholeImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWholeImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pathImage = intent.extras?.getString(PHOTO_PATH_KEY) ?: ""
        val uri = Uri.parse(pathImage)
        val path = uri.path
        if (path == null) {
            Toast.makeText(this, "Photo no debe ser vacia", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        binding.wholeImage.load(File(path))

    }
}