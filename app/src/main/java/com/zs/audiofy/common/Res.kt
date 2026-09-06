@file:Suppress("ClassName", "ConstPropertyName", "UseKtx")

package com.zs.audiofy.common

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.zs.audiofy.common.Res.app.PKG_MARKET_ID
import com.zs.audiofy.common.Res.app.color_accent_dark
import com.zs.audiofy.common.Res.app.color_accent_light
import com.zs.audiofy.common.Res.app.intent_github
import com.zs.audiofy.common.Res.app.intent_github_issues
import com.zs.audiofy.common.Res.app.intent_join_beta
import com.zs.audiofy.common.Res.app.intent_privacy_policy
import com.zs.audiofy.common.Res.app.intent_share_app
import com.zs.audiofy.common.Res.app.intent_telegram
import com.zs.audiofy.common.Res.app.intent_translate
import com.zs.audiofy.common.Res.app.market_url_prefix
import com.zs.audiofy.common.Res.app.market_web_url_prefix
import com.zs.audiofy.common.Res.key.app_config
import com.zs.audiofy.common.Res.key.color_accent_dark
import com.zs.audiofy.common.Res.key.color_accent_light
import com.zs.audiofy.common.Res.key.colorization_strategy
import com.zs.audiofy.common.Res.key.glance_widget
import com.zs.audiofy.common.Res.key.launch_counter
import com.zs.audiofy.common.Res.key.night_mode
import com.zs.audiofy.common.Res.key.transparent_system_bars
import com.zs.audiofy.common.Res.key.use_accent_in_nav_bar
import com.zs.audiofy.common.Res.shape.circle
import com.zs.audiofy.common.Res.shape.compact_disk
import com.zs.audiofy.common.Res.shape.crt_screen
import com.zs.audiofy.common.Res.shape.folder
import com.zs.audiofy.common.Res.shape.ghost
import com.zs.audiofy.common.Res.shape.rectangle
import com.zs.audiofy.common.Res.shape.section
import com.zs.audiofy.common.Res.shape.section_bottom
import com.zs.audiofy.common.Res.shape.section_middle
import com.zs.audiofy.common.Res.shape.section_top
import com.zs.audiofy.common.Res.shape.squircle
import com.zs.audiofy.common.Res.shape.sunny
import com.zs.audiofy.common.Res.space.large
import com.zs.audiofy.common.Res.space.medium
import com.zs.audiofy.common.Res.space.normal
import com.zs.audiofy.common.Res.space.small
import com.zs.audiofy.common.Res.space.x_large
import com.zs.audiofy.common.Res.space.x_small
import com.zs.audiofy.common.shapes.CompactDisk
import com.zs.audiofy.common.shapes.CrtScreenShape
import com.zs.audiofy.common.shapes.FolderShape
import com.zs.audiofy.common.shapes.GhostShape
import com.zs.audiofy.common.shapes.SunnyShape
import com.zs.audiofy.common.shapes.SuperellipseShape
import com.zs.core.billing.Paymaster
import com.zs.preferences.IntSaver
import com.zs.preferences.booleanPreferenceKey
import com.zs.preferences.intPreferenceKey
import com.zs.preferences.stringPreferenceKey

/**
 * A custom [IntSaver] implementation for persisting [Color] objects.
 * Saves the color as an ARGB integer and restores it back to a [Color] instance.
 */
private val ColorSaver = object : IntSaver<Color> {
    override fun restore(value: Int): Color = Color(value)
    override fun save(value: Color): Int = value.toArgb()
}

/**
 * A generic [IntSaver] implementation for any [Enum] type.
 *
 * This saver persists enum values by storing their [Enum.name] as a String,
 * and restores them by looking up the corresponding enum constant.
 *
 * Usage:
 * ```
 * val saver = enumSaver<MyEnum>()
 * val state = rememberSaveable(stateSaver = saver) { mutableStateOf(MyEnum.FIRST) }
 * ```
 *
 * This avoids writing custom savers for each enum class.
 */
@Suppress("FunctionName")
private inline fun <reified T : Enum<T>> OrdinalEnumSaver(): IntSaver<T> = object : IntSaver<T> {
    // Save enum as its name string
    override fun save(value: T): Int = value.ordinal

    // Restore enum constant by ordinal lookup
    override fun restore(value: Int): T =
        enumValues<T>()[value]
}

/**
 * The standard [GoogleFont.Provider] utilized to asynchronously download
 * and cache fonts securely via Google Play Services.
 */
private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = Res.array.com_google_android_gms_fonts_certs
)

/**
 * Creates a [FontFamily] from the given Google Font name.
 *
 * @param name The name of theGoogle Font to use.
 * @return A [FontFamily] object
 */
@Stable
private fun FontFamily(name: String): FontFamily {
    // Create a GoogleFont object from the given name.
    val font = GoogleFont(name)
    // Create a FontFamily object with four different font weights.
    return FontFamily(
        Font(fontProvider = provider, googleFont = font, weight = FontWeight.Light),
        Font(fontProvider = provider, googleFont = font, weight = FontWeight.Medium),
        Font(fontProvider = provider, googleFont = font, weight = FontWeight.Normal),
        Font(fontProvider = provider, googleFont = font, weight = FontWeight.Bold),
    )
}

