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
import kotlinx.android.synthetic.main.fragment_unsharp_masking.*
import kotlin.math.*

class UnsharpMaskingFragment : Fragment(R.layout.fragment_unsharp_masking) {

    private lateinit var photoPlace: ImageView
    var effect: EditText? = null
    var radius: EditText? = null
    var threshold: EditText? = null
    var startMaskingButton: ImageView? = null
    var saveMaskingButton: ImageView? = null
    var returnButton: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_unsharp_masking, container, false)
        photoPlace = rootView.findViewById(R.id.placeForImageMasking)
        val photo = (context as ThirdPageActivity).setPicture
        photoPlace.setImageBitmap(photo)

        effect = rootView.findViewById(R.id.effect)
        radius = rootView.findViewById(R.id.radius)
        threshold = rootView.findViewById(R.id.threshold)

        startMaskingButton = rootView.findViewById(R.id.startMaskingButton)
        startMaskingButton?.setOnClickListener {
            if (effect == null || radius == null || threshold == null) {
                Toast.makeText(this.context, "Некорректный ввод данных", 5).show()
            } else {
                if (effect?.text.toString().toInt() < 50 || effect?.text.toString().toInt() > 500) {
                    Toast.makeText(this.context, "Значение эффекта должно быть в диапазоне от 50 до 100%", 5).show()
                } else {
                    if (radius?.text.toString().toInt() < 50 || radius?.text.toString().toInt() > 500) {
                        Toast.makeText(this.context, "Значение радиуса должно быть в диапазоне от 50 до 100%", 5).show()
                    } else {
                        if (threshold?.text.toString().toInt() < 5 || threshold?.text.toString().toInt() > 50) {
                            Toast.makeText(
                                this.context,
                                "Значение порога должно быть в диапазоне от 5 до 50 пикселей",
                                5
                            ).show()
                        } else {
                            lateinit var maskPhoto: Bitmap
                            maskPhoto = unsharpMasking()
                            (context as ThirdPageActivity).setPicture = maskPhoto
                        }
                    }
                }
            }
        }

        saveMaskingButton = rootView.findViewById(R.id.endWorkingWithImageButtonMask)
        saveMaskingButton?.setOnClickListener() {
            returnToMainMenu()
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

    private fun unsharpMasking(): Bitmap {

        var effectValue = effect?.text.toString().toInt()
        var radiusValue = radius?.text.toString().toInt()
        var thresholdValue = threshold?.text.toString().toInt()

        fun returnNumberFromCoordinates(imageWidth: Int, x: Int, y: Int): Int {
            return x + y * imageWidth
        }

        class Pixel() {
            var red = 0
            var green = 0
            var blue = 0

            fun addColor(coefficient: Double, pixel: Int) {

                red += (coefficient * pixel.red).roundToInt()
                green += (coefficient * pixel.green).roundToInt()
                blue += (coefficient * pixel.blue).roundToInt()
            }

            fun updateValues() {
                red = 0
                green = 0
                blue = 0
            }

            fun returnFinishValue(): Int {
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

            fun returnFilteringPixel(newPixel: Int): Int {
                if (abs(red - newPixel.red) > thresholdValue)
                    red += (effectValue / 100.0 * (red - newPixel.red)).roundToInt()
//
                if (abs(newPixel.green - green) > thresholdValue)
                    green += (effectValue / 100.0 * (green - newPixel.green)).roundToInt()
//
                if (abs(newPixel.blue - blue) > thresholdValue)
                    blue += (effectValue / 100.0 * (blue - newPixel.blue)).roundToInt()

                return returnFinishValue()
            }
        }


        val image = (placeForImageMasking?.drawable as BitmapDrawable).bitmap

        val imageWidth = image.width
        val imageHeight = image.height

        val oldPixels = IntArray(imageWidth * imageHeight)
        image.getPixels(oldPixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        var newImage = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888)

        var newPixels = IntArray(imageWidth * imageHeight)
        newImage.getPixels(newPixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        var coef = 0.0
        var currentPixel = Pixel()
        var pixelX = 0
        var pixelY = 0

        var sigma = radiusValue / 100.0
        var doubleSigma: Int = 2 * (sigma.roundToInt())
        val coefficients = Array(2 * doubleSigma + 1) { Array(2 * doubleSigma + 1) { 0.0 } }
        for (i in -1 * doubleSigma..doubleSigma) {
            for (j in -1 * doubleSigma..doubleSigma) {
                coefficients[i + doubleSigma][j + doubleSigma] =
                    1 / (sigma * sigma * 2 * PI) * (E.pow(-1 * ((i * i + j * j) / (2 * sigma * sigma))))
            }
        }
        for (k in 0 until imageWidth * imageHeight) {

            pixelX = k % imageWidth
            pixelY = k / imageWidth
            currentPixel.updateValues()
            for (i in -1 * doubleSigma..doubleSigma) {
                for (j in -1 * doubleSigma..doubleSigma) {

                    coef = coefficients[i + doubleSigma][j + doubleSigma]
                    if (pixelX + i >= imageWidth || pixelX + i < 0) {
                        if (pixelY + j >= imageHeight || pixelY + j < 0) {
                            currentPixel.addColor(
                                coef,
                                oldPixels[returnNumberFromCoordinates(
                                    imageWidth,
                                    pixelX + i - doubleSigma * (sign(i.toDouble()).toInt()),
                                    pixelY + j - doubleSigma * (sign(j.toDouble()).toInt())
                                )]
                            )
                        } else {

                            currentPixel.addColor(
                                coef,
                                oldPixels[returnNumberFromCoordinates(
                                    imageWidth,
                                    pixelX + i - doubleSigma * (sign(i.toDouble()).toInt()),
                                    pixelY + j
                                )]
                            )
                        }
                        continue
                    }
                    if (pixelY + j >= imageHeight || pixelY + j < 0) {
                        currentPixel.addColor(
                            coef,
                            oldPixels[returnNumberFromCoordinates(
                                imageWidth,
                                pixelX,
                                pixelY + j - doubleSigma * (sign(j.toDouble()).toInt())
                            )]
                        )
                    } else {
                        currentPixel.addColor(
                            coef,
                            oldPixels[returnNumberFromCoordinates(
                                imageWidth,
                                pixelX + i,
                                pixelY + j
                            )]
                        )
                    }
                }

            }
            newPixels[k] = currentPixel.returnFinishValue()

        }

        for (i in 0 until imageWidth * imageHeight) {
            currentPixel.updateValues()
            currentPixel.addColor(1.0, oldPixels[i])
            newPixels[i] = currentPixel.returnFilteringPixel(newPixels[i])
        }
        newImage.setPixels(newPixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        val draw: Drawable = BitmapDrawable(resources, newImage)
        placeForImageMasking!!.setImageDrawable(draw)

        return newImage
    }
}