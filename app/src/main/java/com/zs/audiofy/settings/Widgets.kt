package com.zs.audiofy.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zs.audiofy.MainActivity
import com.zs.audiofy.common.IAP_COLOR_CROFT_GOLDEN_DUST
import com.zs.audiofy.common.IAP_COLOR_CROFT_GRADIENT_GROVES
import com.zs.audiofy.common.IAP_COLOR_CROFT_MISTY_DREAM
import com.zs.audiofy.common.IAP_COLOR_CROFT_ROTATING_GRADIENT
import com.zs.audiofy.common.IAP_COLOR_CROFT_WAVY_GRADIENT_DOTS
import com.zs.audiofy.common.IAP_COLOR_CROFT_WIDGET_BUNDLE
import com.zs.audiofy.common.IAP_PLATFORM_WIDGET_DISK_DYNAMO
import com.zs.audiofy.common.IAP_PLATFORM_WIDGET_ELONGATE_BEAT
import com.zs.audiofy.common.IAP_PLATFORM_WIDGET_IPHONE
import com.zs.audiofy.common.IAP_PLATFORM_WIDGET_RED_VIOLET_CAKE
import com.zs.audiofy.common.IAP_PLATFORM_WIDGET_SKEWED_DYNAMIC
import com.zs.audiofy.common.IAP_PLATFORM_WIDGET_SNOW_CONE
import com.zs.audiofy.common.IAP_PLATFORM_WIDGET_TIRAMISU
import com.zs.audiofy.common.IAP_WIDGETS_PLATFORM
import com.zs.audiofy.common.Res
import com.zs.audiofy.common.compose.LocalSystemFacade
import com.zs.audiofy.common.compose.preference
import com.zs.audiofy.common.compose.rememberAcrylicSurface
import com.zs.audiofy.common.compose.shine
import com.zs.audiofy.common.ellipsize
import com.zs.audiofy.common.isFreemium
import com.zs.audiofy.common.richDesc
import com.zs.audiofy.common.vectorResource
import com.zs.audiofy.console.widget.Widget
import com.zs.audiofy.console.widget.styles.DiskDynamo
import com.zs.audiofy.console.widget.styles.ElongatedBeat
import com.zs.audiofy.console.widget.styles.GoldenDust
import com.zs.audiofy.console.widget.styles.GradientGroves
import com.zs.audiofy.console.widget.styles.MistyDream
import com.zs.audiofy.console.widget.styles.MistyTunes
import com.zs.audiofy.console.widget.styles.RedVioletCake
import com.zs.audiofy.console.widget.styles.RotatingColorGradient
import com.zs.audiofy.console.widget.styles.SkewedDynamic
import com.zs.audiofy.console.widget.styles.SnowCone
import com.zs.audiofy.console.widget.styles.Tiramisu
import com.zs.audiofy.console.widget.styles.WavyGradientDots
import com.zs.compose.foundation.Background
import com.zs.compose.theme.AppTheme
import com.zs.compose.theme.Button
import com.zs.compose.theme.ButtonDefaults
import com.zs.compose.theme.ContentAlpha
import com.zs.compose.theme.Icon
import com.zs.compose.theme.LocalSharedAnimsEnabled
import com.zs.compose.theme.None
import com.zs.compose.theme.SplitButtonLayout
import com.zs.compose.theme.TrailingSplitButton
import com.zs.compose.theme.appbar.TopAppBar
import com.zs.compose.theme.snackbar.SnackbarDuration
import com.zs.compose.theme.text.Header
import com.zs.compose.theme.text.Label
import com.zs.core.billing.Paymaster
import com.zs.core.billing.Product
import com.zs.core.billing.Purchase
import com.zs.core.billing.purchased
import com.zs.core.playback.NowPlaying
import kotlinx.coroutines.flow.combine


private typealias Purchasable = Pair<Purchase?, Product?>

private const val TAG = "Config"

/**
 * Represents all the widgets offered by the app.
 */
private val WIDGETS = arrayOf(
    // Widgets Platform
    Paymaster.IAP_PLATFORM_WIDGET_IPHONE,
    Paymaster.IAP_PLATFORM_WIDGET_RED_VIOLET_CAKE,
    Paymaster.IAP_PLATFORM_WIDGET_SNOW_CONE,
    Paymaster.IAP_PLATFORM_WIDGET_TIRAMISU,
    Paymaster.IAP_PLATFORM_WIDGET_DISK_DYNAMO,
    Paymaster.IAP_PLATFORM_WIDGET_ELONGATE_BEAT,
    Paymaster.IAP_PLATFORM_WIDGET_SKEWED_DYNAMIC,

    // widgets COLOR_CROFT
    Paymaster.IAP_COLOR_CROFT_GRADIENT_GROVES,
    Paymaster.IAP_COLOR_CROFT_GOLDEN_DUST,
    Paymaster.IAP_COLOR_CROFT_WAVY_GRADIENT_DOTS,
    Paymaster.IAP_COLOR_CROFT_ROTATING_GRADIENT,
    Paymaster.IAP_COLOR_CROFT_MISTY_DREAM,
)

