package com.zs.audiofy.common.shapes

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object GhostShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val (w, h) = size
        val path  =Path().apply {
            // Start at the top-center
            moveTo(
                x = w * 0.50f,
                y = 0f
            )

            // Left half of the dome
            cubicTo(
                w * 0.23f, h * 0.00f,
                w * 0.00f, h * 0.20f,
                w * 0.00f, h * 0.46f
            )

            // Left vertical side
            lineTo(
                w * 0.00f,
                h * 0.78f
            )

            // Bottom-left rounded corner.
            // This is the START of the continuous bottom wave.
            cubicTo(
                w * 0.00f, h * 0.91f,
                w * 0.08f, h * 1.00f,
                w * 0.21f, h * 1.00f
            )

            // -------------------------------------------------
            // CONTINUOUS BOTTOM WAVE
            // -------------------------------------------------

            // Left low section -> center dip/peak
            cubicTo(
                w * 0.32f, h * 1.00f,
                w * 0.36f, h * 0.84f,
                w * 0.50f, h * 0.84f
            )

            // Center -> right low section
            cubicTo(
                w * 0.64f, h * 0.84f,
                w * 0.68f, h * 1.00f,
                w * 0.79f, h * 1.00f
            )

            // -------------------------------------------------
            // Bottom-right rounded corner.
            // This is the END of the continuous wave.
            // -------------------------------------------------
            cubicTo(
                w * 0.92f, h * 1.00f,
                w * 1.00f, h * 0.91f,
                w * 1.00f, h * 0.78f
            )

            // Right vertical side
            lineTo(
                w,
                h * 0.46f
            )

            // Right half of the dome
            cubicTo(
                w,
                h * 0.20f,
                w * 0.77f,
                0f,
                w * 0.50f,
                0f
            )

            close()
        }
        return Outline.Generic(path)
    }
}




