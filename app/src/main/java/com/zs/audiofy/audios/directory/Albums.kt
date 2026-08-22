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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zs.audiofy.audios.RouteAudios
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.Route
import com.zs.audiofy.common.compose.LocalNavController
import com.zs.audiofy.common.compose.directory.Directory
import com.zs.audiofy.common.compose.directory.DirectoryViewState
import com.zs.audiofy.common.vectorResource
import com.zs.compose.foundation.decorator.decorator
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.ContentAlpha
import com.zs.compose.theme.LocalContentColor
import com.zs.compose.theme.Surface
import com.zs.compose.theme.text.Label
import com.zs.compose.theme.text.Text
import com.zs.core.store.MediaProvider
import com.zs.core.store.models.Audio.Album
import com.zs.audiofy.common.compose.ContentPadding as CP

object RouteAlbums : Route

@Composable
private fun Album(
    value: Album,
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
                    .padding(CP.small),
                content = {
                    // Album Art
                    AsyncImage(
                        MediaProvider.buildAlbumArtUri(value.id),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(bottom = CP.medium)
                            .decorator(
                                colors.background(3.dp),
                                shape = Res.shape.compact_disk,
                                border = BorderStroke(1.dp, AppTheme.colors.onBackground)
                            )
                            .aspectRatio(1.0f),
                    )

                    // Title
                    Text(
                        text = value.title,
                        style = AppTheme.typography.label1,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2, // Allow at most 2 lines for label
                        minLines = 2,
                        overflow = TextOverflow.MiddleEllipsis
                    )

                    // Artist
                    Label(
                        text = value.artist,
                        style = AppTheme.typography.label3,
                        color = LocalContentColor.current.copy(ContentAlpha.medium)
                    )

                    // MoreInfo
                    Row(
                        modifier = Modifier.padding(top = CP.xSmall),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        content = {
                            // Count
                            InfoChip(
                                icon = vectorResource(Res.drawable.ic_music_note),
                                label = "${value.cardinality}"
                            )
                            // year
                            InfoChip(
                                icon = vectorResource(Res.drawable.ic_calendar_month),
                                label = "${value.firstYear}"
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
@NonRestartableComposable
fun Albums(viewState: DirectoryViewState<Album>) {
    val navController = LocalNavController.current
    Directory(
        viewState,
        key = Album::id,
        minSize = 100.dp,
        itemContent = {
            Album(
                it,
                onClick = {
                    navController.navigate(
                        RouteAudios(
                            RouteAudios.SOURCE_ALBUM,
                            "${it.id}"
                        )
                    )
                },
                modifier = Modifier.animateItem()
            )
        }
    )
}