/** @return the bungle key in which widget [key] resides.*/
private fun bundleKeyOf(key: String): String {
    return when (key) {
        // Widgets Platform
        Paymaster.IAP_PLATFORM_WIDGET_IPHONE,
        Paymaster.IAP_PLATFORM_WIDGET_RED_VIOLET_CAKE,
        Paymaster.IAP_PLATFORM_WIDGET_SNOW_CONE,
        Paymaster.IAP_PLATFORM_WIDGET_TIRAMISU,
        Paymaster.IAP_PLATFORM_WIDGET_DISK_DYNAMO,
        Paymaster.IAP_PLATFORM_WIDGET_ELONGATE_BEAT,
        Paymaster.IAP_PLATFORM_WIDGET_SKEWED_DYNAMIC -> Paymaster.IAP_WIDGETS_PLATFORM

        // widgets COLOR_CROFT
        Paymaster.IAP_COLOR_CROFT_GRADIENT_GROVES,
        Paymaster.IAP_COLOR_CROFT_GOLDEN_DUST,
        Paymaster.IAP_COLOR_CROFT_WAVY_GRADIENT_DOTS,
        Paymaster.IAP_COLOR_CROFT_ROTATING_GRADIENT,
        Paymaster.IAP_COLOR_CROFT_MISTY_DREAM -> Paymaster.IAP_COLOR_CROFT_WIDGET_BUNDLE

        else -> error("No mapping key found for key: $key")
    }
}

private val NonePlaying = NowPlaying(null, null)
private val SplitButtonShape = RoundedCornerShape(8.dp) to CircleShape

/**
 * Represents the widget preview flyout
 */
