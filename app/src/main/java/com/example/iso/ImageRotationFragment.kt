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
import android.widget.EditText
import android.widget.ImageView
import kotlinx.android.synthetic.main.fragment_image_rotation.*
import kotlin.math.PI
import kotlin.math.roundToInt

class ImageRotationFragment : Fragment(R.layout.fragment_image_rotation) {
    private lateinit var photoPlace: ImageView
    var rotatingAngle: EditText? = null
    var startRotationButton: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_image_rotation, container, false)
        rotatingAngle = rootView.findViewById(R.id.angle)
        photoPlace = rootView.findViewById(R.id.placeForImageRotation)
        startRotationButton = rootView.findViewById(R.id.startRotation)
        startRotationButton?.setOnClickListener { imageRotating() }
        val photo = (context as ThirdPageActivity).fromUriToBitmap()
        photoPlace.setImageBitmap(photo)
        return rootView
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
        val image = (placeForImageRotation?.drawable as BitmapDrawable).bitmap

        val oldWidth = image.width
        val oldHeight = image.height

        val oldPixels = IntArray(oldWidth * oldHeight)
        image.getPixels(oldPixels, 0, oldWidth, 0, 0, oldWidth, oldHeight)

        val sinAngle = kotlin.math.sin(angle.toDouble() * PI / 180.0)
        val tanHalfAngle = kotlin.math.tan(angle.toDouble() * PI / 360.0)


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
        placeForImageRotation!!.setImageDrawable(draw)
    }
}