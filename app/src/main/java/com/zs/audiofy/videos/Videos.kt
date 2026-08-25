/*
 * Copyright 2025 Zakir Sheikh
 *
 * Created by Zakir Sheikh on 13-05-2025.
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

package com.zs.audiofy.videos

import android.app.Activity
import android.text.format.DateUtils
import android.text.format.Formatter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.zs.audiofy.common.Action
import com.zs.audiofy.common.INFO
import com.zs.audiofy.common.PLAYLIST_ADD
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.compose.ContentPadding
import com.zs.audiofy.common.compose.InfoChip
import com.zs.audiofy.common.compose.LocalNavController
import com.zs.audiofy.common.compose.LocalSystemFacade
import com.zs.audiofy.common.compose.LottieAnimatedButton
import com.zs.audiofy.common.compose.LottieAnimatedIcon
import com.zs.audiofy.common.compose.OverflowMenu
import com.zs.audiofy.common.compose.directory.Files
import com.zs.audiofy.common.shapes.SuperellipseShape
import com.zs.audiofy.common.vectorResource
import com.zs.audiofy.console.RouteConsole
import com.zs.audiofy.playlists.Playlists
import com.zs.audiofy.properties.RouteProperties
import com.zs.compose.foundation.SignalWhite
import com.zs.compose.foundation.decorator.EdgeInsets
import com.zs.compose.foundation.decorator.decorator
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.BaseListItem
import com.zs.compose.theme.ContentAlpha
import com.zs.compose.theme.LocalContentColor
import com.zs.compose.theme.minimumInteractiveComponentSize
import com.zs.compose.theme.text.Label
import com.zs.compose.theme.text.Text
import com.zs.core.common.PathUtils
import com.zs.core.store.models.Video
import dev.chrisbanes.haze.rememberHazeState
import androidx.compose.foundation.combinedClickable as clickable
import com.zs.audiofy.common.compose.ContentPadding as CP

private const val TAG = "Videos"

/**
 * Represents the [Video] list item.
 */
@Composable
private fun Video(
    value: Video,
    shape: Shape,
    actions: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    BaseListItem(
        trailing = actions,
        spacing = CP.small,
        centerAlign = false,
        padding = PaddingValues(start = CP.medium, end = 0.dp, top = CP.medium, bottom = CP.medium),
                // Title
        overline = {
            Label(
                value.name,
                fontWeight = FontWeight.SemiBold,
                color = AppTheme.colors.onBackground,
                maxLines = 2,
                style = AppTheme.typography.body2
            )
        },
        // Path
        heading = {
            Text(
                PathUtils.parent(value.path),
                overflow = TextOverflow.StartEllipsis,
                maxLines = 1,
                style = AppTheme.typography.label2,
                modifier = Modifier.padding(vertical = CP.xSmall),
                color = LocalContentColor.current.copy(ContentAlpha.medium)
            )
        },
        // Properties
        subheading = {
            val ctx = LocalContext.current
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                InfoChip(
                    label = "${Formatter.formatShortFileSize(ctx, value.size)} ",
                    icon = vectorResource(Res.drawable.ic_save),
                    shape = AppTheme.shapes.xSmall
                )
                InfoChip(
                    label = stringResource(Res.string.pixels_d, value.height),
                    icon = vectorResource(Res.drawable.ic_fit_screen),
                    shape = AppTheme.shapes.xSmall
                )
            }
        },
        // Thumbnail
        leading = {
            Box(
                modifier = Modifier
                    .decorator(
                        elevation = 4.dp,
                        shape = Res.shape.crt_screen,
                        border = BorderStroke(1.dp, Color.White),
                        backgroundColor = AppTheme.colors.background(3.dp)
                    ),
                content = {
                    AsyncImage(
                        model = value.contentUri,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = Modifier.size(90.dp, 58.dp)
                    )

                    // Duration
                    Label(
                        " ${DateUtils.formatElapsedTime(value.duration / 1000)} ",
                        modifier = Modifier
                            .padding(end = CP.xSmall, bottom = CP.xSmall)
                            .background(Color.Black.copy(0.36f), AppTheme.shapes.small)
                            .align(Alignment.BottomEnd),
                        color = Color.SignalWhite,
                        style = AppTheme.typography.label3,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            )
        },
        modifier = Modifier.decorator(
            backgroundColor = AppTheme.colors.background(1.dp),
            shape = shape,
            edgeInsets = EdgeInsets(horizontal = ContentPadding.normal)
        )
            .then(modifier),
    )
}

@Composable
fun Videos(viewState: VideosViewState) {
    val selected = viewState.selected
    val favourites by viewState.favourites.collectAsState(emptySet())
    val facade = LocalSystemFacade.current
    val navController = LocalNavController.current
    val surface = rememberHazeState()

    var showPlaylists by remember { mutableStateOf(false) }

    var focused: Video? by remember { mutableStateOf(null) }
    Playlists(
        showPlaylists,
        viewState.playlists,
        onSelect = { playlist ->
            if (playlist != null) {
                viewState.addToPlaylist(playlist.id, focused)
                focused = null
            }
            showPlaylists = false
        }
    )

    Files(
        viewState,
        surface = surface,
        onTapAction = {
            when(it){
                Action.PLAYLIST_ADD -> showPlaylists = true
                else -> viewState.onPerformAction(it, facade as Activity)
            }
        },
        key = Video::id,
        itemContent = { video, pos ->
            Video(
                value = video,
                shape = when (pos) {
                    0 -> Res.shape.section
                    1 -> Res.shape.section_first_item
                    2 -> Res.shape.section_middle_item
                    else -> Res.shape.section_last_item
                },
                modifier = Modifier
                    .animateItem()
                    .clickable(
                        onClick = {
                            if (viewState.isInSelectionMode)
                                viewState.select(video.id)
                            else {
                                viewState.play(video)
                                navController.navigate(RouteConsole())
                            }
                        },
                        onLongClick = { viewState.select(video.id) }
                    ),
                // actions
                actions = {
                    // show checkbox
                    if (viewState.isInSelectionMode)
                        return@Video LottieAnimatedIcon(
                            Res.raw.lt_checkbox,
                            animationSpec = AppTheme.motionScheme.slowSpatialSpec(),
                            atEnd = video.id in selected, // if fav
                            contentDescription = null,
                            progressRange = 0.05f..0.30f,
                            scale = 1.6f,
                            tint = AppTheme.colors.accent,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .padding(end = ContentPadding.small)
                        )

                    // show actions
                    Row {
                        // Heart
                        LottieAnimatedButton(
                            Res.raw.lt_twitter_heart_filled_unfilled,
                            onClick = { viewState.toggleLiked(video) },
                            animationSpec = tween(800),
                            atEnd = video.contentUri.toString() in favourites, // if fav
                            contentDescription = null,
                            progressRange = 0.13f..1.0f,
                            scale = 3.5f,
                            tint = AppTheme.colors.accent,
                        )
                        // More
                        val onPerformAction = {action: Action ->
                            when (action) {
                                Action.INFO -> navController.navigate(RouteProperties(video.path))
                                Action.PLAYLIST_ADD -> {
                                    showPlaylists = true
                                    focused = video
                                }
                                else -> viewState.onPerformAction(action, facade as Activity, video)
                            }
                        }

                        OverflowMenu(
                            collapsed = 0,
                            items = viewState.actions,
                            expanded = 5,
                            onItemClicked = onPerformAction
                        )
                    }
                }
            )
        }
    )
}