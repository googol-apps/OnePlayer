package com.zs.audiofy.common.shapes

import androidx.compose.foundation.shape.GenericShape
import androidx.compose.ui.geometry.Size

val GhostishShape = getGhostishShape(50f)


/**
 * @param radius Defines the size of the bottom corner curves and the wave's height.
 */
fun getGhostishShape(radius: Float) = GenericShape { size, _ ->
    val w = size.width
    val h = size.height

    // Where the straight vertical sides stop to make room for the bottom corners
    val cornerStartY = h - radius

    // Where the center indent rests (slightly lower than the corner start for a wavy look)
    val centerDipY = h - (radius * 0.5f)

    // 1. Start at the middle-left edge (just below the dome)
    moveTo(0f, h * 0.4f)

    // 2. Draw the rounded top dome
    cubicTo(
        x1 = 0f, y1 = -h * 0.1f,
        x2 = w, y2 = -h * 0.1f,
        x3 = w, y3 = h * 0.4f
    )

    // 3. Right side straight down, STOPPING early to leave room for the corner
    lineTo(w, cornerStartY)

    // 4. Bottom Right Corner & Foot
    cubicTo(
        x1 = w, y1 = h,                   // Tangent is vertical: rounds the bottom-right corner perfectly
        x2 = w * 0.65f, y2 = centerDipY,  // Tangent is horizontal: approaches the center dip smoothly
        x3 = w * 0.5f, y3 = centerDipY    // The center dip anchor point
    )

    // 5. Bottom Left Corner & Foot
    cubicTo(
        x1 = w * 0.35f, y1 = centerDipY,  // Tangent is horizontal: leaves the center dip smoothly
        x2 = 0f, y2 = h,                  // Tangent is vertical: rounds the bottom-left corner
        x3 = 0f, y3 = cornerStartY        // Ends exactly where the left straight side will begin
    )

    // 6. Close the path (This automatically draws the left vertical line back up to the start)
    close()
}