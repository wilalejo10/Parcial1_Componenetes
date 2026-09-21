package com.example.parcial_1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AzulAccento = Color(0xFF2F6FED)
private val AzulOscuro = Color(0xFF1B2A4A)
private val FondoClaro = Color(0xFFFFFFFF)
private val FondoCampo = Color(0xFFF1F2F6)
private val TextoGris = Color(0xFF6B7280)
private val TextoOscuro = Color(0xFF1B1B1F)

private val AmbarClaro = Color(0xFFFFE9B0)
private val AmbarTexto = Color(0xFF8A5A00)
private val VerdeClaro = Color(0xFFD9F2E3)
private val VerdeTexto = Color(0xFF16794C)
private val RojoClaro = Color(0xFFFBD9D9)
private val RojoTexto = Color(0xFFB3261E)

private val CaseTrackColorScheme = lightColorScheme(
    primary = AzulAccento,
    onPrimary = Color.White,
    secondary = AzulOscuro,
    onSecondary = Color.White,
    background = FondoClaro,
    onBackground = TextoOscuro,
    surface = FondoClaro,
    onSurface = TextoOscuro,
    surfaceVariant = FondoCampo,
    onSurfaceVariant = TextoGris,
    // Colores de las pastillas de estado (EstadoPill)
    primaryContainer = AmbarClaro,
    onPrimaryContainer = AmbarTexto,
    tertiaryContainer = VerdeClaro,
    onTertiaryContainer = VerdeTexto,
    errorContainer = RojoClaro,
    onErrorContainer = RojoTexto
)

@Composable
fun CaseTrackNoirTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CaseTrackColorScheme,
        typography = Typography,
        content = content
    )
}