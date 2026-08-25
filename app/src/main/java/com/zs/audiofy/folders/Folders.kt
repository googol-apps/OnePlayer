/*
 * Copyright 2025 Zakir Sheikh
 *
 * Created by Zakir Sheikh on 14-05-2025.
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

package com.zs.audiofy.folders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zs.audiofy.audios.RouteAudios
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.compose.InfoChip
import com.zs.audiofy.common.compose.LocalNavController
import com.zs.audiofy.common.compose.directory.Directory
import com.zs.audiofy.common.fileSizeFormatted
import com.zs.audiofy.common.vectorResource
import com.zs.audiofy.videos.RouteVideos
import com.zs.compose.foundation.decorator.EdgeInsets
import com.zs.compose.foundation.decorator.decorator
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.ContentAlpha
import com.zs.compose.theme.LocalContentColor
import com.zs.compose.theme.Surface
import com.zs.compose.theme.text.Text
import com.zs.core.store.models.Folder
import com.zs.audiofy.common.compose.ContentPadding as CP


@Composable
private fun Folder(
    value: Folder,
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
                        value.artworkUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .decorator(
                                colors.background(3.dp),
                                shape = Res.shape.folder,
                                border = BorderStroke(Dp.Hairline, AppTheme.colors.onBackground),
                                edgeInsets = EdgeInsets(
                                    start = CP.xSmall,
                                    end = CP.xSmall,
                                    bottom = CP.medium
                                )
                            )
                            .aspectRatio(1.30f),
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
                    Text(
                        text = value.path,
                        style = AppTheme.typography.label3,
                        color = LocalContentColor.current.copy(ContentAlpha.medium),
                        maxLines = 1,
                        overflow = TextOverflow.StartEllipsis
                    )

                    // MoreInfo
                    Row(
                        modifier = Modifier.padding(top = CP.xSmall),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        content = {
                            val ctx = LocalContext.current
                            // Count
                            InfoChip(
                                icon = vectorResource(Res.drawable.ic_format_list_numbered),
                                label = "${value.count}"
                            )
                            // year
                            InfoChip(
                                icon = vectorResource(Res.drawable.ic_save),
                                label = ctx.fileSizeFormatted(value.size.toLong())
                            )
                        }
                    )
                }
            )
        }
    )
}

@Composable
fun Folders(viewState: FoldersViewState) {
    val navController = LocalNavController.current
    Directory(
        viewState,
        key = Folder::path,
        minSize = 110.dp,
        itemContent = {
            Folder(
                it,
                modifier = Modifier.animateItem(),
                onClick = {
                    navController.navigate(
                        when {
                            viewState.ofAudios -> RouteAudios(
                                RouteAudios.SOURCE_FOLDER,
                                it.path
                            )

                            else -> RouteVideos(it.path)
                        }
                    )
                }
            )
        }
    )
}