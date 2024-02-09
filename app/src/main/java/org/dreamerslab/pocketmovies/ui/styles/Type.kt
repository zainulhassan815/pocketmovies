package org.dreamerslab.pocketmovies.ui.styles

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import org.dreamerslab.pocketmovies.R

private val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

private val font = GoogleFont("Poppins")
private val fontFamily = FontFamily(
    Font(font, provider),
    Font(font, provider, FontWeight.Medium),
)

private val defaultTypography = Typography()

val Typography = Typography(
    titleLarge = defaultTypography.titleLarge.copy(
        fontFamily = fontFamily
    ),
    titleMedium = defaultTypography.titleMedium.copy(
        fontFamily = fontFamily,
    ),
    titleSmall = defaultTypography.titleSmall.copy(
        fontFamily = fontFamily,
    ),
    bodyLarge = defaultTypography.bodyLarge.copy(
        fontFamily = fontFamily,
    ),
    bodyMedium = defaultTypography.bodyMedium.copy(
        fontFamily = fontFamily,
    ),
    bodySmall = defaultTypography.bodySmall.copy(
        fontFamily = fontFamily,
    ),
    labelLarge = defaultTypography.labelLarge.copy(
        fontFamily = fontFamily,
    ),
    labelMedium = defaultTypography.labelMedium.copy(
        fontFamily = fontFamily,
    ),
    labelSmall = defaultTypography.labelSmall.copy(
        fontFamily = fontFamily,
    ),
)