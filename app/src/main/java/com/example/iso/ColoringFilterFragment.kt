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


class ColoringFilterFragment : Fragment() {

    private lateinit var photoPlace: ImageView
    var redInput: EditText? = null
    var greenInput: EditText? = null
    var blueInput: EditText? = null
    var startColoringButton: ImageView? = null
    var saveColoringButton: ImageView? = null
    var returnButton: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_coloring_filter, container, false)
        startColoringButton = rootView.findViewById(R.id.doingSomethingWithColor)
        photoPlace = rootView.findViewById(R.id.placeForColoring)
        val photo = (context as ThirdPageActivity).setPicture
        photoPlace.setImageBitmap(photo)

        startColoringButton?.setOnClickListener {
            if (redInput == null || greenInput == null || blueInput == null) {
                Toast.makeText(this.context, "Некорректный ввод данных", 5).show()
            } else {
                if (redInput?.text.toString().toInt() < -100 || redInput?.text.toString().toInt() > 100) {
                    Toast.makeText(this.context, "Значение должно быть в диапазоне от -100 до 100", 5).show()
                } else {
                    if (greenInput?.text.toString().toInt() < -100 || greenInput?.text.toString().toInt() > 100) {
                        Toast.makeText(this.context, "Значение должно быть в диапазоне от -100 до 100", 5).show()
                    } else {
                        if (blueInput?.text.toString().toInt() < -100 || blueInput?.text.toString().toInt() > 100) {
                            Toast.makeText(
                                this.context,
                                "Значение должно быть в диапазоне от -100 до 100",
                                5
                            ).show()
                        } else {
                            lateinit var colorPhoto: Bitmap
                            colorPhoto = gammaChanging()
                            (context as ThirdPageActivity).setPicture = colorPhoto
                        }
                    }
                }
            }
        }

        saveColoringButton = rootView.findViewById(R.id.endWorkingWithImageButtonColoring)
        saveColoringButton?.setOnClickListener() {
            returnToMainMenu()
        }

        returnButton = rootView.findViewById(R.id.returnToSecondPageButtonColoring)
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

    fun gammaChanging(): Bitmap {
        class Pixel() {
            var red = 0
            var green = 0
            var blue = 0

            val deltaR = 50
            val deltaG = 0
            val deltaB = 0

            fun setValues(pixel: Int) {
                red = pixel.red
                green = pixel.green
                blue = pixel.blue
            }

            fun changePixelGamma(): Int {
                val mediumValue: Int = (red + green + blue) / 3

                red = mediumValue + deltaR
                green = mediumValue + deltaG
                blue = mediumValue + deltaB

                if (red > 255)
                    red = 255
                else if (red < 0)
                    red = 0
                if (green > 255)
                    green = 255
                else if (green < 0)
                    green = 0
                if (blue > 255)
                    blue = 255
                else if (blue < 0)
                    blue = 0

                return rgb(red, green, blue)
            }
        }

        val image = (photoPlace?.drawable as BitmapDrawable).bitmap

        val imageWidth = image.width
        val imageHeight = image.height

        val imagePixels = IntArray(imageWidth * imageHeight)
        image.getPixels(imagePixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        var newImage = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)

        var currentPixel = Pixel()
        for (i in 0 until imageWidth * imageHeight) {
            currentPixel.setValues(imagePixels[i])
            imagePixels[i] = currentPixel.changePixelGamma()
        }
        newImage.setPixels(imagePixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        val draw: Drawable = BitmapDrawable(resources, newImage)
        photoPlace!!.setImageDrawable(draw)

        return newImage
    }
}