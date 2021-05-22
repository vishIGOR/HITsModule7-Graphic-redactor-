package com.example.iso

import android.graphics.Bitmap
import android.graphics.Color.argb
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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import kotlinx.android.synthetic.main.fragment_cube.*
import kotlin.math.*

class CubeFragment : Fragment(R.layout.fragment_cube) {

    private lateinit var cubePlace: ImageView
    var effect: EditText? = null
    var radius: EditText? = null
    var threshold: EditText? = null
    var doingSomethingWithCube: ImageView? = null
    var returnButton: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_cube, container, false)

        effect = rootView.findViewById(R.id.effect)
        radius = rootView.findViewById(R.id.radius)
        threshold = rootView.findViewById(R.id.threshold)
        doingSomethingWithCube = rootView.findViewById(R.id.doingSomethingWithCube)
        cubePlace = rootView.findViewById(R.id.placeForCube)

        doingSomethingWithCube?.setOnClickListener {
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
                            cube()
                        }
                    }
                }
            }
        }

        returnButton = rootView.findViewById(R.id.returnToSecondPageButtonCube)
        returnButton?.setOnClickListener{
            returnToMainMenu()
        }
        return rootView
    }

    private fun returnToMainMenu() {
        val returnFragment: Fragment = MainMenuFragment()
        val transForMenu: FragmentManager = this.fragmentManager!!

        transForMenu.commit {
            replace(R.id.fragments, returnFragment)
        }
    }

    private fun cube() {
        fun updateScreen(image: Bitmap) {
            image.eraseColor(argb(0, 0, 0, 0))
        }

        class Point {
            var x = 0
            var y = 0
            var z = 0

            fun setCoordinates(xCoordinate: Int, yCoordinate: Int, zCoordinate: Int) {
                x = xCoordinate
                y = yCoordinate
                z = zCoordinate
            }

            fun rotateByX(angle: Double) {
                val tempVar = (sin(angle) * y + cos(angle) * z).roundToInt()
                y = (cos(angle) * y - sin(angle) * z).roundToInt()
                z = tempVar
            }

            fun rotateByY(angle: Double) {
                val tempVar = (-1 * sin(angle) * x + cos(angle) * z).roundToInt()
                x = (cos(angle) * x + sin(angle) * z).roundToInt()
                z = tempVar
            }

            fun rotateByZ(angle: Double) {
                val tempVar = (sin(angle) * x + cos(angle) * y).roundToInt()
                x = (cos(angle) * x - sin(angle) * y).roundToInt()
                y = tempVar
            }


        }

        fun drawLine(
            firstPoint: Point,
            secondPoint: Point,
            imagePixels: IntArray,
            imageSize: Int
        ) {
            var x = firstPoint.x
            var y = firstPoint.y
            var dx = secondPoint.x - x
            var dy = secondPoint.y - y
            var signX: Int = sign(dx.toDouble()).roundToInt()
            var signY: Int = sign(dy.toDouble()).roundToInt()
            dx = abs(dx)
            dy = abs(dy)
            ///////////////

            if (dx == 0) {
                for (i in 0..dy) {
                    imagePixels[y * imageSize + x] = argb(255, 0, 0, 0)
                    y += signY
                }
                return
            }
            if (dy == 0) {
                for (i in 0..dx) {
                    imagePixels[y * imageSize + x] = argb(255, 0, 0, 0)
                    x += signX
                }
                return
            }
            if (dx == dy) {
                for (i in 0..dx) {
                    imagePixels[imageSize * y + x]
                    x += signX
                    y += signY
                }
                return
            }

            var brightness1 = 0
            var brightness2 = 0
            if (dx > dy) {
                if (firstPoint.x > secondPoint.x) {
                    y = secondPoint.y
                    x = secondPoint.x
                    signY *= -1
                }
                val grad = dy / ((dx).toDouble()) * signY
                imagePixels[firstPoint.y * imageSize + firstPoint.x] = argb(255, 0, 0, 0)
                imagePixels[secondPoint.y * imageSize + secondPoint.x] = argb(255, 0, 0, 0)
                var doubleY = y.toDouble() + grad

                for (i in 1 until dx) {
                    x += 1
                    brightness1 = (255 * (1 - (doubleY - doubleY.toInt()))).roundToInt()
                    brightness2 = (255 * (doubleY - doubleY.toInt())).roundToInt()
                    imagePixels[doubleY.toInt() * imageSize + x] = argb(brightness1, 0, 0, 0)
                    imagePixels[(doubleY.toInt() + 1) * imageSize + x] = argb(brightness2, 0, 0, 0)
                    doubleY += grad
                }

            } else {
                if (firstPoint.y > secondPoint.y) {
                    y = secondPoint.y
                    x = secondPoint.x
                    signX *= -1
                }
                val grad = dx / ((dy).toDouble()) * signX
                imagePixels[firstPoint.y * imageSize + firstPoint.x] = argb(255, 0, 0, 0)
                imagePixels[secondPoint.y * imageSize + secondPoint.x] = argb(255, 0, 0, 0)
                var doubleX = x.toDouble() + grad

                for (i in 1 until dy) {
                    y += 1
                    brightness1 = (255 * (1 - (doubleX - doubleX.toInt()))).roundToInt()
                    brightness2 = (255 * (doubleX - doubleX.toInt())).roundToInt()
                    imagePixels[y * imageSize + doubleX.toInt()] = argb(brightness1, 0, 0, 0)
                    imagePixels[y * imageSize + doubleX.toInt() + 1] = argb(brightness2, 0, 0, 0)
                    doubleX += grad
                }
            }
        }

        class Facet(
            val size: Int = 0,
            val topLeftP: Point = Point(),
            val topRightP: Point = Point(),
            val bottomLeftP: Point = Point(),
            val bottomRightP: Point = Point(),
            var imagePixels: IntArray = IntArray(1) { 0 }
        ) {
            var imageSize = (size * sqrt(3.0)).roundToInt() + 20

            var isVisible = false
            var number = 0

            fun paintRow(color: Int, direction: Int, bx: Int, by: Int, borderPoint: Point) {
                if (by < 3 || by >= imageSize - 3) {
                    return
                }
                var deltaX = bx
                val blankColor = 0
                var signX = 1
                if (bx > borderPoint.x) {
                    signX = -1
                }
                if (imagePixels[by * imageSize + bx] != blankColor) {
                    if (by == borderPoint.y) {
                        return
                    }

                    while (imagePixels[by * imageSize + deltaX] != blankColor) {
                        deltaX += signX
                    }
                    var tempBX = deltaX

                    if (signX == 1 && deltaX > borderPoint.x) {
                        return
                    }
                    if (signX == -1 && deltaX < borderPoint.x) {
                        return
                    }


                    while (imagePixels[by * imageSize + deltaX] == blankColor) {
                        imagePixels[by * imageSize + deltaX] = color
                        deltaX += signX
                        if ((signX == 1 && deltaX > topRightP.x && deltaX > topLeftP.x && deltaX > bottomRightP.x && deltaX > bottomLeftP.x)
                            || (signX == -1 && deltaX < topRightP.x && deltaX < topLeftP.x && deltaX < bottomRightP.x && deltaX < bottomLeftP.x)
                            || (deltaX < 0 || deltaX >= imageSize) || (deltaX + signX + by * imageSize < 0 || deltaX + signX + by * imageSize >= imageSize * imageSize)
                        ) {
                            for (i in 0..abs(deltaX - tempBX)) {
                                imagePixels[by * imageSize + tempBX + i * signX] = argb(0, 0, 0, 0)
                            }
                            return
                        }
                    }

                    paintRow(color, direction, tempBX, by + direction, borderPoint)
                    return
                }
                deltaX = bx + 1
                imagePixels[by * imageSize + bx] = color
                while (imagePixels[by * imageSize + deltaX] == blankColor && by * imageSize + deltaX in 1..imageSize * imageSize - 2) {
                    imagePixels[by * imageSize + deltaX] = color
                    deltaX++
                }
                deltaX = bx - 1
                while (imagePixels[by * imageSize + deltaX] == blankColor && by * imageSize + deltaX in 1..imageSize * imageSize - 2) {
                    imagePixels[by * imageSize + deltaX] = color
                    deltaX--
                }
                paintRow(color, direction, bx, by + direction, borderPoint)
            }

            fun setColor() {
                var color = 0
                if (number == 1) {
                    color = argb(255, 165, 250, 165)
                }
                if (number == 2) {
                    color = argb(255, 250, 180, 185)
                }
                if (number == 3) {
                    color = argb(255, 170, 215, 230)
                }
                if (number == 4) {
                    color = argb(255, 230, 230, 230)
                }
                if (number == 5) {
                    color = argb(255, 230, 210, 210)
                }
                if (number == 6) {
                    color = argb(255, 250, 170, 250)
                }
                var topPoint = topLeftP
                var bottomPoint = bottomRightP
                if (topRightP.y > topPoint.y) {
                    topPoint = topRightP
                    bottomPoint = bottomLeftP
                }
                if (bottomRightP.y > topPoint.y) {
                    topPoint = bottomRightP
                    bottomPoint = topLeftP
                }
                if (bottomLeftP.y > topPoint.y) {
                    topPoint = bottomLeftP
                    bottomPoint = topRightP
                }

                val middlePoint =
                    getMiddlePoint(getMiddlePoint(topLeftP, topRightP), getMiddlePoint(bottomLeftP, bottomRightP))
                paintRow(color, 1, middlePoint.x, middlePoint.y, topPoint)
                paintRow(color, -1, middlePoint.x, middlePoint.y - 1, bottomPoint)
            }

            fun getMiddlePoint(firstPoint: Point, secondPoint: Point): Point {
                var middlePoint = Point()
                middlePoint.x = (firstPoint.x + secondPoint.x) / 2
                middlePoint.y = (firstPoint.y + secondPoint.y) / 2
                return middlePoint
            }

            fun indentFromPoint(coefficient: Double, firstPoint: Point, secondPoint: Point): Point {
                var finishPoint = Point()
                finishPoint.x = firstPoint.x + (coefficient * (secondPoint.x - firstPoint.x)).roundToInt()
                finishPoint.y = firstPoint.y + (coefficient * (secondPoint.y - firstPoint.y)).roundToInt()
                return finishPoint
            }

            fun drawDigitOne(
                topLeftP: Point,
                topRightP: Point,
                bottomLeftP: Point,
                bottomRightP: Point
            ) {

                var topMiddleP = getMiddlePoint(topLeftP, topRightP)
                var bottomMiddleP = getMiddlePoint(bottomLeftP, bottomRightP)
                drawLine(topLeftP, topRightP, imagePixels, imageSize)
                drawLine(bottomLeftP, bottomRightP, imagePixels, imageSize)
                drawLine(topMiddleP, bottomMiddleP, imagePixels, imageSize)
            }

            fun drawDigitFive(
                topLeftP: Point,
                topRightP: Point,
                bottomLeftP: Point,
                bottomRightP: Point
            ) {
                var bottomMiddleP = getMiddlePoint(bottomLeftP, bottomRightP)
                drawLine(bottomMiddleP, topLeftP, imagePixels, imageSize)
                drawLine(bottomMiddleP, topRightP, imagePixels, imageSize)
            }

            fun drawNumber() {
                val widthOfDigit = 0.24
                val margin = 0.20
                val interdigitalSpace = 0.06

                if (number == 1) {
                    drawDigitOne(
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )
                }
                if (number == 2) {
                    val x: Double = (1 - widthOfDigit * 2 - interdigitalSpace) / 2
                    drawDigitOne(
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )

                    drawDigitOne(
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )
                }
                if (number == 3) {
                    val x: Double = (1 - 3 * widthOfDigit - 2 * interdigitalSpace) / 2
                    drawDigitOne(
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )

                    drawDigitOne(
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )

                    drawDigitOne(
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )
                }
                if (number == 4) {
                    val x: Double = (1 - widthOfDigit * 2 - interdigitalSpace) / 2
                    drawDigitOne(
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )

                    drawDigitFive(
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )
                }
                if (number == 5) {
                    drawDigitFive(
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            (1 - widthOfDigit) / 2,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )
                }
                if (number == 6) {
                    val x: Double = (1 - widthOfDigit * 2 - interdigitalSpace) / 2
                    drawDigitFive(
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )

                    drawDigitOne(
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, topLeftP, bottomLeftP),
                            indentFromPoint(margin, topRightP, bottomRightP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, topRightP, bottomRightP),
                            indentFromPoint(margin, topLeftP, bottomLeftP)
                        ),
                        indentFromPoint(
                            1 - x - widthOfDigit,
                            indentFromPoint(margin, bottomLeftP, topLeftP),
                            indentFromPoint(margin, bottomRightP, topRightP)
                        ),
                        indentFromPoint(
                            x,
                            indentFromPoint(margin, bottomRightP, topRightP),
                            indentFromPoint(margin, bottomLeftP, topLeftP)
                        )
                    )
                }
            }
        }

        class Cube(size: Int) {

            var size = size
            var arrayOfPoints: Array<Point> = Array(8) { Point() }


            fun updateCube() {
                var counter = 0
                for (i in 0..1) {
                    for (j in 0..1) {
                        for (k in 0..1) {
                            arrayOfPoints[counter].setCoordinates(
                                (size - 1) / 2,
                                (size - 1) / 2,
                                (size - 1) / 2
                            )
                            arrayOfPoints[counter].x *= (-1.0).pow(i).roundToInt()
                            arrayOfPoints[counter].y *= (-1.0).pow(j).roundToInt()
                            arrayOfPoints[counter].z *= (-1.0).pow(k).roundToInt()
                            ++counter
                        }
                    }
                }
            }

            fun rotateCubeByX(angle: Int) {
                var newAngle: Double = angle * PI / 180
                for (i in 0..7) {
                    arrayOfPoints[i].rotateByX(newAngle)
                }
            }

            fun rotateCubeByY(angle: Int) {
                var newAngle: Double = angle * PI / 180
                for (i in 0..7) {
                    arrayOfPoints[i].rotateByY(newAngle)
                }
            }

            fun rotateCubeByZ(angle: Int) {
                var newAngle: Double = angle * PI / 180
                for (i in 0..7) {
                    arrayOfPoints[i].rotateByZ(newAngle)
                }
            }

            fun makeProjection() {
                var imageSize = (size * sqrt(3.0)).roundToInt() + 20
                for (element in arrayOfPoints) {
                    element.x = element.x + (imageSize + 1) / 2
                    element.y = element.z + (imageSize + 1) / 2
                }
            }

            fun printCube(newImage: Bitmap, imageSize: Int): IntArray {
                var imagePixels = IntArray(imageSize * imageSize)
                newImage.getPixels(imagePixels, 0, imageSize, 0, 0, imageSize, imageSize)

                var minY = 100000
                var isPointVisible: Array<Boolean> = Array(8) { true }
                for (i in 0..7) {
                    if (arrayOfPoints[i].y < minY)
                        minY = arrayOfPoints[i].y
                }

                for (i in 0..7) {
                    if (arrayOfPoints[i].y == minY) {
                        isPointVisible[i] = false
                    }
                }

//                for (i in 0..7) {
//                    if (isPointVisible[i]) {
//                        imagePixels[(arrayOfPoints[i].z + (imageSize + 1) / 2) * imageSize + arrayOfPoints[i].x + (imageSize + 1) / 2] =
//                            argb(255, 0, 0, 0)
//                    }
//                }

                makeProjection()

                for (i in 0..7) {
                    if (isPointVisible[i]) {
                        if (i % 2 == 0) {
                            if (isPointVisible[i + 1])
                                drawLine(
                                    arrayOfPoints[i],
                                    arrayOfPoints[i + 1],
                                    imagePixels,
                                    imageSize
                                )
                        }

                        if (i == 6 || i == 7) {
                            if (isPointVisible[i - 2])
                                drawLine(
                                    arrayOfPoints[i],
                                    arrayOfPoints[i - 2],
                                    imagePixels,
                                    imageSize
                                )
                            if (isPointVisible[i - 4])
                                drawLine(
                                    arrayOfPoints[i],
                                    arrayOfPoints[i - 4],
                                    imagePixels,
                                    imageSize
                                )
                        }

                        if (i == 1 || i == 0) {
                            if (isPointVisible[i + 2])
                                drawLine(
                                    arrayOfPoints[i],
                                    arrayOfPoints[i + 2],
                                    imagePixels,
                                    imageSize
                                )
                            if (isPointVisible[i + 4])
                                drawLine(
                                    arrayOfPoints[i],
                                    arrayOfPoints[i + 4],
                                    imagePixels,
                                    imageSize
                                )
                        }
                    }
                }

                var cubeFacets: Array<Facet> = Array(6) { Facet(0, Point(), Point(), Point(), Point(), IntArray(1)) }
                cubeFacets[0] =
                    Facet(size, arrayOfPoints[2], arrayOfPoints[0], arrayOfPoints[3], arrayOfPoints[1], imagePixels)
                if (isPointVisible[2] && isPointVisible[0] && isPointVisible[3] && isPointVisible[1]) {
                    cubeFacets[0].isVisible = true
                    cubeFacets[0].number = 1
                }
                cubeFacets[1] =
                    Facet(size, arrayOfPoints[0], arrayOfPoints[4], arrayOfPoints[1], arrayOfPoints[5], imagePixels)
                if (isPointVisible[0] && isPointVisible[4] && isPointVisible[1] && isPointVisible[5]) {
                    cubeFacets[1].isVisible = true
                    cubeFacets[1].number = 2
                }
                cubeFacets[2] =
                    Facet(size, arrayOfPoints[4], arrayOfPoints[6], arrayOfPoints[5], arrayOfPoints[7], imagePixels)
                if (isPointVisible[4] && isPointVisible[6] && isPointVisible[5] && isPointVisible[7]) {
                    cubeFacets[2].isVisible = true
                    cubeFacets[2].number = 3
                }
                cubeFacets[3] =
                    Facet(size, arrayOfPoints[6], arrayOfPoints[2], arrayOfPoints[7], arrayOfPoints[3], imagePixels)
                if (isPointVisible[6] && isPointVisible[2] && isPointVisible[7] && isPointVisible[3]) {
                    cubeFacets[3].isVisible = true
                    cubeFacets[3].number = 4
                }
                cubeFacets[4] =
                    Facet(size, arrayOfPoints[6], arrayOfPoints[4], arrayOfPoints[2], arrayOfPoints[0], imagePixels)
                if (isPointVisible[6] && isPointVisible[4] && isPointVisible[2] && isPointVisible[0]) {
                    cubeFacets[4].isVisible = true
                    cubeFacets[4].number = 5
                }
                cubeFacets[5] =
                    Facet(size, arrayOfPoints[5], arrayOfPoints[7], arrayOfPoints[1], arrayOfPoints[3], imagePixels)
                if (isPointVisible[7] && isPointVisible[5] && isPointVisible[3] && isPointVisible[1]) {
                    cubeFacets[5].isVisible = true
                    cubeFacets[5].number = 6
                }

                for (facet in cubeFacets) {
                    if (facet.isVisible) {
                        facet.setColor()
                        facet.drawNumber()
                    }
                }

                return imagePixels
            }
        }

        var cubeSize = 199
        var imageSize = (cubeSize * sqrt(3.0)).roundToInt() + 20

        var newImage = Bitmap.createBitmap(imageSize, imageSize, Bitmap.Config.ARGB_8888)
        updateScreen(newImage)


        var cube = Cube(cubeSize)
        cube.updateCube()
        cube.rotateCubeByZ((-180..180).random())
        cube.rotateCubeByX((-180..180).random())
        cube.rotateCubeByY((-180..180).random())

        newImage.setPixels(
            cube.printCube(newImage, imageSize), 0, imageSize, 0, 0, imageSize, imageSize
        )

        val draw: Drawable = BitmapDrawable(resources, newImage)
        placeForCube!!.setImageDrawable(draw)
    }
}
