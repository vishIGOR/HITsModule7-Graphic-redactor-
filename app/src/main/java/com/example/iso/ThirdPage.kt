package com.example.iso

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import kotlinx.android.synthetic.main.choice_of_algo_page.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction


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

        fun fromThirdToFinal() {
            val thirdToFinal = Intent(this@ThirdPage, FinalPage::class.java)
            startActivity(thirdToFinal)
        }

        val thirdPageToFinal = findViewById<View>(R.id.endWorkingWithImageButton)
        thirdPageToFinal.setOnClickListener(View.OnClickListener { fromThirdToFinal() })

        val extras = intent.extras
        val myUri = Uri.parse(extras!!.getString("imageUri"))
        placeForImageSelectionPage.setImageURI(myUri)

        fun fromThirdToCube() {
            val yoursFragment: Fragment = Cube()
            val trans: FragmentTransaction = supportFragmentManager.beginTransaction()
            trans.add(R.id.fragments, yoursFragment)
            trans.commit()
        }

        val thirdPageToCube = findViewById<View>(R.id.cubeButton)
        thirdPageToCube.setOnClickListener(View.OnClickListener { fromThirdToCube() })

        fun fromThirdToRotation() {
            val rotationFragment: Fragment = ImageRotation()
            val transForRotation: FragmentTransaction = supportFragmentManager.beginTransaction()
            transForRotation.add(R.id.fragments, rotationFragment)
            transForRotation.commit()
        }

        val thirdPageToRotation = findViewById<View>(R.id.imageRotationButton)
        thirdPageToRotation.setOnClickListener(View.OnClickListener { fromThirdToRotation() })
    }
}