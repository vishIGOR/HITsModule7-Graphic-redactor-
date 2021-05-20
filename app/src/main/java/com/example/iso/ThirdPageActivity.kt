package com.example.iso

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import com.example.iso.databinding.ChoiceOfAlgoPageBinding
import kotlinx.android.synthetic.main.choice_of_algo_page.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.io.IOException
import by.kirich1409.viewbindingdelegate.viewBinding

class ThirdPageActivity : AppCompatActivity() {

    private val viewBinding by viewBinding(ChoiceOfAlgoPageBinding::bind, R.id.constL)
    lateinit var myUri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choice_of_algo_page)

        val extras = intent.extras
        myUri = Uri.parse(extras!!.getString("imageUri"))
        viewBinding.placeForImageSelectionPage.setImageURI(myUri)

        fun fromThirdToSecond() {
            val thirdToSecond = Intent(this@ThirdPageActivity, SecondPageActivity::class.java)
            startActivity(thirdToSecond)
        }

        viewBinding.returnToSecondPageButton.setOnClickListener { fromThirdToSecond() }

        fun fromThirdToFinal() {
            val thirdToFinal = Intent(this@ThirdPageActivity, FinalPageActivity::class.java)
            startActivity(thirdToFinal)
        }

        viewBinding.endWorkingWithImageButton.setOnClickListener { fromThirdToFinal() }

        fun fromThirdToCube() {
            val cubeFragment: Fragment = CubeFragment()
            val trans: FragmentTransaction = supportFragmentManager.beginTransaction()
            trans.replace(R.id.fragments, cubeFragment)
            trans.commit()
        }

        viewBinding.cubeButton.setOnClickListener { fromThirdToCube() }

        fun fromThirdToRotation() {
            val rotationFragment: Fragment = ImageRotationFragment()
            val transForRotation: FragmentTransaction = supportFragmentManager.beginTransaction()
            transForRotation.replace(R.id.fragments, rotationFragment)
            transForRotation.commit()
        }

        viewBinding.imageRotationButton.setOnClickListener { fromThirdToRotation() }
    }

    fun fromUriToBitmap(): Bitmap {
        lateinit var bitmapPicture: Bitmap
        try {
            bitmapPicture = MediaStore.Images.Media.getBitmap(this.contentResolver, myUri)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmapPicture
    }
}
