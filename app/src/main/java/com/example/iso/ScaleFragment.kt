package com.example.iso

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import kotlin.math.roundToInt
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit


class ScaleFragment : Fragment() {
    private lateinit var photoPlace: ImageView
    var coefficientInput: EditText? = null
    var startScaleButton: ImageView? = null
    var saveScaleButton: ImageView? = null
    var returnButton: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_scale, container, false)
        photoPlace = rootView.findViewById(R.id.placeForScale)
        val photo = (context as ThirdPageActivity).setPicture
        photoPlace.setImageBitmap(photo)

        coefficientInput = rootView.findViewById(R.id.coefficient)
        startScaleButton = rootView.findViewById(R.id.doingSomethingWithScale)
        startScaleButton?.setOnClickListener {
            lateinit var rotatedPhoto: Bitmap
            rotatedPhoto = imageScaling()
            (context as ThirdPageActivity).setPicture = rotatedPhoto
        }

        saveScaleButton = rootView.findViewById(R.id.endWorkingWithImageButtonScale)
        saveScaleButton?.setOnClickListener() {
            returnToMainMenu()
        }

        returnButton = rootView.findViewById(R.id.returnToSecondPageButtonScale)
        returnButton?.setOnClickListener{
            returnToMainMenuTwo()
        }

        return rootView
    }

    private fun returnToMainMenu() {
        val returnFragment: Fragment = MainMenuFragment()
        val transForMenu: FragmentManager = this.fragmentManager!!

        transForMenu.commit {
            add(R.id.fragments, returnFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }

    private fun returnToMainMenuTwo() {
        val returnFragment: Fragment = MainMenuFragment()
        val transForMenu: FragmentManager = this.fragmentManager!!

        transForMenu.commit {
            replace(R.id.fragments, returnFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }

    fun imageScaling(): Bitmap {

        val coefficient = coefficientInput?.text.toString().toFloat()
        val image = (photoPlace?.drawable as BitmapDrawable).bitmap
        val oldHeight = image.height
        val oldWidth = image.width

        val newHeight = (oldHeight * coefficient).roundToInt()
        val newWidth = (oldWidth * coefficient).roundToInt()
        var oldX = 0
        var oldY = 0
        val newImage = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)

        for (i in 0 until newWidth) {
            for (j in 0 until newHeight) {
                oldX = (i / coefficient).toInt()
                oldY = (j / coefficient).toInt()
                var colorOfPixel = image.getPixel(oldX, oldY)
                newImage.setPixel(i, j, colorOfPixel)
            }
        }
        val draw: Drawable = BitmapDrawable(resources, newImage)
        photoPlace!!.setImageDrawable(draw)

        return newImage
    }
}