// Pre-configured global FontFamily instances.
private val OutfitFontFamily = FontFamily("Outfit")
private val RobotoFontFamily = FontFamily("Roboto")
private val DancingScriptFontFamily = FontFamily("Dancing Script")
private val GeomFontFamily = FontFamily("Geom")

// Extension properties providing seamless access to the app's standard typefaces.
val FontFamily.Companion.outfit get() = OutfitFontFamily
val FontFamily.Companion.geom get() = GeomFontFamily
val FontFamily.Companion.dancing_script get() = DancingScriptFontFamily
val FontFamily.Companion.roboto get() = RobotoFontFamily
val FontFamily.Companion.default inline get() = Default

// Pre-configured global spacing arrangements using common spacing dimensions.
private val LargeArrangement = Arrangement.spacedBy(large)
private val SmallArrangement = Arrangement.spacedBy(small)
private val xSmallArrangement = Arrangement.spacedBy(x_small)
private val mediumArrangement = Arrangement.spacedBy(medium)

// Extension properties for quickly applying spacing arrangements in Compose layouts.
val Arrangement.gap_large get() =  LargeArrangement
val Arrangement.gap_small get() =  SmallArrangement
val Arrangement.gap_x_small get() =  xSmallArrangement
val Arrangement.gap_medium get() =  mediumArrangement

/**
 * Centralized resource locator for the application.
 * Acts as a namespace providing unified access to strings, intents, preferences,
 * shapes, and dimensions used extensively across the app and Compose UI.
 */
object Res {
    /// Typealiases for direct access to Android resources (R.string, R.drawable, etc.)
    typealias string = com.zs.audiofy.R.string
    typealias drawable = com.zs.audiofy.R.drawable
    typealias raw = com.zs.audiofy.R.raw
    typealias array = com.zs.audiofy.R.array
    typealias plurals = com.zs.audiofy.R.plurals
    typealias font = FontFamily.Companion
    typealias layout = Arrangement

    /**
     * Global constants and intents.
     *
     * Provides URIs, package names, default colors, and permission lists
     * required for app configuration and external navigation.
     *
     * @property market_url_prefix Scheme prefix for Play Store app links.
     * @property market_web_url_prefix Scheme prefix for Play Store web links.
     * @property PKG_MARKET_ID Package ID for the Google Play Store.
     * @property color_accent_light Default accent color used in light theme.
     * @property color_accent_dark Default accent color used in dark theme.
     * @property intent_privacy_policy Reusable intent targeting the app's privacy policy.
     * @property intent_github_issues Reusable intent targeting the GitHub issues tracker.
     * @property intent_telegram Reusable intent targeting the Telegram support group.
     * @property intent_github Reusable intent targeting the main repository.
     * @property intent_join_beta Reusable intent to enroll in the Play Store beta track.
     * @property intent_share_app Reusable intent to launch the system share sheet.
     * @property intent_translate Reusable intent targeting the Crowdin translation project.
     */
    object app {
        //
        const val market_url_prefix = "market://details?id="
        const val market_web_url_prefix = "http://play.google.com/store/apps/details?id="
        const val PKG_MARKET_ID = "com.android.vending"

        // Default Accents
        val color_accent_light = Color(0xFF636A1D)
        val color_accent_dark = Color(0xFF4c662b)

        // Intents
        val intent_privacy_policy = com.zs.core.common.Intent(Intent.ACTION_VIEW) {
            data =
                Uri.parse("https://docs.google.com/document/d/1RzR-KTeycsuQik4rTzWbJU-F5lNqrJkTI8BP8Pu7sOs/edit?usp=sharing")
        }
        val intent_github_issues = com.zs.core.common.Intent(Intent.ACTION_VIEW) {
            data = Uri.parse("https://github.com/iZakirSheikh/OnePlayer/issues")
        }
        val intent_telegram = com.zs.core.common.Intent(Intent.ACTION_VIEW) {
            data = Uri.parse("https://t.me/audiofy_support")
        }
        val intent_github = com.zs.core.common.Intent(Intent.ACTION_VIEW) {
            data = Uri.parse("https://github.com/iZakirSheikh/OnePlayer")
        }
        val intent_join_beta = com.zs.core.common.Intent(Intent.ACTION_VIEW) {
            data = Uri.parse("https://play.google.com/apps/testing/com.prime.player/join")
        }
        val intent_share_app = com.zs.core.common.Intent(Intent.ACTION_SEND) {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Hey, check out this cool app: [app link here]")
        }
        val intent_translate = com.zs.core.common.Intent(Intent.ACTION_VIEW) {
            data = Uri.parse("https://crowdin.com/project/audiofy")
        }

        /**
         * Utility to check if the current SDK version is at least [api].
         */
        @ChecksSdkIntAtLeast(parameter = 0)
        fun isAtLeast(api: Int) = Build.VERSION.SDK_INT >= api
    }

