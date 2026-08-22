package com.zs.audiofy.common.shapes


import androidx.annotation.FloatRange
import androidx.compose.runtime.Immutable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.cos
import kotlin.math.sin

// Accepts 0.0f to 1.0f
@Immutable
class SunnyShape(@FloatRange(0.0, 1.0) private val radius: Float = 0.3f) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val path = Path().apply {
            val centerX = size.width / 2f
            val centerY = size.height / 2f
            val outerRadius = size.width / 2f
            val innerRadius = outerRadius * 0.75f

            // Ensure the value stays safely between 0 and 1
            val cornerRounding = radius.coerceIn(0f, 1f)

            val numPoints = 8
            val angleStep = (2 * Math.PI) / numPoints
            val halfStep = angleStep / 2.0

            val startAngle = -Math.PI / 2 - halfStep
            moveTo(
                x = centerX + (innerRadius * cos(startAngle)).toFloat(),
                y = centerY + (innerRadius * sin(startAngle)).toFloat()
            )

            for (i in 0 until numPoints) {
                val outerAngle = i * angleStep - Math.PI / 2
                val nextInnerAngle = outerAngle + halfStep
                val prevInnerAngle = outerAngle - halfStep

                val outerX = centerX + (outerRadius * cos(outerAngle)).toFloat()
                val outerY = centerY + (outerRadius * sin(outerAngle)).toFloat()

                val prevInnerX = centerX + (innerRadius * cos(prevInnerAngle)).toFloat()
                val prevInnerY = centerY + (innerRadius * sin(prevInnerAngle)).toFloat()

                val nextInnerX = centerX + (innerRadius * cos(nextInnerAngle)).toFloat()
                val nextInnerY = centerY + (innerRadius * sin(nextInnerAngle)).toFloat()

                // Use the radiusPct (cornerRounding) to calculate where curves begin and end
                val startCurveX = prevInnerX + (outerX - prevInnerX) * (1 - cornerRounding)
                val startCurveY = prevInnerY + (outerY - prevInnerY) * (1 - cornerRounding)

                val endCurveX = nextInnerX + (outerX - nextInnerX) * (1 - cornerRounding)
                val endCurveY = nextInnerY + (outerY - nextInnerY) * (1 - cornerRounding)

                lineTo(startCurveX, startCurveY)

                quadraticBezierTo(
                    x1 = outerX, y1 = outerY,
                    x2 = endCurveX, y2 = endCurveY
                )

                lineTo(nextInnerX, nextInnerY)
            }
            close()
        }

        return Outline.Generic(path)
    }
}