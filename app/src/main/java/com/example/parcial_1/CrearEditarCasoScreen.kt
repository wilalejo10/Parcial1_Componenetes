package com.example.parcial_1

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CrearEditarCasoScreen(
    casoId: Int?,
    casoDao: CasoDao,
    onGuardado: () -> Unit,
    onCancelar: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Abierto") }
    var casoOriginal by remember { mutableStateOf<Caso?>(null) }

    LaunchedEffect(casoId) {
        if (casoId != null) {
            val caso = casoDao.obtenerCasoPorId(casoId)
            casoOriginal = caso
            if (caso != null) {
                titulo = caso.titulo
                descripcion = caso.descripcion
                fecha = caso.fecha
                estado = caso.estado
            }
        }
    }

    fun camposSonValidos(): Boolean {
        if (titulo.isBlank() || descripcion.isBlank() || fecha.isBlank() || estado.isBlank()) {
            Toast.makeText(context, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return false
        }
        if (fecha.length < 10) {
            Toast.makeText(context, "Ingresa una fecha válida (DD/MM/AAAA)", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = if (casoId == null) "Crear caso" else "Editar caso",
            style = MaterialTheme.typography.headlineMedium
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción *") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = { input ->
                val numeros = input.filter { it.isDigit() }
                if (numeros.length <= 8) {
                    fecha = when {
                        numeros.length >= 5 -> "${numeros.substring(0, 2)}/${numeros.substring(2, 4)}/${numeros.substring(4)}"
                        numeros.length >= 3 -> "${numeros.substring(0, 2)}/${numeros.substring(2)}"
                        else -> numeros
                    }
                }
            },
            label = { Text("Fecha *") },
            placeholder = { Text("DD/MM/AAAA") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado,
            onValueChange = { estado = it },
            label = { Text("Estado *") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (camposSonValidos()) {
                    scope.launch {
                        if (casoOriginal != null) {
                            casoDao.actualizarCaso(
                                casoOriginal!!.copy(
                                    titulo = titulo,
                                    descripcion = descripcion,
                                    fecha = fecha,
                                    estado = estado
                                )
                            )
                        } else {
                            casoDao.insertarCaso(
                                Caso(titulo = titulo, descripcion = descripcion, fecha = fecha, estado = estado)
                            )
                        }
                        onGuardado()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (casoId == null) "Guardar caso" else "Guardar cambios")
        }

        OutlinedButton(onClick = onCancelar, modifier = Modifier.fillMaxWidth()) {
            Text("Cancelar")
        }
    }
}