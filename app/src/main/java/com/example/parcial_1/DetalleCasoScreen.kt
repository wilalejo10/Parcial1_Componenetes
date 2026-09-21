package com.example.parcial_1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetalleCasoScreen(
    casoId: Int,
    casoDao: CasoDao,
    hallazgoDao: HallazgoDao,
    cierreDao: CierreDao,
    onEditar: () -> Unit,
    onVolver: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var caso by remember { mutableStateOf<Caso?>(null) }
    var hallazgos by remember { mutableStateOf<List<Hallazgo>>(emptyList()) }
    var cierreExistente by remember { mutableStateOf<Cierre?>(null) }
    var vistaActual by remember { mutableStateOf("resumen") }

    var descripcionHallazgo by remember { mutableStateOf("") }
    var tipoHallazgo by remember { mutableStateOf("Hallazgo") }
    var motivoCierre by remember { mutableStateOf("") }

    suspend fun cargarTodo() {
        caso = casoDao.obtenerCasoPorId(casoId)
        hallazgos = hallazgoDao.obtenerHallazgosPorCaso(casoId)
        cierreExistente = cierreDao.obtenerCierrePorCaso(casoId)
    }

    LaunchedEffect(casoId) { cargarTodo() }

    val casoActual = caso

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(text = casoActual?.titulo ?: "Cargando...", style = MaterialTheme.typography.headlineMedium)

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { vistaActual = "resumen" }, modifier = Modifier.weight(1f)) {
                Text("Resumen")
            }
            Button(onClick = { vistaActual = "hallazgos" }, modifier = Modifier.weight(1f)) {
                Text("Hallazgos")
            }
            Button(onClick = { vistaActual = "cierre" }, modifier = Modifier.weight(1f)) {
                Text("Cierre")
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        if (casoActual != null && vistaActual == "resumen") {
            Text("Caso #${casoActual.id}")
            Text("Descripción: ${casoActual.descripcion}")
            Text("Fecha: ${casoActual.fecha}")
            Text("Estado: ${casoActual.estado}")
        }

        if (vistaActual == "hallazgos") {
            Text("Registrar hallazgo o evidencia", style = MaterialTheme.typography.titleMedium)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { tipoHallazgo = "Hallazgo" }, modifier = Modifier.weight(1f)) {
                    Text("Hallazgo")
                }
                Button(onClick = { tipoHallazgo = "Evidencia" }, modifier = Modifier.weight(1f)) {
                    Text("Evidencia")
                }
            }
            Text("Tipo seleccionado: $tipoHallazgo")

            OutlinedTextField(
                value = descripcionHallazgo,
                onValueChange = { descripcionHallazgo = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (descripcionHallazgo.isNotBlank()) {
                        scope.launch {
                            val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                            hallazgoDao.insertarHallazgo(
                                Hallazgo(
                                    casoId = casoId,
                                    tipo = tipoHallazgo,
                                    descripcion = descripcionHallazgo,
                                    fecha = fecha
                                )
                            )
                            descripcionHallazgo = ""
                            cargarTodo()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar")
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            if (hallazgos.isEmpty()) {
                Text("Todavía no hay hallazgos ni evidencias.")
            } else {
                hallazgos.forEach { h ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("${h.tipo} - ${h.fecha}", style = MaterialTheme.typography.labelMedium)
                            Text(h.descripcion)
                        }
                    }
                }
            }
        }

        if (casoActual != null && vistaActual == "cierre") {
            if (casoActual.estado.equals("Cerrado", ignoreCase = true) && cierreExistente != null) {
                Text("Este caso ya está cerrado.")
                Text("Fecha de cierre: ${cierreExistente!!.fechaCierre}")
                Text("Motivo: ${cierreExistente!!.motivo}")
            } else {
                Text("Cerrar este caso", style = MaterialTheme.typography.titleMedium)
                OutlinedTextField(
                    value = motivoCierre,
                    onValueChange = { motivoCierre = it },
                    label = { Text("Motivo del cierre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (motivoCierre.isNotBlank()) {
                            scope.launch {
                                val fecha = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
                                casoDao.actualizarCaso(casoActual.copy(estado = "Cerrado"))
                                cierreDao.insertarCierre(
                                    Cierre(casoId = casoId, fechaCierre = fecha, motivo = motivoCierre)
                                )
                                cargarTodo()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Confirmar cierre")
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onEditar, modifier = Modifier.weight(1f)) {
                Text("Editar caso")
            }
            OutlinedButton(onClick = onVolver, modifier = Modifier.weight(1f)) {
                Text("Volver")
            }
        }
    }
}
