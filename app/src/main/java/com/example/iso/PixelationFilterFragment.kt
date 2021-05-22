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
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.fragment_pixelation_filter.*


class PixelationFilterFragment : Fragment() {

    private lateinit var photoPlace: ImageView
    var pixels: EditText? = null
    var startPixButton: ImageView? = null
    var savePixButton: ImageView? = null
    var returnButton: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_pixelation_filter, container, false)

        photoPlace = rootView.findViewById(R.id.placeForPix)
        val photo = (context as ThirdPageActivity).setPicture
        photoPlace.setImageBitmap(photo)

        pixels = rootView.findViewById(R.id.pixels)
        startPixButton = rootView.findViewById(R.id.doingSomethingWithPix)

        startPixButton?.setOnClickListener {
            if (pixels?.text.toString().toInt() < 2 || pixels?.text.toString().toInt() > 200) {
                Toast.makeText(
                    this.context,
                    "Значение должно быть в диапазоне от 2 до 200 пикселей",
                    5
                ).show()
            } else {
                lateinit var pixPhoto: Bitmap
                pixPhoto = pixelGraphic()
                (context as ThirdPageActivity).setPicture = pixPhoto
            }
        }

        savePixButton = rootView.findViewById(R.id.endWorkingWithImageButtonPix)
        savePixButton?.setOnClickListener() {
            returnToMainMenu()
        }

        returnButton = rootView.findViewById(R.id.returnToSecondPageButtonPix)
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

    fun pixelGraphic(): Bitmap {

        class Pixel(pixelDiameter: Int) {
            var red = 0
            var green = 0
            var blue = 0
            val pixelDiameter: Double = pixelDiameter * 1.0
            var pixelColor = 0

            fun addColors(pixel: Int) {


                red += pixel.red
                green += pixel.green
                blue += pixel.blue
            }

            fun updateValues() {
                red = 0
                green = 0
                blue = 0
            }

            fun setPixelColor() {
                red = (red / pixelDiameter / pixelDiameter).toInt()
                green = (green / pixelDiameter / pixelDiameter).toInt()
                blue = (blue / pixelDiameter / pixelDiameter).toInt()
                pixelColor = rgb(red, green, blue)
            }
        }

        val image = (placeForPix?.drawable as BitmapDrawable).bitmap

        val pixelDiameter = 10
        val imageWidth = image.width - image.width % pixelDiameter
        val imageHeight = image.height - image.height % pixelDiameter

        val imagePixels = IntArray(imageWidth * imageHeight)
        image.getPixels(imagePixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        var newImage = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)


        var currentPixel = Pixel(pixelDiameter)
        var newI = 0
        for (i in 0 until imageHeight * imageWidth / pixelDiameter / pixelDiameter) {
            newI =
                i * pixelDiameter + imageWidth * (pixelDiameter - 1) * (i * pixelDiameter / imageWidth)

            currentPixel.updateValues()
            for (x in 0 until pixelDiameter) {
                for (y in 0 until pixelDiameter) {
                    currentPixel.addColors(imagePixels[newI + x + y * imageWidth])
                }
            }
            currentPixel.setPixelColor()
            for (x in 0 until pixelDiameter) {
                for (y in 0 until pixelDiameter) {
                    imagePixels[newI + x + y * imageWidth] = currentPixel.pixelColor
                }
            }
        }
        newImage.setPixels(imagePixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        val draw: Drawable = BitmapDrawable(resources, newImage)
        placeForPix!!.setImageDrawable(draw)

        return newImage
    }
}
