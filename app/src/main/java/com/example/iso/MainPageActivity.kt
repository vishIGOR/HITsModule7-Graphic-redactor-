package com.example.iso

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.start_page.*

class MainPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.start_page)
        fun toSecondPage() {
            val toSecond = Intent(this@MainPageActivity, SecondPageActivity::class.java)
            startActivity(toSecond)
        }

        val startWorking = findViewById<View>(R.id.startButton)
        startWorking.setOnClickListener { toSecondPage() }
    }
}

