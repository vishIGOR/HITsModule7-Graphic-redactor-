package com.example.iso

import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import kotlinx.android.synthetic.main.final_page.*
import android.os.Build
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.iso.databinding.FinalPageBinding
import java.io.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class FinalPageActivity : AppCompatActivity() {

    lateinit var myUri: Uri
    lateinit var setPicture: Bitmap

    private val viewBinding by viewBinding(FinalPageBinding::bind, R.id.save)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.final_page)

        val extras = intent.extras
        myUri = Uri.parse(extras!!.getString("imageUri"))
        setPicture = MediaStore.Images.Media.getBitmap(this.contentResolver, myUri);
        viewBinding.imageView.setImageBitmap(setPicture)

        saveButton.setOnClickListener {
            saveTempBitmap(setPicture)
        }
    }


    fun saveTempBitmap(bitmap: Bitmap) {
        if (isExternalStorageWritable()) {
            saveImage(bitmap)
        } else {
            //prompt the user or do something
        }
    }

    private fun saveImage(finalBitmap: Bitmap) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/saved_images")
        myDir.mkdirs()
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val fname = "Shutta_$timeStamp.jpg"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* Checks if external storage is available for read and write */
    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return if (Environment.MEDIA_MOUNTED == state) {
            true
        } else false
    }
}