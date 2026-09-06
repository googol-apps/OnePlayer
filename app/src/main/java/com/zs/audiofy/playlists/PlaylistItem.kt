/*
 * Copyright 2024 Zakir Sheikh
 *
 * Created by 2024 on 20-10-2024.
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

package com.zs.audiofy.playlists

import android.text.format.DateUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.compose.InfoChip
import com.zs.audiofy.common.vectorResource
import com.zs.compose.foundation.decorator.EdgeInsets
import com.zs.compose.foundation.decorator.decorator
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.ContentAlpha
import com.zs.compose.theme.LocalContentColor
import com.zs.compose.theme.Surface
import com.zs.compose.theme.text.Label
import com.zs.compose.theme.text.Text
import com.zs.core.db.playlists.Playlist



/**
 * Represents an item of [Playlists] screen
 */
@Composable
fun PlaylistItem(
    value: Playlist,
    modifier: Modifier = Modifier,
    focused: Boolean = false
) {
    val colors = AppTheme.colors
    Surface(
        color = colors.background(1.dp),
        modifier = modifier,
        shape = AppTheme.shapes.medium,
        border = if (focused) BorderStroke(2.dp, LocalContentColor.current) else null,
        content = {
            Column(
                modifier = Modifier.padding(Res.space.small),
                content = {
                    // Album Art
                    AsyncImage(
                        value.artwork,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .decorator(
                                colors.background(20.dp),
                                shape = Res.shape.ghost,
                                //  border = BorderStroke(Dp.Hairline, AppTheme.colors.onBackground),
                                edgeInsets = EdgeInsets(bottom = Res.space.medium),
                                elevation = 4.dp
                            )
                            .aspectRatio(1.0f),
                    )

                    // Title
                    Text(
                        text = value.name,
                        style = AppTheme.typography.label1,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Path
                    Label(
                        text = DateUtils.getRelativeTimeSpanString(
                            value.dateModified, System.currentTimeMillis(),
                            DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL
                        ),
                        style = AppTheme.typography.label3,
                        fontWeight = FontWeight.Light,
                        color = LocalContentColor.current.copy(ContentAlpha.medium),
                    )

                    // MoreInfo
                    // Count
                    InfoChip(
                        icon = vectorResource(Res.drawable.ic_format_list_numbered),
                        label = "${value.count}",
                        modifier = Modifier.padding(top = Res.space.small)
                    )
                }
            )
        }
    )
}