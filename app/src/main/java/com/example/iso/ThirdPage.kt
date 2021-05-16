package com.example.iso

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.dingmouren.layoutmanagergroup.skidright.SkidRightLayoutManager
import kotlinx.android.synthetic.main.choice_of_algo_page.*


class ThirdPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choice_of_algo_page)

        fun fromThirdToSecond() {
            val thirdToSecond = Intent(this@ThirdPage, SecondPage::class.java)
            startActivity(thirdToSecond)
        }

        val thirdPageToSecond = findViewById<View>(R.id.returnToSecondPageButton)
        thirdPageToSecond.setOnClickListener(View.OnClickListener { fromThirdToSecond() })

        val extras = intent.extras
        val myUri = Uri.parse(extras!!.getString("imageUri"))
        imageView.setImageURI(myUri)
    }
}