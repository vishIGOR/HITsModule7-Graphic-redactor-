package com.example.myapplication

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.rgb
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.*

class MainActivity2 : AppCompatActivity() {
    var bSelectImage: Button? = null // кнопка

    var iVPreviewImage: ImageView? = null // будущее место для изображения

    var startButton: Button? = null

    var startGaussBlur: Button? = null

    var startNegative: Button? = null

    var startPixelGraphic: Button? = null

    var startGammaChanging: Button? = null

    var SELECT_PICTURE = 200 // константа для сравнения, получилось ли


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        bSelectImage =
            findViewById(R.id.bSelectImage) // достаём элементы из UI по айдишникам (см. activity_main.xml)
        iVPreviewImage = findViewById(R.id.iVPreviewImage)
        startButton = findViewById(R.id.bStart)
        // триггерим функцию выбора изображения событием нажатия на кнопку
        bSelectImage?.setOnClickListener(View.OnClickListener { imageChooser() }) // ПРОВЕРКИ С NULL ВЫПОЛНЯТЬ ВСЕГДА

        startGaussBlur = findViewById(R.id.bGaussBlur)
        startNegative = findViewById(R.id.bNegative)
        startPixelGraphic = findViewById(R.id.bPixelGraphic)
        startGammaChanging = findViewById(R.id.bGammaChanging)

        startGaussBlur?.setOnClickListener { gaussBlur() }
        startNegative?.setOnClickListener { negative() }
        startPixelGraphic?.setOnClickListener { pixelGraphic() }
        startGammaChanging?.setOnClickListener { gammaChanging() }
    }

    // фукнция выбора изображения
    fun imageChooser() {

        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) { //сравнение с константой

            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data!!.data // получение адреса изображения
                if (null != selectedImageUri) {
                    iVPreviewImage!!.setImageURI(selectedImageUri) // обновление layout с проверкой на null
                }
            }
        }
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

        var sigma = 1.5
        var doubleSigma: Int = 2 * (sigma.roundToInt())
        val coefficients = Array(2 * doubleSigma + 1) { Array(2 * doubleSigma + 1) { 0.0 } }
        for (i in -1 * doubleSigma..doubleSigma) {
            for (j in -1 * doubleSigma..doubleSigma) {
                coefficients[i + doubleSigma][j + doubleSigma] =
                    1 / (sigma * sigma * 2 * PI) * (E.pow(-1 * ((i * i + j * j) / (2 * sigma * sigma))))
            }
        }
        for (i in 0 until imageWidth * imageHeight) {

            pixelX = i % imageWidth
            pixelY = i / imageWidth
            currentPixel.updateValues()

            for (i in -1 * doubleSigma..doubleSigma) {
                for (j in -1 * doubleSigma..doubleSigma) {
                    if (pixelX + i in 0 until imageWidth && pixelY + j in 0 until imageHeight) {

                        coef = coefficients[i + doubleSigma][j + doubleSigma]
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
            newPixels[i] = currentPixel.returnFinishValue()

        }
        newImage.setPixels(newPixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        val draw: Drawable = BitmapDrawable(resources, newImage)
        iVPreviewImage!!.setImageDrawable(draw)
    }

    fun negative() {

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

        val image = (iVPreviewImage?.drawable as BitmapDrawable).bitmap

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
        iVPreviewImage!!.setImageDrawable(draw)
    }

    fun pixelGraphic() {

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

        val image = (iVPreviewImage?.drawable as BitmapDrawable).bitmap

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
        iVPreviewImage!!.setImageDrawable(draw)
    }

    fun gammaChanging() {
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

            fun changePixelGamma():Int {
                val mediumValue: Int = (red + green + blue) / 3

                red = mediumValue +deltaR
                green = mediumValue +deltaG
                blue = mediumValue +deltaB

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

                return rgb(red,green,blue)
            }
        }

        val image = (iVPreviewImage?.drawable as BitmapDrawable).bitmap

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
        iVPreviewImage!!.setImageDrawable(draw)
    }
}