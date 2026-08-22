package com.zs.audiofy.common.shapes

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

@Immutable
@Stable
class PixelCircleShape(private val gridSize: Int = 12) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val stepX = size.width / gridSize
        val stepY = size.height / gridSize

        val radius = gridSize / 2f
        val center = radius

        // Arrays to store the left and right boundaries for each row
        val startX = IntArray(gridSize) { -1 }
        val endX = IntArray(gridSize) { -1 }

        // 1. Scan the grid to find the outer edges
        for (y in 0 until gridSize) {
            for (x in 0 until gridSize) {
                val dx = (x + 0.5f) - center
                val dy = (y + 0.5f) - center

                // If inside the radius, update the row's boundaries
                if ((dx * dx + dy * dy) <= (radius * radius)) {
                    if (startX[y] == -1) startX[y] = x
                    endX[y] = x + 1 // +1 to capture the right-side edge of the cell
                }
            }
        }

        // Find the first row that actually contains pixels (usually row 0)
        val firstValidY = startX.indexOfFirst { it != -1 }
        if (firstValidY == -1) return Outline.Generic(path) // Fallback for empty shapes

        // 2. Trace the left side (Top to Bottom)
        path.moveTo(startX[firstValidY] * stepX, firstValidY * stepY)

        for (y in firstValidY until gridSize) {
            if (startX[y] != -1) {
                path.lineTo(startX[y] * stepX, y * stepY)         // Horizontal step
                path.lineTo(startX[y] * stepX, (y + 1) * stepY)   // Vertical step down
            }
        }

        // 3. Trace the right side (Bottom to Top)
        for (y in gridSize - 1 downTo firstValidY) {
            if (endX[y] != -1) {
                path.lineTo(endX[y] * stepX, (y + 1) * stepY)     // Horizontal step to right edge
                path.lineTo(endX[y] * stepX, y * stepY)           // Vertical step up
            }
        }

        path.close()
        return Outline.Generic(path)
    }
}
