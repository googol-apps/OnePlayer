package com.zs.audiofy.settings

import androidx.compose.runtime.Stable
import com.zs.audiofy.common.SystemFacade
import com.zs.preferences.Key


/**
 * Represents the state of the Settings screen.
 */
@Stable
interface SettingsViewState {

    val save: Boolean

    var trashCanEnabled: Boolean
    var preferCachedThumbnails: Boolean
    var enabledBackgroundBlur: Boolean
    var fontScale: Float
    var minTrackLengthSecs: Int
    var inAppAudioEffectsEnabled: Boolean
    var gridItemSizeMultiplier: Float
    var fabLongPressLaunchConsole: Boolean
    var isSurfaceViewVideoRenderingPreferred: Boolean
    var isFileGroupingEnabled: Boolean
    var isSplashAnimWaitEnabled: Boolean
    var isWidgetToConsoleTransitionEnabled: Boolean
    var isLabsModeOn: Boolean
    val bgPlaybackPolicy: Int

    /**
     * Commits [com.zs.audiofy.common.AppConfig] to memory.
     */
    fun commit(facade: SystemFacade)

    fun setBgPlaybackPolicy(policy: Int)

    fun discard()

    /** Sets the value of the given [key] to [value]. */
    fun <S, O> set(key: Key<S, O>, value: O)
}