package com.zs.audiofy.console

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.takeOrElse
import androidx.compose.ui.viewinterop.AndroidView
import com.zs.compose.theme.text.LocalTextStyle
import com.zs.core.playback.SubtitleView

@Composable
fun Cues(
    provider: () -> List<Any>?,
    modifier: Modifier = Modifier
) {
    // for now set text stize to local text style. in the future make it dynamic
    val style = LocalTextStyle.current
    AndroidView(
        modifier = modifier,
        factory = {
            val wrapper = SubtitleView(it)
            wrapper.raw
        },
        update = {view ->
           val wrapper =  SubtitleView(view)
            val cues = provider()
            wrapper.setCues(cues)
            wrapper.setTextSize(style.fontSize.takeOrElse { 15.sp }.value)
        }
    )
}