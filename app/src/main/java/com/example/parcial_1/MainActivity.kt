package com.example.parcial_1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.parcial_1.ui.theme.Parcial_1Theme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)

        setContent {
            Parcial_1Theme {

                var casos by remember {
                    mutableStateOf<List<Caso>>(emptyList())
                }

                var casoSeleccionado by remember {
                    mutableStateOf<Caso?>(null)
                }

                CrearCasoScreen(
                    casos = casos,
                    casoSeleccionado = casoSeleccionado,

                    onGuardarCaso = { titulo, descripcion, fecha, estado ->
                        lifecycleScope.launch {
                            val caso = Caso(
                                titulo = titulo,
                                descripcion = descripcion,
                                fecha = fecha,
                                estado = estado
                            )
                            database.casoDao().insertarCaso(caso)
                            casos = database.casoDao().obtenerCasos()
                        }
                    },

                    onActualizarCaso = { caso ->
                        lifecycleScope.launch {
                            database.casoDao().actualizarCaso(caso)
                            casos = database.casoDao().obtenerCasos()
                            casoSeleccionado = null
                        }
                    },

                    onEliminarCaso = { caso ->
                        lifecycleScope.launch {
                            database.casoDao().eliminarCaso(caso)
                            casos = database.casoDao().obtenerCasos()
                            casoSeleccionado = null
                        }
                    },

                    onBuscarCasos = { query ->
                        lifecycleScope.launch {
                            casos = database.casoDao().buscarCasos(query)
                        }
                    },

                    onVerCasos = {
                        lifecycleScope.launch {
                            casos = database.casoDao().obtenerCasos()
                            casoSeleccionado = null
                        }
                    },

                    onSeleccionarCaso = { id ->
                        lifecycleScope.launch {
                            casoSeleccionado =
                                database.casoDao().obtenerCasoPorId(id)
                        }
                    },

                    onVolverLista = {
                        casoSeleccionado = null
                    }
                )
            }
        }
    }
}

@Composable
fun CrearCasoScreen(
    casos: List<Caso>,
    casoSeleccionado: Caso?,
    onGuardarCaso: (String, String, String, String) -> Unit,
    onActualizarCaso: (Caso) -> Unit,
    onEliminarCaso: (Caso) -> Unit,
    onBuscarCasos: (String) -> Unit,
    onVerCasos: () -> Unit,
    onSeleccionarCaso: (Int) -> Unit,
    onVolverLista: () -> Unit
) {
    val context = LocalContext.current

    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("Abierto") }
    var queryBusqueda by remember { mutableStateOf("") }
    var modoEdicion by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    LaunchedEffect(casoSeleccionado) {
        if (casoSeleccionado != null) {
            titulo = casoSeleccionado.titulo
            descripcion = casoSeleccionado.descripcion
            fecha = casoSeleccionado.fecha
            estado = casoSeleccionado.estado
        } else {
            titulo = ""
            descripcion = ""
            fecha = ""
            estado = "Abierto"
            modoEdicion = false
        }
    }

    // Función auxiliar para validar campos vacíos
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
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = if (modoEdicion) "Editar caso" else "Crear caso",
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

        // Campo de Fecha configurado para solo aceptar números y aplicar la máscara DD/MM/AAAA
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

        if (modoEdicion && casoSeleccionado != null) {
            Button(
                onClick = {
                    if (camposSonValidos()) {
                        val casoActualizado = casoSeleccionado.copy(
                            titulo = titulo,
                            descripcion = descripcion,
                            fecha = fecha,
                            estado = estado
                        )
                        onActualizarCaso(casoActualizado)
                        modoEdicion = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cambios")
            }
        } else {
            Button(
                onClick = {
                    if (camposSonValidos()) {
                        onGuardarCaso(titulo, descripcion, fecha, estado)
                        titulo = ""
                        descripcion = ""
                        fecha = ""
                        estado = "Abierto"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar caso")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Búsqueda de casos
        OutlinedTextField(
            value = queryBusqueda,
            onValueChange = {
                queryBusqueda = it
                onBuscarCasos(it)
            },
            label = { Text("Buscar casos...") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = { onVerCasos() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver todos los casos")
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        // Detalle / Lista
        if (casoSeleccionado != null) {
            Text(
                text = "Detalle del caso",
                style = MaterialTheme.typography.titleLarge
            )
            Text("Título: ${casoSeleccionado.titulo}")
            Text("Descripción: ${casoSeleccionado.descripcion}")
            Text("Fecha: ${casoSeleccionado.fecha}")
            Text("Estado: ${casoSeleccionado.estado}")

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { modoEdicion = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Editar")
                }

                Button(
                    onClick = { onEliminarCaso(casoSeleccionado) },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar")
                }

                OutlinedButton(
                    onClick = { onVolverLista() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver a la lista")
                }
            }
        } else {
            if (casos.isEmpty()) {
                Text("No hay casos registrados.")
            } else {
                casos.forEach { caso ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onSeleccionarCaso(caso.id) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("ID: ${caso.id}", style = MaterialTheme.typography.labelMedium)
                            Text("Título: ${caso.titulo}", style = MaterialTheme.typography.bodyLarge)
                            Text("Estado: ${caso.estado}", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        }
    }
}