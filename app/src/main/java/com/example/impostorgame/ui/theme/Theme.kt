package com.example.impostorgame.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Dark-first color scheme with our custom palette
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    onPrimary = TextWhite,
    primaryContainer = PrimaryPurpleLight,
    onPrimaryContainer = TextWhite,
    secondary = SecondaryBlue,
    onSecondary = TextWhite,
    secondaryContainer = DarkCard,
    onSecondaryContainer = TextLight,
    tertiary = AccentCyan,
    onTertiary = TextDark,
    background = DarkBackground,
    onBackground = TextWhite,
    surface = DarkSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextMuted,
    error = ImpostorRed,
    onError = TextWhite,
    outline = DividerColor
)

@Composable
fun ImpostorGameTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme

    // Set status bar color to match our dark background
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = DarkBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}