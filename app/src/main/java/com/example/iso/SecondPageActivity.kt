package com.example.iso

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.select_image_page.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.choice_of_algo_page.*

class SecondPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.select_image_page)

        fun toFirstPage() {
            val toFirst = Intent(this@SecondPageActivity, MainPageActivity::class.java)
            startActivity(toFirst)
        }

        val backToFirst = findViewById<View>(R.id.backToStartPage)
        backToFirst.setOnClickListener { toFirstPage() }

        imageFromGalleryButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    ImagePicker.with(this)
                        .galleryOnly()    //User can only select image from Gallery
                        .createIntent { intent ->
                            startForProfileImageResult.launch(intent)
                        }

                }
            } else {
                ImagePicker.with(this)
                    .galleryOnly()    //User can only select image from Gallery
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
        }

        imageFromCameraButton.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    val permissions = arrayOf(Manifest.permission.CAMERA);
                    requestPermissions(permissions, PERMISSION_CODE);
                } else ImagePicker.with(this)
                    .cameraOnly()
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            } else {
                ImagePicker.with(this)
                    .cameraOnly()
                    .createIntent { intent ->
                        startForProfileImageResult.launch(intent)
                    }
            }
        }
    }

    companion object {
        const val IMAGE_PICK_CODE = 1000;
        private const val PERMISSION_CODE = 1001;
    }

    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            if (resultCode == Activity.RESULT_OK) {
                //Image Uri will not be null for RESULT_OK
                val imageUri = data?.data!!
                val theIntent = Intent(this, ThirdPageActivity::class.java)
                theIntent.putExtra("imageUri", imageUri.toString())
                startActivity(theIntent)

            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageUri: Uri = data?.data!!
            val theIntent = Intent(this, ThirdPageActivity::class.java)
            theIntent.putExtra("imageUri", imageUri.toString())
            startActivity(theIntent)
        }
    }
}