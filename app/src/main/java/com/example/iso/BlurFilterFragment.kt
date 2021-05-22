package com.example.iso

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.*


/*class BlurFilterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blur_filter, container, false)
    }

    fun gaussBlur() {
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
                return Color.rgb(red, green, blue)
            }

        }

        val image = (iVPreviewImage?.drawable as BitmapDrawable).bitmap

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

        var sigma = 2.0
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
                    if (pixelX+i >=imageWidth || pixelX+i<0){
                        if(pixelY+j>=imageHeight || pixelY+j<0){
                            currentPixel.addColor(
                                coef,
                                oldPixels[returnNumberFromCoordinates(
                                    imageWidth,
                                    pixelX + i-doubleSigma*(sign(i.toDouble()).toInt()),
                                    pixelY + j-doubleSigma*(sign(j.toDouble()).toInt())
                                )]
                            )
                        }else{

                            currentPixel.addColor(
                                coef,
                                oldPixels[returnNumberFromCoordinates(
                                    imageWidth,
                                    pixelX + i-doubleSigma*(sign(i.toDouble()).toInt()),
                                    pixelY + j
                                )]
                            )
                        }
                        continue
                    }
                    if(pixelY+j>=imageHeight || pixelY+j<0){
                        currentPixel.addColor(
                            coef,
                            oldPixels[returnNumberFromCoordinates(
                                imageWidth,
                                pixelX,
                                pixelY + j-doubleSigma*(sign(j.toDouble()).toInt())
                            )]
                        )
                    }else{
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
        newImage.setPixels(newPixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        val draw: Drawable = BitmapDrawable(resources, newImage)
        iVPreviewImage!!.setImageDrawable(draw)
    }

}*/