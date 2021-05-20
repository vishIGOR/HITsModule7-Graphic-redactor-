package com.example.myapplication

import android.R.attr.bitmap
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Color.rgb
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import kotlin.math.*


class MainActivity : AppCompatActivity() {

    var bSelectImage: Button? = null // кнопка

    var iVPreviewImage: ImageView? = null // будущее место для изображения

    var rotatingAngle: EditText? = null

    var startButton: Button? = null

    var startMaskingButton: Button? = null

    var SELECT_PICTURE = 200 // константа для сравнения, получилось ли


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bSelectImage =
                findViewById(R.id.bSelectImage) // достаём элементы из UI по айдишникам (см. activity_main.xml)
        iVPreviewImage = findViewById(R.id.iVPreviewImage)
        rotatingAngle = findViewById(R.id.angle)
        startButton = findViewById(R.id.bStart)
        startMaskingButton = findViewById(R.id.bStartMasking)
        // триггерим функцию выбора изображения событием нажатия на кнопку
        bSelectImage?.setOnClickListener(View.OnClickListener { imageChooser() }) // ПРОВЕРКИ С NULL ВЫПОЛНЯТЬ ВСЕГДА
        startButton?.setOnClickListener(View.OnClickListener { imageRotating() })
        startMaskingButton?.setOnClickListener(View.OnClickListener { unsharpMasking() })
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

    fun imageRotating() {

        class Point(x: Int, y: Int) {
            var x: Int = x
            var y: Int = y
            fun threeShears(tanHalfAngle: Double, sinAngle: Double) {
                var oldX = x
                var oldY = y
                x = (oldX - oldY * tanHalfAngle).roundToInt()
                y = oldY
                oldX = x
                oldY = (sinAngle * x + y).roundToInt()
                x = (oldX - oldY * tanHalfAngle).roundToInt()
                y = oldY
            }

        }

        val angle = rotatingAngle?.text.toString().toInt()
        val image = (iVPreviewImage?.drawable as BitmapDrawable).bitmap

        val oldWidth = image.width
        val oldHeight = image.height

        val oldPixels = IntArray(oldWidth * oldHeight)
        image.getPixels(oldPixels, 0, oldWidth, 0, 0, oldWidth, oldHeight)

        val sinAngle = sin(angle.toDouble() * PI / 180.0)
        val tanHalfAngle = tan(angle.toDouble() * PI / 360.0)


        var points: Array<Point> = Array(4) { Point(0, 0) }
        points[0] = Point(0, 0)
        points[1] = Point(oldWidth - 1, 0)
        points[2] = Point(0, oldHeight - 1)
        points[3] = Point(oldWidth - 1, oldHeight - 1)

        for (element in points) {
            element.threeShears(tanHalfAngle, sinAngle)
        }

        var maxX = points[0].x
        var maxY = points[0].y
        var deltaX = points[0].x
        var deltaY = points[0].y

        for (element in points) {
            if (element.x < deltaX)
                deltaX = element.x

            if (element.y < deltaY)
                deltaY = element.y

            if (element.x > maxX)
                maxX = element.x

            if (element.y > maxY)
                maxY = element.y

        }

        val newWidth = maxX - deltaX
        val newHeight = maxY - deltaY

        var newImage = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        newImage.eraseColor(Color.WHITE)

        var newPixels = IntArray(newWidth * newHeight)
        newImage.getPixels(newPixels, 0, newWidth, 0, 0, newWidth, newHeight)

        var currentPoint = Point(0, 0)
        for (i in 0 until oldWidth * oldHeight) {
            currentPoint.x = i % oldWidth
            currentPoint.y = i / oldWidth

            currentPoint.threeShears(tanHalfAngle, sinAngle)

            currentPoint.x -= deltaX
            currentPoint.y -= deltaY

            if (currentPoint.y * newWidth + currentPoint.x >= 0
                    && currentPoint.y * newWidth + currentPoint.x < newWidth * newHeight
            ) {
                newPixels[currentPoint.y * newWidth + currentPoint.x] = oldPixels[i]
            }
        }

        newImage.setPixels(newPixels, 0, newWidth, 0, 0, newWidth, newHeight)

        val draw: Drawable = BitmapDrawable(resources, newImage)
        iVPreviewImage!!.setImageDrawable(draw)
    }

    fun unsharpMasking() {

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

            fun returnFilteringPixel(newPixel: Int, c: Int): Int {
                if (abs(red - newPixel.red) > 10)
                    red += (c/100.0 * (red - newPixel.red)).roundToInt()
//
                if (abs(newPixel.green - green) > 10)
                    green += (c/100.0 * (green - newPixel.green)).roundToInt()
//
                if (abs(newPixel.blue - blue) > 10)
                    blue += (c/100.0 * (blue - newPixel.blue)).roundToInt()




                return returnFinishValue()
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

        var sigma = 1.0
        var doubleSigma:Int = 2 * (sigma.roundToInt())
        val coefficients = Array(2 * doubleSigma+1) { Array(2 * doubleSigma+1) { 0.0 } }
        for (i in -1*doubleSigma..doubleSigma) {
            for (j in -1 * doubleSigma..doubleSigma) {
                coefficients[i+doubleSigma][j+doubleSigma] = 1 / (sigma * sigma * 2 * PI) * (E.pow(-1 * ((i * i + j * j) / (2 * sigma * sigma))))
            }
        }
        for (i in 0 until imageWidth * imageHeight) {

            pixelX = i % imageWidth
            pixelY = i / imageWidth
            currentPixel.updateValues()

            for (i in -1*doubleSigma..doubleSigma) {
                for (j in -1 * doubleSigma..doubleSigma) {
                    if (pixelX + i in 0 until imageWidth && pixelY + j in 0 until imageHeight) {

                        coef = coefficients[i+doubleSigma][j+doubleSigma]
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

        for (i in 0 until imageWidth * imageHeight) {
            currentPixel.updateValues()
            currentPixel.addColor(1.0, oldPixels[i])
            newPixels[i] = currentPixel.returnFilteringPixel(newPixels[i], 100)
        }
        newImage.setPixels(newPixels, 0, imageWidth, 0, 0, imageWidth, imageHeight)

        val draw: Drawable = BitmapDrawable(resources, newImage)
        iVPreviewImage!!.setImageDrawable(draw)
    }
}

