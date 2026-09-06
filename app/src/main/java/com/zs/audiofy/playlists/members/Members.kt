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

package com.zs.audiofy.playlists.members

import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.zs.audiofy.common.Action
import com.zs.audiofy.common.PLAYLIST_ADD
import com.zs.audiofy.common.Res

import com.zs.audiofy.common.compose.LocalNavController
import com.zs.audiofy.common.compose.LottieAnimatedButton
import com.zs.audiofy.common.compose.LottieAnimatedIcon
import com.zs.audiofy.common.compose.OverflowMenu
import com.zs.audiofy.common.compose.directory.Files
import com.zs.audiofy.common.shapes.PixelCircleShape
import com.zs.audiofy.playlists.Playlists
import com.zs.compose.foundation.decorator.EdgeInsets
import com.zs.compose.foundation.decorator.decorator
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.BaseListItem
import com.zs.compose.theme.minimumInteractiveComponentSize
import com.zs.compose.theme.text.Label
import com.zs.core.db.playlists.Playlist.Track
import androidx.compose.foundation.combinedClickable as clickable


@Composable
private fun Track(
    value: Track,
    shape: androidx.compose.ui.graphics.Shape,
    actions: @Composable (() -> Unit),
    modifier: Modifier = Modifier
) {
    BaseListItem(
        trailing = actions,
        centerAlign = false,
        overline = { Label(text = value.subtitle) },
        heading = {
            Label(
                text = value.title,
                maxLines = 2,
                style = AppTheme.typography.body2,
                fontWeight = FontWeight.Bold
            )
        },
        modifier = Modifier.decorator(
            backgroundColor = AppTheme.colors.background(1.dp),
            shape = shape,
            edgeInsets = EdgeInsets(horizontal = Res.space.large)
        )
            .then(modifier),
        leading = {
            AsyncImage(
                model = value.artwork,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier
                    .decorator(
                        elevation = 4.dp,
                        shape = PixelCircleShape(8),
                        border = BorderStroke(2.dp, Color.White),
                        backgroundColor = AppTheme.colors.background(3.dp)
                    )
                    .size(58.dp),
            )
        },
    )
}

/**
 * Represents the state of the members screen.
 */
@Composable
fun Members(viewState: MembersViewState) {
    val selected = viewState.selected
    val favourites by viewState.favourites.collectAsState(emptySet())
    val navController = LocalNavController.current

    var showPlaylists by remember { mutableStateOf(false) }

    var focused: Track? by remember { mutableStateOf(null) }
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
        onTapAction = {
            when(it){
                Action.PLAYLIST_ADD -> { showPlaylists = true }
                else -> viewState.onPerformAction(it)
            }
        },
        key = Track::id,
        itemContent = { audio, pos ->
            Track(
                value = audio,
                shape = when (pos) {
                    0 -> Res.shape.section
                    1 -> Res.shape.section_top
                    2 -> Res.shape.section_middle
                    else -> Res.shape.section_bottom
                },
                modifier = Modifier
                    .animateItem()
                    .clickable(
                        onClick = {
                            if (viewState.isInSelectionMode)
                                return@clickable viewState.select(audio.id)
                            viewState.play(audio)
                        },
                        onLongClick = { viewState.select(audio.id) }
                    ),
                // actions
                actions = {
                    // show checkbox
                    if (viewState.isInSelectionMode)
                        return@Track LottieAnimatedIcon(
                            Res.raw.lt_checkbox,
                            animationSpec = AppTheme.motionScheme.slowSpatialSpec(),
                            atEnd = audio.id in selected, // if fav
                            contentDescription = null,
                            progressRange = 0.05f..0.30f,
                            scale = 1.6f,
                            tint = AppTheme.colors.accent,
                            modifier = Modifier
                                .minimumInteractiveComponentSize()
                                .padding(end = Res.space.small)
                        )

                    // show actions
                    Row {
                        // Heart
                        if (viewState.showFavButton)
                            LottieAnimatedButton(
                            Res.raw.lt_twitter_heart_filled_unfilled,
                            onClick = { viewState.toggleLiked(audio) },
                            animationSpec = tween(800),
                            atEnd = audio.uri.toString() in favourites, // if fav
                            contentDescription = null,
                            progressRange = 0.13f..1.0f,
                            scale = 3.5f,
                            tint = AppTheme.colors.accent,
                        )
                        // More

                        OverflowMenu(
                            collapsed = 0,
                            items = viewState.actions,
                            expanded = 5,
                            onItemClicked = {
                                when(it){
                                    Action.PLAYLIST_ADD -> {
                                        focused = audio
                                        showPlaylists = true
                                    }
                                    else -> viewState.onPerformAction(it, audio)
                                }
                            }
                        )
                    }
                }
            )
        }
    )
}