package com.example.iso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.start_page.*

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_page)
        fun toSecondPage() {
            val toSecond = Intent(this@MainPage, SecondPage::class.java)
            startActivity(toSecond)
        }

        val startWorking = findViewById<View>(R.id.startButton)
        startWorking.setOnClickListener(View.OnClickListener { toSecondPage() })
    }
}

