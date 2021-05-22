package com.example.iso

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import java.io.IOException
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.iso.databinding.ChoiceOfAlgoPageBinding

class ThirdPageActivity : AppCompatActivity() {

    lateinit var myUri: Uri
    lateinit var setPicture: Bitmap

    private val viewBinding by viewBinding(ChoiceOfAlgoPageBinding::bind, R.id.constL)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.choice_of_algo_page)

        val menuFragment: Fragment = MainMenuFragment()
        val transForMenu: FragmentManager = supportFragmentManager

        transForMenu.commit {
            replace(R.id.fragments, menuFragment)
            setReorderingAllowed(true)
        }

        val extras = intent.extras
        myUri = Uri.parse(extras!!.getString("imageUri"))
        setPicture = fromUriToBitmap(myUri)
        viewBinding.placeForPhoto.setImageBitmap(setPicture)
    }

    fun fromUriToBitmap (picture: Uri): Bitmap {
        lateinit var bitmapPicture: Bitmap
        try {
            bitmapPicture = MediaStore.Images.Media.getBitmap(this.contentResolver, picture)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmapPicture
    }

}
