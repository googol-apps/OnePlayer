/*
 * Copyright 2025 Zakir Sheikh
 *
 * Created by Zakir Sheikh on 11-05-2025.
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


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.zs.audiofy.audios.directory.RouteAlbums
import com.zs.audiofy.audios.directory.RouteArtists
import com.zs.audiofy.audios.directory.RouteGenres
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.compose.ContentPadding
import com.zs.audiofy.common.compose.LocalNavController
import com.zs.audiofy.common.shapes.FolderShape
import com.zs.audiofy.common.vectorResource
import com.zs.audiofy.folders.RouteFolders
import com.zs.audiofy.playlists.members.RouteMembers
import com.zs.compose.foundation.textResource
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.Icon
import com.zs.compose.theme.Surface
import com.zs.compose.theme.text.Header
import com.zs.compose.theme.text.Label
import com.zs.core.playback.Remote

/**
 * Composable function to create a clickable shortcut with an icon and label.
 *
 * @param icon: The ImageVector representing the shortcut's icon.
 * @param label: The CharSequence representing the shortcut's label.
 * @param onAction: The action to perform when the shortcut is clicked.
 * @param modifier: Optional modifier to apply to the shortcut's layout.
 */
@Composable
private fun Shortcut(
    icon: ImageVector,
    label: CharSequence,
    onAction: () -> Unit,
    modifier: Modifier = Modifier,
) = Surface(
    shape = FolderShape(),
    color = Color.Transparent,
    border = BorderStroke(1.dp, AppTheme.colors.onBackground.copy(0.4f)),
    onClick = onAction,
    contentColor = AppTheme.colors.onBackground,
    modifier = modifier,
    content = {
        Column(
            modifier = Modifier.size(95.dp, 73.dp).padding(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            content = {
                Icon(// Icon at the top
                    imageVector = icon,
                    // Ensure a content description is provided elsewhere
                    contentDescription = null,
                )

                Label(// Label at the bottom
                    text = label,
                    style = AppTheme.typography.body3,
                )
            }
        )
    }
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Shortcuts(
    modifier: Modifier = Modifier,
) {
    // FlowRow to arrange shortcuts horizontally with spacing
    FlowRow(
        modifier = modifier/*.scaledLayout(1.3f)*/,
        horizontalArrangement = ContentPadding.SmallArrangement,
        verticalArrangement = ContentPadding.xSmallArrangement,
        content = {
            val navigator = LocalNavController.current

            // Shortcut for Genres navigation
            Shortcut(
                onAction = { navigator.navigate(RouteAlbums()) },
                icon = vectorResource(Res.drawable.ic_album_outline),
                label = textResource(id = Res.string.albums),
            )

            // Shortcut for Genres navigation
            Shortcut(
                onAction = { navigator.navigate(RouteGenres()) },
                icon = vectorResource(Res.drawable.ic_grain_filled),
                label = textResource(id = Res.string.genres),
            )

            // Shortcut for Artists navigation
            Shortcut(
                onAction = { navigator.navigate(RouteArtists()) },
                icon = vectorResource(Res.drawable.ic_artist),
                label = textResource(id = Res.string.artists),
            )

            // Favourites
            Shortcut(
                onAction = { navigator.navigate(RouteMembers(Remote.PLAYLIST_FAVOURITE)) },
                icon = vectorResource(Res.drawable.ic_folder_special_outline),
                label = textResource(id = Res.string.liked),
            )

            //
            Header(
                textResource(Res.string.folders),
                style = AppTheme.typography.label3,
                color = AppTheme.colors.accent,
                modifier = Modifier.padding(top = ContentPadding.small)
            )
            // Audio Folders
            Shortcut(
                onAction = { navigator.navigate(RouteFolders(true)) },
                icon = vectorResource(Res.drawable.ic_library_music_outline),
                label = "Audio",
            )

            Shortcut(
                onAction = { navigator.navigate(RouteFolders(false)) },
                icon = vectorResource(Res.drawable.ic_video_library),
                label = "Video",
            )
        }
    )
}