package com.zs.audiofy.audios.directory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.compose.ContentPadding
import com.zs.compose.foundation.decorator.decorator
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.ContentAlpha
import com.zs.compose.theme.Icon
import com.zs.compose.theme.text.Label

@Composable
fun InfoChip(
    label: CharSequence,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    shape: Shape = Res.shape.circle,
    color: Color = AppTheme.colors.accent
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .decorator(
                backgroundColor = color.copy(ContentAlpha.indication),
                shape = shape,
            )
            .padding(vertical = 2.dp, horizontal = ContentPadding.small),
        content = {
            if (icon != null)
                Icon(
                    icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(13.dp)
                )
            Label(
                label,
                color = color,
                style = AppTheme.typography.label3,
                fontSize = 10.sp
            )
        }
    )

}