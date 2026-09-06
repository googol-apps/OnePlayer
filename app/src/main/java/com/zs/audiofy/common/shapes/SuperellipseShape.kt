package com.zs.audiofy.common.shapes

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sign
import kotlin.math.sin


/**
 * A [Shape] implementation that represents a superellipse (also known as a Lamé curve).
 *
 * This shape provides a smooth transition between a circle/ellipse and a rectangle,
 * which is commonly used in modern UI design for "squircle" icons and containers.
 *
 * @param squareness A value between 0.0 and 1.0 that determines the shape's curvature.
 * - 0.0 results in a perfect circle or ellipse.
 */
private class Superellipse(squareness: Float, private val steps: Int) : Shape {

    private val squareness = squareness.coerceIn(0f, 1f)

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {

        // Exact rectangle at the end of the animation.
        if (squareness >= 1f) {
            return Outline.Rectangle(
                Rect(
                    0f,
                    0f,
                    size.width,
                    size.height,
                )
            )
        }

        val a = size.width / 2f
        val b = size.height / 2f

        val cx = a
        val cy = b

        /*
         * Continuous superellipse exponent.
         *
         * s = 0     -> n = 2
         * s = 0.5   -> n = 4
         * s = 0.9   -> n = 20
         * s -> 1    -> n -> infinity
         */
        val exponent = 2f / (1f - squareness)

        /*
         * x = a * sign(cos(t)) * |cos(t)|^(2/n)
         * y = b * sign(sin(t)) * |sin(t)|^(2/n)
         */
        val power = 2f / exponent

        val path = Path()

        for (i in 0..steps) {
            val theta = 2.0 * PI * i / steps

            val cosTheta = cos(theta).toFloat()
            val sinTheta = sin(theta).toFloat()

            val x =
                cx +
                        a *
                        sign(cosTheta) *
                        abs(cosTheta).pow(power)

            val y =
                cy +
                        b *
                        sign(sinTheta) *
                        abs(sinTheta).pow(power)

            if (i == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        path.close()

        return Outline.Generic(path)
    }
}

@Stable
fun SuperellipseShape(squareness: Float, steps: Int = 96): Shape =
    Superellipse(squareness, steps)

