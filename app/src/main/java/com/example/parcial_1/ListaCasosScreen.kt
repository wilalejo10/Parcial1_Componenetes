package com.example.parcial_1

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ListaCasosScreen(
    casoDao: CasoDao,
    onCasoClick: (Int) -> Unit,
    onNuevoCaso: () -> Unit
) {
    var casos by remember { mutableStateOf<List<Caso>>(emptyList()) }
    var queryBusqueda by remember { mutableStateOf("") }

    LaunchedEffect(queryBusqueda) {
        casos = if (queryBusqueda.isBlank()) {
            casoDao.obtenerCasos()
        } else {
            casoDao.buscarCasos(queryBusqueda)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {

        // Header oscuro con los detectives y texto encima
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.banner_detectives),
                contentDescription = "Equipo de detectives",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0x22000000), Color(0xCC0B1220))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Text(
                    text = "detective case 2026",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Hola, Detective",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = "Los detectives Anyi, Luisa y alejandro al mando",
                    color = Color(0xFFD8DCE6),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(text = "Mis casos", style = MaterialTheme.typography.headlineSmall)

            OutlinedTextField(
                value = queryBusqueda,
                onValueChange = { queryBusqueda = it },
                label = { Text("Buscar casos...") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = onNuevoCaso, modifier = Modifier.fillMaxWidth()) {
                Text("Nuevo caso")
            }

            Divider()

            if (casos.isEmpty()) {
                Text("No hay casos registrados.")
            } else {
                casos.forEach { caso ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onCasoClick(caso.id) },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Caso #${caso.id}", style = MaterialTheme.typography.labelMedium)
                                Text(caso.titulo, style = MaterialTheme.typography.bodyLarge)
                                Text(caso.fecha, style = MaterialTheme.typography.bodySmall)
                            }
                            EstadoPill(estado = caso.estado)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EstadoPill(estado: String) {
    val colorFondo = when (estado.lowercase()) {
        "cerrado" -> MaterialTheme.colorScheme.tertiaryContainer
        "suspendido" -> MaterialTheme.colorScheme.errorContainer
        else -> MaterialTheme.colorScheme.primaryContainer
    }
    val colorTexto = when (estado.lowercase()) {
        "cerrado" -> MaterialTheme.colorScheme.onTertiaryContainer
        "suspendido" -> MaterialTheme.colorScheme.onErrorContainer
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    Box(
        modifier = Modifier
            .background(color = colorFondo, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text = estado, color = colorTexto, style = MaterialTheme.typography.labelMedium)
    }
}