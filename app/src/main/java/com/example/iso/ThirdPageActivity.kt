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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import java.io.IOException
import by.kirich1409.viewbindingdelegate.viewBinding

class ThirdPageActivity : AppCompatActivity() {

    lateinit var myUri: Uri

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
