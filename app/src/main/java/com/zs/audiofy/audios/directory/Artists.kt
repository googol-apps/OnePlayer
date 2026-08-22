/*
 * Copyright 2025 Zakir Sheikh
 *
 * Created by Zakir Sheikh on 04-02-2025.
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

package com.zs.audiofy.audios.directory

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.zs.audiofy.audios.RouteAudios
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.Route
import com.zs.audiofy.common.compose.LocalNavController
import com.zs.audiofy.common.compose.directory.Directory
import com.zs.audiofy.common.compose.directory.DirectoryViewState
import com.zs.audiofy.common.shapes.PixelCircleShape
import com.zs.audiofy.common.vectorResource
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.ContentAlpha
import com.zs.compose.theme.Icon
import com.zs.compose.theme.Surface
import com.zs.compose.theme.text.Text
import com.zs.core.store.models.Audio.Artist
import com.zs.audiofy.common.compose.ContentPadding as CP

object RouteArtists : Route

@Composable
@NonRestartableComposable
fun Artists(viewState: DirectoryViewState<Artist>) {
    val navController = LocalNavController.current
    Directory(
        viewState,
        minSize = 100.dp,
        key = Artist::id,
        itemContent = {
            Artist(
                it,
                onClick = {
                    navController.navigate(
                        RouteAudios(
                            RouteAudios.SOURCE_ARTIST,
                            "${it.id}"
                        )
                    )
                },
                modifier = Modifier.animateItem()
            )
        }
    )
}

@Composable
private fun Artist(
    value: Artist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = AppTheme.colors
    Surface(
        color = colors.background(1.dp),
        modifier = modifier,
        onClick = onClick,
        shape = AppTheme.shapes.medium,
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CP.normal, vertical = CP.medium),
                content = {
                    // Top Icon in PixselShape
                    Surface(
                        modifier = Modifier.aspectRatio(1.0f),
                        shape = PixelCircleShape(12),
                        color = colors.background(20.dp),
                        border = BorderStroke(1.dp, colors.onBackground.copy(ContentAlpha.indication))
                    ) {
                        Icon(
                            vectorResource(Res.drawable.ic_artist),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    // Label aligned to the left with padding and styling
                    Text(
                        text = value.name,
                        modifier = Modifier.padding(top = CP.medium, bottom = CP.small), // Add horizontal padding
                        style = AppTheme.typography.label2,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2, // Allow at most 2 lines for label
                        minLines = 2,
                        overflow = TextOverflow.MiddleEllipsis
                    )

                    // MoreInfo
                    InfoChip(
                        icon =  vectorResource(Res.drawable.ic_music_note),
                        label = "${value.tracks}"
                    )
                }
            )
        }
    )
}

