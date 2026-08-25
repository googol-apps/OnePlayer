package com.zs.audiofy.common.shapes

import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection



object CrtScreenShape: Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val (w, h) = size
        val path = Path().apply {
            // Everything scales with the supplied Size
            val minDimension = minOf(w, h)

            // Corner radius
            val corner = minDimension * 0.25f

            // Amount of CRT glass bulge
            val bulgeX = w * 0.018f
            val bulgeY = h * 0.025f

            moveTo(corner, 0f)

            // Top edge — slightly bulged upward
            cubicTo(
                w * 0.35f, -bulgeY,
                w * 0.65f, -bulgeY,
                w - corner, 0f
            )

            // Top-right corner
            cubicTo(
                w - corner * 0.35f, 0f,
                w, corner * 0.35f,
                w, corner
            )

            // Right edge — slightly bulged outward
            cubicTo(
                w + bulgeX,
                h * 0.35f,
                w + bulgeX,
                h * 0.65f,
                w,
                h - corner
            )

            // Bottom-right corner
            cubicTo(
                w,
                h - corner * 0.35f,
                w - corner * 0.35f,
                h,
                w - corner,
                h
            )

            // Bottom edge
            cubicTo(
                w * 0.65f,
                h + bulgeY,
                w * 0.35f,
                h + bulgeY,
                corner,
                h
            )

            // Bottom-left corner
            cubicTo(
                corner * 0.35f,
                h,
                0f,
                h - corner * 0.35f,
                0f,
                h - corner
            )

            // Left edge
            cubicTo(
                -bulgeX,
                h * 0.65f,
                -bulgeX,
                h * 0.35f,
                0f,
                corner
            )

            // Top-left corner
            cubicTo(
                0f,
                corner * 0.35f,
                corner * 0.35f,
                0f,
                corner,
                0f
            )

            close()
        }
        return Outline.Generic(path)
    }
}