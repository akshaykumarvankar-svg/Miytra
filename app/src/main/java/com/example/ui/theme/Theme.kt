package com.example.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val MityraDarkColorScheme = darkColorScheme(
    primary = MityraCoral,
    onPrimary = MityraTextPrimary,
    primaryContainer = MityraDarkSurfaceVariant,
    onPrimaryContainer = MityraCoralGlow,
    secondary = MityraGold,
    onSecondary = MityraDarkBackground,
    secondaryContainer = MityraDarkSurfaceVariant,
    onSecondaryContainer = MityraGold,
    tertiary = MityraPurpleLight,
    onTertiary = MityraDarkBackground,
    background = MityraDarkBackground,
    onBackground = MityraTextPrimary,
    surface = MityraDarkSurface,
    onSurface = MityraTextPrimary,
    surfaceVariant = MityraDarkSurfaceVariant,
    onSurfaceVariant = MityraTextSecondary,
    outline = MityraCardBorder,
    error = MityraSosRed,
    onError = MityraTextPrimary
)

@Composable
fun MityraTheme(
    content: @Composable () -> Unit
) {
    MyApplicationTheme(content = content)
}

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = MityraDarkColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = MityraDarkBackground.toArgb()
                window.navigationBarColor = MityraDarkBackground.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