    /**
     * Preference keys used throughout the app.
     *
     * Provides strongly typed keys for storing and retrieving app settings.
     *
     * @property night_mode Preference key defining the active dark/light mode state.
     * @property transparent_system_bars Preference key determining system bar translucency (adapts to API specific restrictions).
     * @property colorization_strategy Preference key for determining dynamic or static app-wide color generation.
     * @property color_accent_light Preference key for overriding the default light accent color.
     * @property color_accent_dark Preference key for overriding the default dark accent color.
     * @property glance_widget Preference key identifying the selected app widget visual style.
     * @property launch_counter Preference key tracking the total times the app has successfully started.
     * @property use_accent_in_nav_bar Preference key toggling whether the system navigation bar adopts the app's accent color.
     * @property app_config Preference key containing a serialized structure of general application settings.
     */
    object key {
        private const val PREFIX = "Audiofy"
        val night_mode =
            intPreferenceKey(
                "${PREFIX}_night_mode_policy",
                NightMode.ENABLED,
                OrdinalEnumSaver()
            )

        // For Android versions below 10 (API level 29), this is true by default, meaning
        // system bars are translucent and cannot be toggled.
        //
        // In Android 15 (API level 34), this preference is deprecated and no longer functional,
        // as system bar translucency is managed by the system and cannot be customized.
        //
        // For intermediate versions (between Android 10 and Android 15), this setting is false
        // by default but can be toggled to enable or disable translucent system bars based
        // on user preferences.
        val transparent_system_bars = booleanPreferenceKey(
            PREFIX + "_force_colorize",
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
        )

        //val CLOSE_WHEN_TASK_REMOVED = Playback.PREF_KEY_CLOSE_WHEN_REMOVED
        val colorization_strategy = intPreferenceKey(
            "${PREFIX}_colorization_strategy",
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) AccentColorPolicy.WALLPAPER else AccentColorPolicy.DEFAULT,
            OrdinalEnumSaver()
        )
        val color_accent_light =
            intPreferenceKey("${PREFIX}_color_accent_light", app.color_accent_light, ColorSaver)
        val color_accent_dark =
            intPreferenceKey("${PREFIX}_color_accent_dark", app.color_accent_dark, ColorSaver)
        val glance_widget =
            stringPreferenceKey("${PREFIX}_glance", Paymaster.IAP_PLATFORM_WIDGET_IPHONE)
        val launch_counter =
            intPreferenceKey("Audiofy_launch_counter", 0)
        val use_accent_in_nav_bar = booleanPreferenceKey("use_accent_in_nav_bar", false)
        val app_config = stringPreferenceKey("${PREFIX}_app_config")
    }

    /**
     * Common access to Compose shapes.
     *
     * @property section Highly rounded shape intended for standalone items or containers.
     * @property section_top A shape with rounded top corners only; ideal for the first item in grouped lists.
     * @property section_middle A flat rectangle shape for items sandwiched in a grouped list.
     * @property section_bottom A shape with rounded bottom corners only; ideal for the final item in grouped lists.
     * @property squircle A smooth, continuous curve bridging the gap between squares and circles.
     * @property folder A dynamic shape styled visually like a physical file folder.
     * @property ghost A custom aesthetic shape used for specific playful UI elements.
     * @property circle Standard perfectly circular layout boundary.
     * @property rectangle Standard sharp-cornered rectangular layout boundary.
     * @property sunny A stylized shape designed to mimic a sunburst or similar geometry.
     * @property compact_disk A hollow-centered disc shape mimicking a CD or vinyl record.
     * @property crt_screen A slightly bowed square shape replicating the curvature of vintage tube displays.
     */
    object shape {

        // Used to style individual items within a column section.
        val section = RoundedCornerShape(20.dp)
        val section_top = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
        val section_middle = RectangleShape
        val section_bottom = RoundedCornerShape(0.dp, 0.dp, 20.dp, 20.dp)

        val squircle = SuperellipseShape(0.5f)
        val folder =  FolderShape(0.18f)
        val ghost get() =  GhostShape

        val circle get() =  CircleShape
        val rectangle get() =  RectangleShape
        val sunny = SunnyShape(0.4f)
        val compact_disk get() = CompactDisk
        val crt_screen get() = CrtScreenShape
    }

    /**
     * Standardized dimension scale for consistent UI spacing, margins, and padding.
     *
     * @property x_small Smallest default padding constraint (4.dp).
     * @property small Minor padding block (8.dp).
     * @property medium Standard mid-level spacing boundary (12.dp).
     * @property normal Default large container padding (16.dp).
     * @property large Prominent separation between major layout groupings (22.dp).
     * @property x_large Extreme spacing for hero-level offsets (32.dp).
     */
    object space {
        val x_small: Dp = 4.dp
        val small: Dp = 8.dp
        val medium: Dp = 12.dp
        val normal: Dp = 16.dp
        val large: Dp = 22.dp
        val x_large: Dp = 32.dp
    }
}