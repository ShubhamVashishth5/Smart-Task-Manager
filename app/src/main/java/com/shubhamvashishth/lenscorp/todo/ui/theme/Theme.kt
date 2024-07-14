package com.shubhamvashishth.lenscorp.todo.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)


object ColorPalette {

    object RetroMetro {
        val crimson = Color(0xFFEA5545)
        val pink = Color(0xFFF46A9B)
        val orange = Color(0xFFEF9B20)
        val yellow = Color(0xFFEDBF33)
        val lightYellow = Color(0xFFEDE15B)
        val lime = Color(0xFFBDCF32)
        val green = Color(0xFF87BC45)
        val lightBlue = Color(0xFF27AEFF)
        val purple = Color(0xFFB33DC6)
    }

    object DutchField {
        val red = Color(0xFFE60049)
        val blue = Color(0xFF0BB4FF)
        val green = Color(0xFF50E991)
        val yellow = Color(0xFFE6D800)
        val purple = Color(0xFF9B19F5)
        val orange = Color(0xFFFFA300)
        val magenta = Color(0xFFDC0AB4)
        val lightBlue = Color(0xFFB3D4FF)
        val teal = Color(0xFF00BFA0)
    }

    object RiverNights {
        val red = Color(0xFFB30000)
        val maroon = Color(0xFF7C1158)
        val indigo = Color(0xFF4421AF)
        val blue = Color(0xFF1A53FF)
        val skyBlue = Color(0xFF0D88E6)
        val turquoise = Color(0xFF00B7C7)
        val green = Color(0xFF5AD45A)
        val lime = Color(0xFF8BE04E)
        val yellow = Color(0xFFEBDC78)
    }

    object SpringPastel {
        val coralRed = Color(0xFFFD7F6F)
        val skyBlue = Color(0xFF7EB0D5)
        val springGreen = Color(0xFFB2E061)
        val lavenderPurple = Color(0xFFBD7EBE)
        val apricotOrange = Color(0xFFFFB55A)
        val lemonYellow = Color(0xFFEEEE65)
        val lilac = Color(0xFFBEB9DB)
        val rosePink = Color(0xFFFDCCFE)
        val teal = Color(0xFF8BD3C7)
    }

    object DataColor {
        val navyBlue = Color(0xFF003F5C)
        val darkBlue = Color(0xFF2F4B7C)
        val deepPurple = Color(0xFF665191)
        val magenta = Color(0xFFA05195)
        val darkPink = Color(0xFFD45087)
        val coral = Color(0xFFF95D6A)
        val orange = Color(0xFFFF7C43)
        val yellow = Color(0xFFFFA600)
    }
}

@Composable
fun SmartTaskManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}