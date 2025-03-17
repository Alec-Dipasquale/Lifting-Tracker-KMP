package com.squalec.liftingtracker.android.ui.themes

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.squalec.liftingtracker.android.R


object FontFamilyCustom {
    val Unbounded: FontFamily
        get() = unbounded

    val Nunito: FontFamily
        get() = nunito

}


val nunito = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_black, FontWeight.Black),
    Font(R.font.nunito_light, FontWeight.Light),
    Font(R.font.nunito_extra_bold, FontWeight.ExtraBold),
    Font(R.font.nunito_extra_light, FontWeight.ExtraLight),
    Font(R.font.nunito_semi_bold, FontWeight.SemiBold),
)


val unbounded = FontFamily(
    Font(R.font.unbounded_regular, FontWeight.Normal),
    Font(R.font.unbounded_medium, FontWeight.Medium),
    Font(R.font.unbounded_bold, FontWeight.Bold),
    Font(R.font.unbounded_black, FontWeight.Black),
    Font(R.font.unbounded_light, FontWeight.Light),
    Font(R.font.unbounded_extra_bold, FontWeight.ExtraBold),
    Font(R.font.unbounded_extra_light, FontWeight.ExtraLight),
    Font(R.font.unbounded_semi_bold, FontWeight.SemiBold),
)