@Composable
context(_: RouteSettings)
fun Widgets(modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.pointerInput(Unit, {})) {
        // viewpager
        val selected by preference(Res.key.glance_widget)
        var index by remember {
            mutableIntStateOf(WIDGETS.indexOf(selected))
        }

        val facade = LocalSystemFacade.current
        val surface = rememberAcrylicSurface() // just placeholder state.
        val onRequest ={ code: Float -> facade.showSnackbar(Res.string.msg_widget_preview_only) }
        val state by produceState(NonePlaying) {
            val remote = (facade as? MainActivity)?.remote ?: return@produceState
            remote.state.collect { value = it ?: NonePlaying }
        }

        // purchase etc.
        val activity = facade as? MainActivity ?: return@Column
        val paymaster = activity.paymaster
        val widget: Purchasable? by produceState(null, index) {
            val key = WIDGETS[index]
            paymaster.purchases.combine(paymaster.details) { purchases, details ->
                val purchsae = purchases.find { it.id == key }
                val info = details.find { it.id == key }
                purchsae to info
            }.collect { value = it }
        }
        val bundle: Purchasable? by produceState(null, WIDGETS[index]) {
            val key = bundleKeyOf(WIDGETS[index])
            paymaster.purchases.combine(paymaster.details) { purchases, details ->
                val purchsae = purchases.find { it.id == key }
                val info = details.find { it.id == key }
                purchsae to info
            }.collect { value = it }
        }

        // Header
        Header(
            drawDivider = true,
            text = buildAnnotatedString {
                append((bundle?.second?.title ?: "Widgets").ellipsize(16))
                append("\n")
                withStyle(SpanStyle(fontSize = 11.sp)){
                    append(widget?.second?.title ?: "N/A")
                }
            },
            style = AppTheme.typography.body2,
            contentPadding = RouteSettings.HeaderPadding,
            action = action@{
                if (bundle?.first.purchased)
                    return@action Spacer(Modifier)
                Button(onClick = { facade.initiatePurchaseFlow(bundle?.second?.id ?: "") }) {
                    Icon(
                        vectorResource(Res.drawable.ic_lock_open),
                        contentDescription = null
                    )
                    Label(bundle?.second?.formattedPrice ?: "N/A")
                }
            }
        )

        // Widgets
        CompositionLocalProvider(LocalSharedAnimsEnabled provides false) {
            when (WIDGETS[index]) {
                Paymaster.IAP_PLATFORM_WIDGET_DISK_DYNAMO -> DiskDynamo(state, onRequest)
                Paymaster.IAP_PLATFORM_WIDGET_ELONGATE_BEAT -> ElongatedBeat(state, onRequest)
                Paymaster.IAP_COLOR_CROFT_GOLDEN_DUST -> GoldenDust(state, onRequest)
                Paymaster.IAP_COLOR_CROFT_GRADIENT_GROVES -> GradientGroves(state, onRequest)
                Paymaster.IAP_PLATFORM_WIDGET_IPHONE -> MistyTunes(state, surface, onRequest)
                Paymaster.IAP_COLOR_CROFT_MISTY_DREAM -> MistyDream(state, onRequest)
                Paymaster.IAP_PLATFORM_WIDGET_RED_VIOLET_CAKE ->
                    RedVioletCake(state, onRequest)

                Paymaster.IAP_COLOR_CROFT_ROTATING_GRADIENT ->
                    RotatingColorGradient(state, onRequest)

                Paymaster.IAP_PLATFORM_WIDGET_SKEWED_DYNAMIC ->
                    SkewedDynamic(state, onRequest)

                Paymaster.IAP_PLATFORM_WIDGET_SNOW_CONE -> SnowCone(state, onRequest)
                Paymaster.IAP_PLATFORM_WIDGET_TIRAMISU -> Tiramisu(state, onRequest)
                Paymaster.IAP_COLOR_CROFT_WAVY_GRADIENT_DOTS ->
                    WavyGradientDots(state, onRequest)
            }
        }

        // BottomBar
        TopAppBar(
            shape = Res.shape.section_bottom,
            elevation = 0.dp,
            modifier = Modifier.padding(top = Res.space.small),
            background = Background(AppTheme.colors.background(1.dp)),
            windowInsets = WindowInsets.None,
            title = {
                SplitButtonLayout(
                    modifier = Modifier.scale(0.85f),
                    leadingButton = {
                        val (purchase, info) = widget ?: return@SplitButtonLayout
                        val key = WIDGETS[index]
                        // An item is considered unlocked and available for use if any of the following conditions are met:
                        // 1. It is a freemium item.
                        // 2. The user has purchased the entire bundle to which this item belongs.
                        // 3. The user has specifically purchased this individual item.
                        val unlocked =
                            purchase.purchased || info?.isFreemium == true || bundle?.first?.purchased == true
                        Button(
                            if (unlocked) "APPLY" else info?.formattedPrice ?: stringResource(Res.string.abbr_not_available),
                            onClick = {
                                if (!unlocked)
                                    activity.initiatePurchaseFlow(key)
                                else {
                                    activity.setPreference(Res.key.glance_widget, WIDGETS[index])
                                    activity.showToast("Widget have been successfully applied.")
                                    onRequest(Widget.REQUEST_SHOW_CONFIG)
                                }
                            },
                            enabled = key != selected
                        )
                    },
                    trailingButton = {
                        TrailingSplitButton(
                            shapes = AppTheme.shapes.small to AppTheme.shapes.large,
                            content = {
                                Icon(
                                    vectorResource(Res.drawable.ic_info),
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                val info = widget?.second?.richDesc
                                if (info != null)
                                    activity.showSnackbar(
                                        info,
                                        icon = ImageVector.vectorResource(activity.theme, activity.resources, Res.drawable.ic_info, ),
                                        duration = SnackbarDuration.Indefinite
                                    )
                            },
                        )
                    }
                )
            },
            actions = {
                val colors = ButtonDefaults.filledTonalButtonColors(
                    backgroundColor = AppTheme.colors.accent.copy(ContentAlpha.indication)
                )
                SplitButtonLayout(
                    leadingButton = {
                        TrailingSplitButton(
                            shapes = SplitButtonShape,
                            enabled = index != 0,
                            colors = colors,
                            border = AppTheme.colors.shine,
                            content = {
                                Icon(vectorResource(Res.drawable.ic_keyboard_double_arrow_left), contentDescription = null)
                            },
                            onClick = { index -= 1 },
                        )
                    },
                    trailingButton = {
                        TrailingSplitButton(
                            shapes = SplitButtonShape,
                            enabled = index < WIDGETS.lastIndex,
                            colors = colors,
                            border = AppTheme.colors.shine,
                            content = {
                                Icon(vectorResource(Res.drawable.ic_keyboard_double_arrow_right), contentDescription = null)
                            },
                            onClick = { index += 1 },
                        )
                    },
                    modifier = Modifier.scale(0.85f)
                )
            }
        )
    }
}