package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    var bSelectImage: Button? = null // кнопка

    var iVPreviewImage: ImageView? = null // будущее место для изображения

    var SELECT_PICTURE = 200 // константа для сравнения, получилось ли

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bSelectImage = findViewById(R.id.bSelectImage) // достаём элементы из UI по айдишникам (см. activity_main.xml)
        iVPreviewImage = findViewById(R.id.iVPreviewImage)

        // триггерим функцию выбора изображения событием нажатия на кнопку
        bSelectImage?.setOnClickListener(View.OnClickListener { imageChooser() }) // ПРОВЕРКИ С NULL ВЫПОЛНЯТЬ ВСЕГДА
    }

    // фукнция выбора изображения
    fun imageChooser() {

        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) { //сравнение с константой

            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data!!.data // получение адреса изображения
                if (null != selectedImageUri) {
                    iVPreviewImage!!.setImageURI(selectedImageUri) // обновление layout с проверкой на null
                }
            }
        }
    }
}