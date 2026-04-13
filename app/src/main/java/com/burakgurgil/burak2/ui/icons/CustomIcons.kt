package com.burakgurgil.burak2.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object CustomIcons {
    // Icon 1: Çöp kovasına gönderme butonu (Silme) - trash-plus
    val TrashPlus: ImageVector = ImageVector.Builder(
        name = "TrashPlus",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2.0f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            strokeLineMiter = 4.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(12.0f, 12.0f)
            lineTo(12.0f, 16.0f)
            moveTo(10.0f, 14.0f)
            lineTo(14.0f, 14.0f)
            moveTo(16.0f, 6.0f)
            lineTo(15.7294f, 5.18807f)
            curveTo(15.4671f, 4.40125f, 15.3359f, 4.00784f, 15.0927f, 3.71698f)
            curveTo(14.8779f, 3.46013f, 14.6021f, 3.26132f, 14.2905f, 3.13878f)
            curveTo(13.9376f, 3.0f, 13.523f, 3.0f, 12.6936f, 3.0f)
            lineTo(11.3064f, 3.0f)
            curveTo(10.477f, 3.0f, 10.0624f, 3.0f, 9.70951f, 3.13878f)
            curveTo(9.39792f, 3.26132f, 9.12208f, 3.46013f, 8.90729f, 3.71698f)
            curveTo(8.66405f, 4.00784f, 8.53292f, 4.40125f, 8.27064f, 5.18807f)
            lineTo(8.0f, 6.0f)
            moveTo(4.0f, 6.0f)
            lineTo(20.0f, 6.0f)
            moveTo(18.0f, 6.0f)
            lineTo(18.0f, 16.2f)
            curveTo(18.0f, 17.8802f, 18.0f, 18.7202f, 17.673f, 19.362f)
            curveTo(17.3854f, 19.9265f, 16.9265f, 20.3854f, 16.362f, 20.673f)
            curveTo(15.7202f, 21.0f, 14.8802f, 21.0f, 13.2f, 21.0f)
            lineTo(10.8f, 21.0f)
            curveTo(9.11984f, 21.0f, 8.27976f, 21.0f, 7.63803f, 20.673f)
            curveTo(7.07354f, 20.3854f, 6.6146f, 19.9265f, 6.32698f, 19.362f)
            curveTo(6.0f, 18.7202f, 6.0f, 17.8802f, 6.0f, 16.2f)
            lineTo(6.0f, 6.0f)
        }
    }.build()

    // Icon 2: Çöp kovası - trash-blank
    val TrashBlank: ImageVector = ImageVector.Builder(
        name = "TrashBlank",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2.0f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            strokeLineMiter = 4.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(18.0f, 6.0f)
            lineTo(18.0f, 16.2f)
            curveTo(18.0f, 17.8802f, 18.0f, 18.7202f, 17.673f, 19.362f)
            curveTo(17.3854f, 19.9265f, 16.9265f, 20.3854f, 16.362f, 20.673f)
            curveTo(15.7202f, 21.0f, 14.8802f, 21.0f, 13.2f, 21.0f)
            lineTo(10.8f, 21.0f)
            curveTo(9.11984f, 21.0f, 8.27976f, 21.0f, 7.63803f, 20.673f)
            curveTo(7.07354f, 20.3854f, 6.6146f, 19.9265f, 6.32698f, 19.362f)
            curveTo(6.0f, 18.7202f, 6.0f, 17.8802f, 6.0f, 16.2f)
            lineTo(6.0f, 6.0f)
            moveTo(4.0f, 6.0f)
            lineTo(20.0f, 6.0f)
            moveTo(16.0f, 6.0f)
            lineTo(15.7294f, 5.18807f)
            curveTo(15.4671f, 4.40125f, 15.3359f, 4.00784f, 15.0927f, 3.71698f)
            curveTo(14.8779f, 3.46013f, 14.6021f, 3.26132f, 14.2905f, 3.13878f)
            curveTo(13.9376f, 3.0f, 13.523f, 3.0f, 12.6936f, 3.0f)
            lineTo(11.3064f, 3.0f)
            curveTo(10.477f, 3.0f, 10.0624f, 3.0f, 9.70951f, 3.13878f)
            curveTo(9.39792f, 3.26132f, 9.12208f, 3.46013f, 8.90729f, 3.71698f)
            curveTo(8.66405f, 4.00784f, 8.53292f, 4.40125f, 8.27064f, 5.18807f)
            lineTo(8.0f, 6.0f)
        }
    }.build()

    // Icon 3: Çöp kovasından geri yükleme - trash-undo
    val TrashUndo: ImageVector = ImageVector.Builder(
        name = "TrashUndo",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2.0f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            strokeLineMiter = 4.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(11.0f, 15.0f)
            lineTo(9.0f, 13.0f)
            moveTo(9.0f, 13.0f)
            lineTo(11.0f, 11.0f)
            moveTo(9.0f, 13.0f)
            lineTo(13.0f, 13.0f)
            curveTo(14.1046f, 13.0f, 15.0f, 13.8954f, 15.0f, 15.0f)
            lineTo(15.0f, 16.0f)
            moveTo(16.0f, 6.0f)
            lineTo(15.7294f, 5.18807f)
            curveTo(15.4671f, 4.40125f, 15.3359f, 4.00784f, 15.0927f, 3.71698f)
            curveTo(14.8779f, 3.46013f, 14.6021f, 3.26132f, 14.2905f, 3.13878f)
            curveTo(13.9376f, 3.0f, 13.523f, 3.0f, 12.6936f, 3.0f)
            lineTo(11.3064f, 3.0f)
            curveTo(10.477f, 3.0f, 10.0624f, 3.0f, 9.70951f, 3.13878f)
            curveTo(9.39792f, 3.26132f, 9.12208f, 3.46013f, 8.90729f, 3.71698f)
            curveTo(8.66405f, 4.00784f, 8.53292f, 4.40125f, 8.27064f, 5.18807f)
            lineTo(8.0f, 6.0f)
            moveTo(4.0f, 6.0f)
            lineTo(20.0f, 6.0f)
            moveTo(18.0f, 6.0f)
            lineTo(18.0f, 16.2f)
            curveTo(18.0f, 17.8802f, 18.0f, 18.7202f, 17.673f, 19.362f)
            curveTo(17.3854f, 19.9265f, 16.9265f, 20.3854f, 16.362f, 20.673f)
            curveTo(15.7202f, 21.0f, 14.8802f, 21.0f, 13.2f, 21.0f)
            lineTo(10.8f, 21.0f)
            curveTo(9.11984f, 21.0f, 8.27976f, 21.0f, 7.63803f, 20.673f)
            curveTo(7.07354f, 20.3854f, 6.6146f, 19.9265f, 6.32698f, 19.362f)
            curveTo(6.0f, 18.7202f, 6.0f, 17.8802f, 6.0f, 16.2f)
            lineTo(6.0f, 6.0f)
        }
    }.build()

    // Icon 4: Çöp kovasındaki her şeyi silme - trash-xmark
    val TrashXmark: ImageVector = ImageVector.Builder(
        name = "TrashXmark",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2.0f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            strokeLineMiter = 4.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(10.0f, 12.0f)
            lineTo(14.0f, 16.0f)
            moveTo(14.0f, 12.0f)
            lineTo(10.0f, 16.0f)
            moveTo(18.0f, 6.0f)
            lineTo(17.1991f, 18.0129f)
            curveTo(17.129f, 19.065f, 17.0939f, 19.5911f, 16.8667f, 19.99f)
            curveTo(16.6666f, 20.3412f, 16.3648f, 20.6235f, 16.0011f, 20.7998f)
            curveTo(15.588f, 21.0f, 15.0607f, 21.0f, 14.0062f, 21.0f)
            lineTo(9.99377f, 21.0f)
            curveTo(8.93927f, 21.0f, 8.41202f, 21.0f, 7.99889f, 20.7998f)
            curveTo(7.63517f, 20.6235f, 7.33339f, 20.3412f, 7.13332f, 19.99f)
            curveTo(6.90607f, 19.5911f, 6.871f, 19.065f, 6.80086f, 18.0129f)
            lineTo(6.0f, 6.0f)
            moveTo(4.0f, 6.0f)
            lineTo(20.0f, 6.0f)
            moveTo(16.0f, 6.0f)
            lineTo(15.7294f, 5.18807f)
            curveTo(15.4671f, 4.40125f, 15.3359f, 4.00784f, 15.0927f, 3.71698f)
            curveTo(14.8779f, 3.46013f, 14.6021f, 3.26132f, 14.2905f, 3.13878f)
            curveTo(13.9376f, 3.0f, 13.523f, 3.0f, 12.6936f, 3.0f)
            lineTo(11.3064f, 3.0f)
            curveTo(10.477f, 3.0f, 10.0624f, 3.0f, 9.70951f, 3.13878f)
            curveTo(9.39792f, 3.26132f, 9.12208f, 3.46013f, 8.90729f, 3.71698f)
            curveTo(8.66405f, 4.00784f, 8.53292f, 4.40125f, 8.27064f, 5.18807f)
            lineTo(8.0f, 6.0f)
        }
    }.build()

    // Tema İkonları
    // Icon: Açık tema (whitetheme.svg) - Ampul ikonu
    val WhiteTheme: ImageVector = ImageVector.Builder(
        name = "WhiteTheme",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.EvenOdd
        ) {
            // Ana ampul gövdesi (dış çizgi) - tam ortalanmış (4.5f sağa kaydırıldı: orijinal merkez 7.5 -> yeni merkez 12.0)
            moveTo(14.828f, 8.33f)
            arcToRelative(5.903f, 5.903f, 0.0f, false, true, -1.439f, 3.64f)
            arcToRelative(2.874f, 2.874f, 0.0f, false, false, -0.584f, 1.0f)
            verticalLineToRelative(1.037f)
            arcToRelative(0.95f, 0.95f, 0.0f, false, true, -0.95f, 0.95f)
            horizontalLineToRelative(-3.71f)
            arcToRelative(0.95f, 0.95f, 0.0f, false, true, -0.95f, -0.95f)
            verticalLineToRelative(-1.037f)
            arcToRelative(2.876f, 2.876f, 0.0f, false, false, -0.584f, -1.0f)
            arcToRelative(5.903f, 5.903f, 0.0f, false, true, -1.439f, -3.64f)
            arcToRelative(4.83f, 4.83f, 0.0f, false, true, 9.28f, -1.878f)
            arcToRelative(4.796f, 4.796f, 0.0f, false, true, 0.38f, 1.88f)
            close()
            // İç ampul gövdesi - tam ortalanmış
            moveTo(13.878f, 8.33f)
            arcToRelative(3.878f, 3.878f, 0.0f, false, false, -7.756f, 0.0f)
            curveToRelative(0.0f, 2.363f, 2.023f, 3.409f, 2.023f, 4.64f)
            verticalLineToRelative(1.037f)
            horizontalLineToRelative(3.71f)
            verticalLineToRelative(-1.037f)
            curveToRelative(0.0f, -1.231f, 2.023f, -2.277f, 2.023f, -4.64f)
            close()
            // Alt çizgi - tam ortalanmış
            moveTo(12.33f, 16.072f)
            arcToRelative(0.475f, 0.475f, 0.0f, false, true, -0.475f, 0.476f)
            horizontalLineToRelative(-3.71f)
            arcToRelative(0.475f, 0.475f, 0.0f, false, true, 0.0f, -0.95f)
            horizontalLineToRelative(3.71f)
            arcToRelative(0.475f, 0.475f, 0.0f, false, true, 0.475f, 0.474f)
            close()
            // Alt nokta - tam ortalanmış
            moveTo(11.69f, 17.334f)
            arcToRelative(0.238f, 0.238f, 0.0f, false, true, -0.078f, 0.265f)
            arcToRelative(2.669f, 2.669f, 0.0f, false, true, -3.274f, 0.0f)
            arcToRelative(0.237f, 0.237f, 0.0f, false, true, 0.145f, -0.425f)
            horizontalLineToRelative(2.983f)
            arcToRelative(0.238f, 0.238f, 0.0f, false, true, 0.225f, 0.16f)
            close()
        }
    }.build()

    // Icon: Koyu tema (blacktheme.svg)
    val BlackTheme: ImageVector = ImageVector.Builder(
        name = "BlackTheme",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 512.0f,
        viewportHeight = 512.0f
    ).apply {
        path(
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(256.0f, 0.0f)
            curveTo(114.6f, 0.0f, 0.0f, 114.6f, 0.0f, 256.0f)
            reflectiveCurveToRelative(114.6f, 256.0f, 256.0f, 256.0f)
            reflectiveCurveToRelative(256.0f, -114.6f, 256.0f, -256.0f)
            reflectiveCurveTo(397.4f, 0.0f, 256.0f, 0.0f)
            close()
            moveTo(256.0f, 469.3f)
            verticalLineToRelative(-426.6f)
            curveToRelative(117.8f, 0.0f, 213.3f, 95.5f, 213.3f, 213.3f)
            curveTo(469.3f, 373.8f, 373.8f, 469.3f, 256.0f, 469.3f)
            close()
        }
    }.build()

    // Icon: Yaz teması (summertheme.svg)
    val SummerTheme: ImageVector = ImageVector.Builder(
        name = "SummerTheme",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2.0f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            strokeLineMiter = 10.0f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(20.0f, 11.0f)
            horizontalLineToRelative(-2.0f)
            curveToRelative(0.0f, 0.0f, -0.1f, 0.0f, -0.1f, 0.0f)
            curveToRelative(-0.2f, -0.9f, -0.5f, -1.8f, -1.0f, -2.5f)
            curveToRelative(0.0f, 0.0f, 0.1f, 0.0f, 0.1f, -0.1f)
            lineToRelative(1.4f, -1.4f)
            curveToRelative(0.4f, -0.4f, 0.4f, -1.0f, 0.0f, -1.4f)
            reflectiveCurveToRelative(-1.0f, -0.4f, -1.4f, 0.0f)
            lineToRelative(-1.4f, 1.4f)
            curveToRelative(0.0f, 0.0f, 0.0f, 0.1f, -0.1f, 0.1f)
            curveToRelative(-0.7f, -0.5f, -1.6f, -0.9f, -2.5f, -1.0f)
            curveToRelative(0.0f, 0.0f, 0.0f, -0.1f, 0.0f, -0.1f)
            verticalLineTo(4.0f)
            curveToRelative(0.0f, -0.6f, -0.4f, -1.0f, -1.0f, -1.0f)
            reflectiveCurveToRelative(-1.0f, 0.4f, -1.0f, 1.0f)
            verticalLineToRelative(2.0f)
            curveToRelative(0.0f, 0.0f, 0.0f, 0.1f, 0.0f, 0.1f)
            curveToRelative(-0.9f, 0.2f, -1.8f, 0.5f, -2.5f, 1.0f)
            curveToRelative(0.0f, 0.0f, 0.0f, -0.1f, -0.1f, -0.1f)
            lineTo(7.1f, 5.6f)
            curveToRelative(-0.4f, -0.4f, -1.0f, -0.4f, -1.4f, 0.0f)
            reflectiveCurveToRelative(-0.4f, 1.0f, 0.0f, 1.4f)
            lineToRelative(1.4f, 1.4f)
            curveToRelative(0.0f, 0.0f, 0.1f, 0.0f, 0.1f, 0.1f)
            curveToRelative(-0.5f, 0.7f, -0.9f, 1.6f, -1.0f, 2.5f)
            curveToRelative(0.0f, 0.0f, -0.1f, 0.0f, -0.1f, 0.0f)
            horizontalLineTo(4.0f)
            curveToRelative(-0.6f, 0.0f, -1.0f, 0.4f, -1.0f, 1.0f)
            reflectiveCurveToRelative(0.4f, 1.0f, 1.0f, 1.0f)
            horizontalLineToRelative(2.0f)
            curveToRelative(0.0f, 0.0f, 0.1f, 0.0f, 0.1f, 0.0f)
            curveToRelative(0.2f, 0.9f, 0.5f, 1.8f, 1.0f, 2.5f)
            curveToRelative(0.0f, 0.0f, -0.1f, 0.0f, -0.1f, 0.1f)
            lineToRelative(-1.4f, 1.4f)
            curveToRelative(-0.4f, 0.4f, -0.4f, 1.0f, 0.0f, 1.4f)
            curveToRelative(0.2f, 0.2f, 0.5f, 0.3f, 0.7f, 0.3f)
            reflectiveCurveToRelative(0.5f, -0.1f, 0.7f, -0.3f)
            lineToRelative(1.4f, -1.4f)
            curveToRelative(0.0f, 0.0f, 0.0f, -0.1f, 0.1f, -0.1f)
            curveToRelative(0.7f, 0.5f, 1.6f, 0.9f, 2.5f, 1.0f)
            curveToRelative(0.0f, 0.0f, 0.0f, 0.1f, 0.0f, 0.1f)
            verticalLineToRelative(2.0f)
            curveToRelative(0.0f, 0.6f, 0.4f, 1.0f, 1.0f, 1.0f)
            reflectiveCurveToRelative(1.0f, -0.4f, 1.0f, -1.0f)
            verticalLineToRelative(-2.0f)
            curveToRelative(0.0f, 0.0f, 0.0f, -0.1f, 0.0f, -0.1f)
            curveToRelative(0.9f, -0.2f, 1.8f, -0.5f, 2.5f, -1.0f)
            curveToRelative(0.0f, 0.0f, 0.0f, 0.1f, 0.1f, 0.1f)
            lineToRelative(1.4f, 1.4f)
            curveToRelative(0.2f, 0.2f, 0.5f, 0.3f, 0.7f, 0.3f)
            reflectiveCurveToRelative(0.5f, -0.1f, 0.7f, -0.3f)
            curveToRelative(0.4f, -0.4f, 0.4f, -1.0f, 0.0f, -1.4f)
            lineToRelative(-1.4f, -1.4f)
            curveToRelative(0.0f, 0.0f, -0.1f, 0.0f, -0.1f, -0.1f)
            curveToRelative(0.5f, -0.7f, 0.9f, -1.6f, 1.0f, -2.5f)
            curveToRelative(0.0f, 0.0f, 0.1f, 0.0f, 0.1f, 0.0f)
            horizontalLineToRelative(2.0f)
            curveToRelative(0.6f, 0.0f, 1.0f, -0.4f, 1.0f, -1.0f)
            reflectiveCurveTo(20.6f, 11.0f, 20.0f, 11.0f)
            close()
            moveTo(12.0f, 16.0f)
            curveToRelative(-2.2f, 0.0f, -4.0f, -1.8f, -4.0f, -4.0f)
            reflectiveCurveToRelative(1.8f, -4.0f, 4.0f, -4.0f)
            reflectiveCurveToRelative(4.0f, 1.8f, 4.0f, 4.0f)
            reflectiveCurveTo(14.2f, 16.0f, 12.0f, 16.0f)
            close()
        }
    }.build()

    // Icon: Kış teması (wintertheme.svg)
    val WinterTheme: ImageVector = ImageVector.Builder(
        name = "WinterTheme",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2.0f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(12.0f, 9.0f)
            verticalLineTo(5.0f)
            moveTo(9.88f, 9.88f)
            lineTo(7.05f, 7.05f)
            moveToRelative(2.0f, 5.0f)
            horizontalLineTo(5.0f)
            moveToRelative(4.88f, 2.12f)
            lineTo(7.05f, 17.0f)
            moveTo(12.0f, 19.0f)
            verticalLineTo(15.0f)
            moveTo(17.0f, 17.0f)
            lineTo(14.12f, 14.12f)
            moveTo(19.0f, 12.0f)
            horizontalLineTo(15.0f)
            moveTo(17.0f, 7.05f)
            lineTo(14.12f, 9.88f)
            moveTo(12.0f, 9.0f)
            arcToRelative(3.0f, 3.0f, 0.0f, true, false, 3.0f, 3.0f)
            arcTo(3.0f, 3.0f, 0.0f, false, false, 12.0f, 9.0f)
            close()
            moveTo(14.0f, 3.0f)
            lineTo(12.0f, 5.0f)
            lineTo(10.0f, 3.0f)
            moveTo(4.22f, 7.05f)
            horizontalLineTo(7.05f)
            verticalLineTo(4.22f)
            moveTo(3.0f, 14.0f)
            lineToRelative(2.0f, -2.0f)
            lineTo(3.0f, 10.0f)
            moveToRelative(4.05f, 9.78f)
            verticalLineTo(17.0f)
            horizontalLineTo(4.22f)
            moveTo(14.0f, 21.0f)
            lineToRelative(-2.0f, -2.0f)
            lineToRelative(-2.0f, 2.0f)
            moveTo(19.78f, 17.0f)
            horizontalLineTo(17.0f)
            verticalLineToRelative(2.83f)
            moveTo(21.0f, 10.0f)
            lineToRelative(-2.0f, 2.0f)
            lineToRelative(2.0f, 2.0f)
            moveTo(17.0f, 4.22f)
            verticalLineTo(7.05f)
            horizontalLineToRelative(2.83f)
        }
    }.build()

    // Icon: Sonbahar teması (autumntheme.svg)
    val AutumnTheme: ImageVector = ImageVector.Builder(
        name = "AutumnTheme",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 13.0f,
        viewportHeight = 19.0f
    ).apply {
        path(
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(6.942f, 8.426f)
            arcToRelative(0.554f, 0.554f, 0.0f, false, false, -0.784f, -0.784f)
            lineToRelative(-4.373f, 4.374f)
            curveTo(0.543f, 10.169f, 0.7f, 7.695f, 3.048f, 5.346f)
            curveTo(5.747f, 2.649f, 11.732f, 3.0f, 11.732f, 3.0f)
            arcToRelative(0.549f, 0.549f, 0.0f, false, true, 0.502f, 0.501f)
            reflectiveCurveToRelative(0.35f, 5.985f, -2.349f, 8.684f)
            curveToRelative(-2.276f, 2.277f, -4.672f, 2.494f, -6.499f, 1.373f)
            lineToRelative(-1.688f, 1.688f)
            arcToRelative(0.554f, 0.554f, 0.0f, false, true, -0.784f, -0.784f)
            close()
        }
    }.build()

    // Icon: İlkbahar teması (springtheme.svg)
    val SpringTheme: ImageVector = ImageVector.Builder(
        name = "SpringTheme",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 17.0f,
        viewportHeight = 19.0f
    ).apply {
        path(
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(14.12f, 11.403f)
            arcToRelative(3.51f, 3.51f, 0.0f, false, true, -5.62f, 4.08f)
            arcToRelative(3.51f, 3.51f, 0.0f, false, true, -5.62f, -4.08f)
            arcToRelative(3.51f, 3.51f, 0.0f, false, true, 2.147f, -6.603f)
            arcToRelative(3.51f, 3.51f, 0.0f, false, true, 6.943f, 0.0f)
            arcToRelative(3.51f, 3.51f, 0.0f, false, true, 2.147f, 6.603f)
            close()
            moveTo(11.184f, 9.57f)
            arcToRelative(2.682f, 2.682f, 0.0f, true, false, -2.682f, 2.682f)
            arcToRelative(2.682f, 2.682f, 0.0f, false, false, 2.682f, -2.682f)
            close()
        }
    }.build()

    // Icon: Yeni not ekleme (yeninot.svg)
    val NewNote: ImageVector = ImageVector.Builder(
        name = "NewNote",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.5f,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(6.0f, 10.0f)
            horizontalLineToRelative(4.5f)
            curveToRelative(0.8284f, 0.0f, 1.5f, -0.67157f, 1.5f, -1.5f)
            verticalLineTo(4.0f)
        }
        path(
            fill = SolidColor(Color.Black),
            pathFillType = PathFillType.EvenOdd
        ) {
            moveTo(9.85355f, 3.73223f)
            curveTo(10.3224f, 3.26339f, 10.9583f, 3.0f, 11.6213f, 3.0f)
            horizontalLineTo(16.5f)
            curveTo(17.8807f, 3.0f, 19.0f, 4.11929f, 19.0f, 5.5f)
            verticalLineTo(18.5f)
            curveTo(19.0f, 19.8807f, 17.8807f, 21.0f, 16.5f, 21.0f)
            horizontalLineTo(7.5f)
            curveTo(6.11929f, 21.0f, 5.0f, 19.8807f, 5.0f, 18.5f)
            verticalLineTo(9.62132f)
            curveTo(5.0f, 8.95828f, 5.26339f, 8.3224f, 5.73223f, 7.85355f)
            lineTo(9.85355f, 3.73223f)
            close()
            moveTo(11.6213f, 5.0f)
            curveTo(11.4887f, 5.0f, 11.3615f, 5.05268f, 11.2678f, 5.14645f)
            lineTo(7.14645f, 9.26777f)
            curveTo(7.05268f, 9.36154f, 7.0f, 9.48871f, 7.0f, 9.62132f)
            verticalLineTo(18.5f)
            curveTo(7.0f, 18.7761f, 7.22386f, 19.0f, 7.5f, 19.0f)
            horizontalLineTo(16.5f)
            curveTo(16.7761f, 19.0f, 17.0f, 18.7761f, 17.0f, 18.5f)
            verticalLineTo(5.5f)
            curveTo(17.0f, 5.22386f, 16.7761f, 5.0f, 16.5f, 5.0f)
            horizontalLineTo(11.6213f)
            close()
        }
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(10.0f, 14.5f)
            horizontalLineTo(14.0f)
            moveTo(12.0f, 12.5f)
            verticalLineTo(16.5f)
        }
    }.build()

    // Icon: Düzenle (düzenle-yaz-değiştir.svg)
    val Edit: ImageVector = ImageVector.Builder(
        name = "Edit",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2.0f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(18.0f, 9.99982f)
            lineTo(14.0f, 5.99982f)
            moveTo(2.5f, 21.4998f)
            lineTo(5.88437f, 21.1238f)
            curveTo(6.29786f, 21.0778f, 6.5046f, 21.0549f, 6.69785f, 20.9923f)
            curveTo(6.86929f, 20.9368f, 7.03245f, 20.8584f, 7.18289f, 20.7592f)
            curveTo(7.35245f, 20.6474f, 7.49955f, 20.5003f, 7.79373f, 20.2061f)
            lineTo(21.0f, 6.99982f)
            curveTo(22.1046f, 5.89525f, 22.1046f, 4.10438f, 21.0f, 2.99981f)
            curveTo(19.8955f, 1.89525f, 18.1046f, 1.89524f, 17.0f, 2.99981f)
            lineTo(3.79373f, 16.2061f)
            curveTo(3.49955f, 16.5003f, 3.35246f, 16.6474f, 3.24064f, 16.8169f)
            curveTo(3.14143f, 16.9674f, 3.06301f, 17.1305f, 3.00751f, 17.302f)
            curveTo(2.94496f, 17.4952f, 2.92198f, 17.702f, 2.87604f, 18.1155f)
            lineTo(2.5f, 21.4998f)
            close()
        }
    }.build()

    // Icon: Ayarlar (settings.svg)
    val Settings: ImageVector = ImageVector.Builder(
        name = "Settings",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 24.0f,
        viewportHeight = 24.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 2.0f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(20.3499f, 8.92293f)
            lineTo(19.9837f, 8.7192f)
            curveTo(19.9269f, 8.68756f, 19.8989f, 8.67169f, 19.8714f, 8.65524f)
            curveTo(19.5983f, 8.49165f, 19.3682f, 8.26564f, 19.2002f, 7.99523f)
            curveTo(19.1833f, 7.96802f, 19.1674f, 7.93949f, 19.1348f, 7.8831f)
            curveTo(19.1023f, 7.82677f, 19.0858f, 7.79823f, 19.0706f, 7.76998f)
            curveTo(18.92f, 7.48866f, 18.8385f, 7.17515f, 18.8336f, 6.85606f)
            curveTo(18.8331f, 6.82398f, 18.8332f, 6.79121f, 18.8343f, 6.72604f)
            lineTo(18.8415f, 6.30078f)
            curveTo(18.8529f, 5.62025f, 18.8587f, 5.27894f, 18.763f, 4.97262f)
            curveTo(18.6781f, 4.70053f, 18.536f, 4.44993f, 18.3462f, 4.23725f)
            curveTo(18.1317f, 3.99685f, 17.8347f, 3.82534f, 17.2402f, 3.48276f)
            lineTo(16.7464f, 3.1982f)
            curveTo(16.1536f, 2.85658f, 15.8571f, 2.68571f, 15.5423f, 2.62057f)
            curveTo(15.2639f, 2.56294f, 14.9765f, 2.56561f, 14.6991f, 2.62789f)
            curveTo(14.3859f, 2.69819f, 14.0931f, 2.87351f, 13.5079f, 3.22396f)
            lineTo(13.5045f, 3.22555f)
            lineTo(13.1507f, 3.43741f)
            curveTo(13.0948f, 3.47091f, 13.0665f, 3.48779f, 13.0384f, 3.50338f)
            curveTo(12.7601f, 3.6581f, 12.4495f, 3.74365f, 12.1312f, 3.75387f)
            curveTo(12.0992f, 3.7549f, 12.0665f, 3.7549f, 12.0013f, 3.7549f)
            curveTo(11.9365f, 3.7549f, 11.9024f, 3.7549f, 11.8704f, 3.75387f)
            curveTo(11.5515f, 3.74361f, 11.2402f, 3.65759f, 10.9615f, 3.50224f)
            curveTo(10.9334f, 3.48658f, 10.9056f, 3.46956f, 10.8496f, 3.4359f)
            lineTo(10.4935f, 3.22213f)
            curveTo(9.90422f, 2.86836f, 9.60915f, 2.69121f, 9.29427f, 2.62057f)
            curveTo(9.0157f, 2.55807f, 8.72737f, 2.55634f, 8.44791f, 2.61471f)
            curveTo(8.13236f, 2.68062f, 7.83577f, 2.85276f, 7.24258f, 3.19703f)
            lineTo(7.23994f, 3.1982f)
            lineTo(6.75228f, 3.48124f)
            lineTo(6.74688f, 3.48454f)
            curveTo(6.15904f, 3.82572f, 5.86441f, 3.99672f, 5.6517f, 4.23614f)
            curveTo(5.46294f, 4.4486f, 5.32185f, 4.69881f, 5.2374f, 4.97018f)
            curveTo(5.14194f, 5.27691f, 5.14703f, 5.61896f, 5.15853f, 6.3027f)
            lineTo(5.16568f, 6.72736f)
            curveTo(5.16676f, 6.79166f, 5.16864f, 6.82362f, 5.16817f, 6.85525f)
            curveTo(5.16343f, 7.17499f, 5.08086f, 7.48914f, 4.92974f, 7.77096f)
            curveTo(4.9148f, 7.79883f, 4.8987f, 7.8267f, 4.86654f, 7.88237f)
            curveTo(4.83436f, 7.93809f, 4.81877f, 7.96579f, 4.80209f, 7.99268f)
            curveTo(4.63336f, 8.26452f, 4.40214f, 8.49186f, 4.12733f, 8.65572f)
            curveTo(4.10015f, 8.67193f, 4.0715f, 8.68752f, 4.01521f, 8.71871f)
            lineTo(3.65365f, 8.91908f)
            curveTo(3.05208f, 9.25245f, 2.75137f, 9.41928f, 2.53256f, 9.65669f)
            curveTo(2.33898f, 9.86672f, 2.19275f, 10.1158f, 2.10349f, 10.3872f)
            curveTo(2.00259f, 10.6939f, 2.00267f, 11.0378f, 2.00424f, 11.7255f)
            lineTo(2.00551f, 12.2877f)
            curveTo(2.00706f, 12.9708f, 2.00919f, 13.3122f, 2.11032f, 13.6168f)
            curveTo(2.19979f, 13.8863f, 2.34495f, 14.134f, 2.53744f, 14.3427f)
            curveTo(2.75502f, 14.5787f, 3.05274f, 14.7445f, 3.64974f, 15.0766f)
            lineTo(4.00808f, 15.276f)
            curveTo(4.06907f, 15.3099f, 4.09976f, 15.3266f, 4.12917f, 15.3444f)
            curveTo(4.40148f, 15.5083f, 4.63089f, 15.735f, 4.79818f, 16.0053f)
            curveTo(4.81625f, 16.0345f, 4.8336f, 16.0648f, 4.8683f, 16.1255f)
            curveTo(4.90256f, 16.1853f, 4.92009f, 16.2152f, 4.93594f, 16.2452f)
            curveTo(5.08261f, 16.5229f, 5.16114f, 16.8315f, 5.16649f, 17.1455f)
            curveTo(5.16707f, 17.1794f, 5.16658f, 17.2137f, 5.16541f, 17.2827f)
            lineTo(5.15853f, 17.6902f)
            curveTo(5.14695f, 18.3763f, 5.1419f, 18.7197f, 5.23792f, 19.0273f)
            curveTo(5.32287f, 19.2994f, 5.46484f, 19.55f, 5.65463f, 19.7627f)
            curveTo(5.86915f, 20.0031f, 6.16655f, 20.1745f, 6.76107f, 20.5171f)
            lineTo(7.25478f, 20.8015f)
            curveTo(7.84763f, 21.1432f, 8.14395f, 21.3138f, 8.45869f, 21.379f)
            curveTo(8.73714f, 21.4366f, 9.02464f, 21.4344f, 9.30209f, 21.3721f)
            curveTo(9.61567f, 21.3017f, 9.90948f, 21.1258f, 10.4964f, 20.7743f)
            lineTo(10.8502f, 20.5625f)
            curveTo(10.9062f, 20.5289f, 10.9346f, 20.5121f, 10.9626f, 20.4965f)
            curveTo(11.2409f, 20.3418f, 11.5512f, 20.2558f, 11.8695f, 20.2456f)
            curveTo(11.9015f, 20.2446f, 11.9342f, 20.2446f, 11.9994f, 20.2446f)
            curveTo(12.0648f, 20.2446f, 12.0974f, 20.2446f, 12.1295f, 20.2456f)
            curveTo(12.4484f, 20.2559f, 12.7607f, 20.3422f, 13.0394f, 20.4975f)
            curveTo(13.0639f, 20.5112f, 13.0885f, 20.526f, 13.1316f, 20.5519f)
            lineTo(13.5078f, 20.7777f)
            curveTo(14.0971f, 21.1315f, 14.3916f, 21.3081f, 14.7065f, 21.3788f)
            curveTo(14.985f, 21.4413f, 15.2736f, 21.4438f, 15.5531f, 21.3855f)
            curveTo(15.8685f, 21.3196f, 16.1657f, 21.1471f, 16.7586f, 20.803f)
            lineTo(17.2536f, 20.5157f)
            curveTo(17.8418f, 20.1743f, 18.1367f, 20.0031f, 18.3495f, 19.7636f)
            curveTo(18.5383f, 19.5512f, 18.6796f, 19.3011f, 18.764f, 19.0297f)
            curveTo(18.8588f, 18.7252f, 18.8531f, 18.3858f, 18.8417f, 17.7119f)
            lineTo(18.8343f, 17.2724f)
            curveTo(18.8332f, 17.2081f, 18.8331f, 17.1761f, 18.8336f, 17.1445f)
            curveTo(18.8383f, 16.8247f, 18.9195f, 16.5104f, 19.0706f, 16.2286f)
            curveTo(19.0856f, 16.2007f, 19.1018f, 16.1726f, 19.1338f, 16.1171f)
            curveTo(19.166f, 16.0615f, 19.1827f, 16.0337f, 19.1994f, 16.0068f)
            curveTo(19.3681f, 15.7349f, 19.5995f, 15.5074f, 19.8744f, 15.3435f)
            curveTo(19.9012f, 15.3275f, 19.9289f, 15.3122f, 19.9838f, 15.2818f)
            lineTo(19.9857f, 15.2809f)
            lineTo(20.3472f, 15.0805f)
            curveTo(20.9488f, 14.7472f, 21.2501f, 14.5801f, 21.4689f, 14.3427f)
            curveTo(21.6625f, 14.1327f, 21.8085f, 13.8839f, 21.8978f, 13.6126f)
            curveTo(21.9981f, 13.3077f, 21.9973f, 12.9658f, 21.9958f, 12.2861f)
            lineTo(21.9945f, 11.7119f)
            curveTo(21.9929f, 11.0287f, 21.9921f, 10.6874f, 21.891f, 10.3828f)
            curveTo(21.8015f, 10.1133f, 21.6555f, 9.86561f, 21.463f, 9.65685f)
            curveTo(21.2457f, 9.42111f, 20.9475f, 9.25526f, 20.3517f, 8.92378f)
            lineTo(20.3499f, 8.92293f)
            close()
            moveTo(8.00033f, 12.0f)
            curveTo(8.00033f, 14.2091f, 9.79119f, 16.0f, 12.0003f, 16.0f)
            curveTo(14.2095f, 16.0f, 16.0003f, 14.2091f, 16.0003f, 12.0f)
            curveTo(16.0003f, 9.79082f, 14.2095f, 7.99996f, 12.0003f, 7.99996f)
            curveTo(9.79119f, 7.99996f, 8.00033f, 9.79082f, 8.00033f, 12.0f)
            close()
        }
    }.build()

    // Icon: Yıldız (Star) - star-svgrepo-com.svg
    val Star: ImageVector = ImageVector.Builder(
        name = "Star",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 25.0f,
        viewportHeight = 25.0f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(12.71f, 3.45f)
            lineTo(15.17f, 7.94f)
            lineTo(20.73f, 9.3f)
            lineTo(17.66f, 14.38f)
            lineTo(18.09f, 20.12f)
            lineTo(12.32f, 18.77f)
            lineTo(6.99f, 20.95f)
            lineTo(6.49f, 15.04f)
            lineTo(2.77f, 10.65f)
            lineTo(8.21f, 8.35f)
            lineTo(11.28f, 3.45f)
            close()
        }
    }.build()

    // Icon: Yıldız dolu (Star Filled) - sarı dolu yıldız
    val StarFilled: ImageVector = ImageVector.Builder(
        name = "StarFilled",
        defaultWidth = 24.0.dp,
        defaultHeight = 24.0.dp,
        viewportWidth = 25.0f,
        viewportHeight = 25.0f
    ).apply {
        path(
            fill = SolidColor(Color(0xFFFFD700)), // Altın sarısı
            pathFillType = PathFillType.EvenOdd
        ) {
            moveTo(12.71f, 3.45f)
            lineTo(15.17f, 7.94f)
            lineTo(20.73f, 9.3f)
            lineTo(17.66f, 14.38f)
            lineTo(18.09f, 20.12f)
            lineTo(12.32f, 18.77f)
            lineTo(6.99f, 20.95f)
            lineTo(6.49f, 15.04f)
            lineTo(2.77f, 10.65f)
            lineTo(8.21f, 8.35f)
            lineTo(11.28f, 3.45f)
            close()
        }
        path(
            fill = null,
            stroke = SolidColor(Color(0xFFFFA500)), // Turuncu kenarlık
            strokeLineWidth = 1.5f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
            pathFillType = PathFillType.NonZero
        ) {
            moveTo(12.71f, 3.45f)
            lineTo(15.17f, 7.94f)
            lineTo(20.73f, 9.3f)
            lineTo(17.66f, 14.38f)
            lineTo(18.09f, 20.12f)
            lineTo(12.32f, 18.77f)
            lineTo(6.99f, 20.95f)
            lineTo(6.49f, 15.04f)
            lineTo(2.77f, 10.65f)
            lineTo(8.21f, 8.35f)
            lineTo(11.28f, 3.45f)
            close()
        }
    }.build()
}

