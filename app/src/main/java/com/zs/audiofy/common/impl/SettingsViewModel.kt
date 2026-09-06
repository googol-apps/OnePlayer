/*
 * Copyright 2025 Zakir Sheikh
 *
 * Created by Zakir Sheikh on 10-05-2025.
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

package com.zs.audiofy.common.impl

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.zs.audiofy.common.AppConfig
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.SystemFacade
import com.zs.audiofy.settings.SettingsViewState
import com.zs.compose.theme.snackbar.SnackbarResult
import com.zs.core.playback.Remote
import com.zs.preferences.Key
import kotlinx.coroutines.launch

class SettingsViewModel(val remote: Remote) : KoinViewModel(), SettingsViewState {
    override var trashCanEnabled: Boolean by mutableStateOf(Res.config.isTrashCanEnabled)
    override var preferCachedThumbnails: Boolean by mutableStateOf(Res.config.isLoadThumbnailFromCache)
    override var enabledBackgroundBlur: Boolean by mutableStateOf(Res.config.isBackgroundBlurEnabled)
    override var fontScale: Float by mutableFloatStateOf(Res.config.fontScale)
    override var minTrackLengthSecs: Int by mutableIntStateOf(Res.config.minTrackLengthSecs)
    override var inAppAudioEffectsEnabled: Boolean by mutableStateOf(Res.config.inAppAudioEffectsEnabled)
    override var gridItemSizeMultiplier: Float by mutableFloatStateOf(Res.config.gridItemSizeMultiplier)
    override var fabLongPressLaunchConsole: Boolean by mutableStateOf(Res.config.fabLongPressLaunchConsole)
    override var isSurfaceViewVideoRenderingPreferred: Boolean by mutableStateOf(Res.config.isSurfaceViewVideoRenderingPreferred)
    override var isFileGroupingEnabled: Boolean by mutableStateOf(Res.config.isFileGroupingEnabled)
    override var isSplashAnimWaitEnabled: Boolean by mutableStateOf(Res.config.isSplashAnimWaitEnabled)
    override var isWidgetToConsoleTransitionEnabled: Boolean by mutableStateOf(Res.config.isWidgetToConsoleTransitionEnabled)
    override var isLabsModeOn: Boolean by mutableStateOf(Res.config.isLabsModeOn)
    @set:JvmName("setBgPlaybackPolicy2")
    override var bgPlaybackPolicy: Int by mutableIntStateOf(Remote.BG_PLAYBACK_AUDIO_ONLY)

    init {
        viewModelScope.launch {
            bgPlaybackPolicy = remote.getBgPlaybackPolicy()
        }
    }

    override val save: Boolean by derivedStateOf {
        trashCanEnabled != Res.config.isTrashCanEnabled ||
                preferCachedThumbnails != Res.config.isLoadThumbnailFromCache ||
                enabledBackgroundBlur != Res.config.isBackgroundBlurEnabled ||
                fontScale != Res.config.fontScale ||
                minTrackLengthSecs != Res.config.minTrackLengthSecs ||
                inAppAudioEffectsEnabled != Res.config.inAppAudioEffectsEnabled ||
                gridItemSizeMultiplier != Res.config.gridItemSizeMultiplier ||
                fabLongPressLaunchConsole != Res.config.fabLongPressLaunchConsole ||
                isSurfaceViewVideoRenderingPreferred != Res.config.isSurfaceViewVideoRenderingPreferred ||
                isFileGroupingEnabled != Res.config.isFileGroupingEnabled ||
                isSplashAnimWaitEnabled != Res.config.isSplashAnimWaitEnabled ||
                isWidgetToConsoleTransitionEnabled != Res.config.isWidgetToConsoleTransitionEnabled ||
                isLabsModeOn != Res.config.isLabsModeOn
    }

    override fun commit(facade: SystemFacade) {
        viewModelScope.launch {
            // [CORE_SETTING_CHANGE] Update AppConfig with new settings values
            // This directly modifies the global AppConfig object, which is used throughout the application
            // to determine runtime behavior based on user preferences.
            val global = Res.config.isLoadThumbnailFromCache != preferCachedThumbnails ||
                    Res.config.isWidgetToConsoleTransitionEnabled != isWidgetToConsoleTransitionEnabled

            AppConfig.isLoadThumbnailFromCache = preferCachedThumbnails
            AppConfig.isBackgroundBlurEnabled = enabledBackgroundBlur
            AppConfig.isTrashCanEnabled = trashCanEnabled
            AppConfig.fontScale = fontScale
            AppConfig.minTrackLengthSecs = minTrackLengthSecs
            AppConfig.inAppAudioEffectsEnabled = inAppAudioEffectsEnabled
            AppConfig.gridItemSizeMultiplier = gridItemSizeMultiplier
            AppConfig.fabLongPressLaunchConsole = fabLongPressLaunchConsole
            AppConfig.isSurfaceViewVideoRenderingPreferred = isSurfaceViewVideoRenderingPreferred
            AppConfig.isFileGroupingEnabled = isFileGroupingEnabled
            AppConfig.isSplashAnimWaitEnabled = isSplashAnimWaitEnabled
            AppConfig.isWidgetToConsoleTransitionEnabled = isWidgetToConsoleTransitionEnabled
            AppConfig.isLabsModeOn = isLabsModeOn

            // [PERSISTENCE] Serialize and save the updated AppConfig to preferences
            // The `stringify()` method likely converts the AppConfig object into a JSON or similar string format
            // for storage. `preferences` is an abstraction over SharedPreferences or DataStore.
            preferences[Res.key.app_config] = Res.config.stringify()
            // trigger save
            val enabled = trashCanEnabled
            trashCanEnabled = !enabled
            trashCanEnabled = enabled
            // [USER_FEEDBACK] Inform the user that a restart is required for some changes to take effect
            // Display a snackbar with a "Restart" action.
            val result = showSnackbar(
                Res.string.msg_apply_changes_restart,
                Res.string.restart,
                icon = vectorResource(Res.drawable.ic_power_settings_new)
            )
            // [APP_LIFECYCLE] If the user confirms, trigger an application restart via the SystemFacade
            if (result == SnackbarResult.ActionPerformed)
                facade.restart(global)
        }
    }


    override fun setBgPlaybackPolicy(policy: Int) {
        bgPlaybackPolicy = policy
        viewModelScope.launch {
            remote.setBgPlaybackPolicy(policy)
        }
    }

    override fun discard() {
        viewModelScope.launch {
            val res = showSnackbar("Discard unsaved changes?", "Discard")
            if (res == SnackbarResult.ActionPerformed) {
                fontScale = Res.config.fontScale
                minTrackLengthSecs = Res.config.minTrackLengthSecs
                inAppAudioEffectsEnabled = Res.config.inAppAudioEffectsEnabled
                gridItemSizeMultiplier = Res.config.gridItemSizeMultiplier
                preferCachedThumbnails = Res.config.isLoadThumbnailFromCache
                enabledBackgroundBlur = Res.config.isBackgroundBlurEnabled
                trashCanEnabled = Res.config.isTrashCanEnabled
                fabLongPressLaunchConsole = Res.config.fabLongPressLaunchConsole
                isSurfaceViewVideoRenderingPreferred = Res.config.isSurfaceViewVideoRenderingPreferred
                isFileGroupingEnabled = Res.config.isFileGroupingEnabled
                isSplashAnimWaitEnabled = Res.config.isSplashAnimWaitEnabled
                isLabsModeOn = Res.config.isLabsModeOn
            }
        }
    }

    override fun <S, O> set(key: Key<S, O>, value: O) {
        preferences[key] = value
    }
}