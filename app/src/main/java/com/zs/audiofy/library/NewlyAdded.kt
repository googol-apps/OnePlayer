/*
 * Copyright 2024 Zakir Sheikh
 *
 * Created by Zakir Sheikh on 07-07-2024.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zs.audiofy.library

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.toBitmap
import com.zs.audiofy.audios.RouteAudios
import com.zs.audiofy.audios.directory.RouteAlbums
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.compose.LocalNavController
import com.zs.audiofy.common.compose.emit
import com.zs.audiofy.common.shapes.CompactDisk
import com.zs.audiofy.common.vectorResource
import com.zs.compose.foundation.ImageBrush
import com.zs.compose.foundation.SignalWhite
import com.zs.compose.foundation.decorator.EdgeInsets
import com.zs.compose.foundation.decorator.decorator
import com.zs.compose.foundation.foreground
import com.zs.compose.foundation.visualEffect
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.Icon
import com.zs.compose.theme.Surface
import com.zs.compose.theme.text.Label
import com.zs.core.common.WallpaperAccentColor
import com.zs.core.store.MediaProvider
import com.zs.core.store.models.Audio
import com.zs.core.store.models.Audio.Album
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.saveable.rememberSaveable as savable
import com.zs.audiofy.common.compose.ContentPadding as CP

private val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color? {
        return Color(value)
    }

    override fun SaverScope.save(value: Color): Int? {
        return value.toArgb()
    }
}

/**
 * Composable function to create a clickable newly added item with image, label, and play icon.
 *
 * @param label: The CharSequence representing the item's label.
 * @param onClick: The action to perform when the item is clicked.
 * @param modifier: Optional modifier to apply to the item's layout.
 * @param imageUri: Optional Uri for the item's image.
 * @param alignment: The alignment of the image within the item (default: Center).
 */
@Composable
private fun NewlyAddedItem(
    value: Album,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = AppTheme.colors
    Surface(
        color = colors.background(2.dp),
        shape = AppTheme.shapes.large,
        onClick = onClick,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(horizontal = CP.normal, vertical = CP.medium).size(width = 110.dp, 190.dp)) {
            AsyncImage(
                MediaProvider.buildAlbumArtUri(value.id),
                contentDescription = null,
                modifier = Modifier
                    .decorator(
                        colors.background(3.dp),
                        shape = CompactDisk,
                        border = BorderStroke(1.dp, AppTheme.colors.onBackground)
                    )
                    .aspectRatio(1.0f),
            )

            // Label aligned to the left with padding and styling
            Label(
                text = value.title,
                modifier = Modifier
                    .padding(top = CP.medium), // Add horizontal padding
                style = AppTheme.typography.label1,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2, // Allow at most 2 lines for label
            )

            // Label aligned to the left with padding and styling
            Label(
                text = value.artist,
                style = AppTheme.typography.label3,
                fontWeight = FontWeight.Light
            )
        }
    }
}

/**
 * A Composable function that displays a list of newly added items.
 *
 * @param state The state of the library.
 * @param modifier The modifier to be applied to the list.
 * @param contentPadding The padding to be applied to the list.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewlyAdded(
    state: LibraryViewState,
    modifier: Modifier = Modifier
) {
    // Collect newly added items from the Library state
    val audios by state.newlyAdded.collectAsState()
    val navController = LocalNavController.current
    // Display the list with loading, empty, and content states
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(CP.normal),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
        content = {
            // Ensure first item is visible by adding a spacer at the front
            item(contentType = "library_list_spacer") {
                Spacer(modifier = Modifier)
            }

            val data = emit(false, audios) ?: return@LazyRow
            items(data, key = Album::id) { item ->
                // Create newly added item with parallax-adjusted image alignment
                NewlyAddedItem(
                    value = item,
                    onClick = { navController.navigate(RouteAudios(RouteAudios.SOURCE_ALBUM, "${item.id}")) },
                    modifier = Modifier.animateItem(),
                )
            }
        }
    )
}