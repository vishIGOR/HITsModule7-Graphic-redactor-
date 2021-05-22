package com.example.iso

import android.graphics.Bitmap
import android.graphics.Color.rgb
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.fragment_negative_filter.*


class NegativeFilterFragment : Fragment() {
    private lateinit var photoPlace: ImageView
    var saveNegativeButton: ImageView? = null
    var returnButton: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_negative_filter, container, false)
        photoPlace = rootView.findViewById(R.id.placeForN)
        val photo = (context as ThirdPageActivity).setPicture
        photoPlace.setImageBitmap(photo)

        saveNegativeButton = rootView.findViewById(R.id.endWorkingWithImageButtonN)
        saveNegativeButton?.setOnClickListener(){
            lateinit var negativePhoto: Bitmap
            negativePhoto = negative()
            (context as ThirdPageActivity).setPicture = negativePhoto
            returnToMainMenu()
        }

        returnButton = rootView.findViewById(R.id.returnToSecondPageButtonN)
        returnButton?.setOnClickListener() {
            returnToFiltersMenu()
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

    private fun returnToFiltersMenu() {
        val returnFragment: Fragment = FiltersFragment()
        val transForMenu: FragmentManager = this.fragmentManager!!

        transForMenu.commit {
            replace(R.id.fragments, returnFragment)
            setReorderingAllowed(true)
            addToBackStack("name") // name can be null
        }
    }

    fun negative(): Bitmap {

        class Pixel() {
            var red = 0
            var green = 0
            var blue = 0

            fun setValues(pixel: Int) {
                red = pixel.red
                green = pixel.green
                blue = pixel.blue
            }

            fun reverseColor(): Int {
                return rgb(255 - red, 255 - green, 255 - blue)
            }
        }

        val image = (placeForN?.drawable as BitmapDrawable).bitmap

        val imageWidth = image.width
        val imageHeight = image.height

        val imagePixels = IntArray(imageWidth * imageHeight)
        image.getPixels(imagePixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        var newImage = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)

        var currentPixel = Pixel()
        for (i in 0 until imageWidth * imageHeight) {
            currentPixel.setValues(imagePixels[i])
            imagePixels[i] = currentPixel.reverseColor()
        }
        newImage.setPixels(imagePixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        val draw: Drawable = BitmapDrawable(resources, newImage)
        placeForN!!.setImageDrawable(draw)

        return newImage
    }
}