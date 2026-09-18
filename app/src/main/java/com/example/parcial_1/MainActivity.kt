package com.example.parcial_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
    onVerCasos: () -> Unit,
    onSeleccionarCaso: (Int) -> Unit,
    onVolverLista: () -> Unit
) {

    var titulo by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
    }

    var fecha by remember {
        mutableStateOf("")
    }

    var estado by remember {
        mutableStateOf("Abierto")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text("Crear caso")

        OutlinedTextField(
            value = titulo,
            onValueChange = {
                titulo = it
            },
            label = {
                Text("Título")
            }
        )

        OutlinedTextField(
            value = descripcion,
            onValueChange = {
                descripcion = it
            },
            label = {
                Text("Descripción")
            }
        )

        OutlinedTextField(
            value = fecha,
            onValueChange = {
                fecha = it
            },
            label = {
                Text("Fecha")
            },
            placeholder = {
                Text("DD/MM/AAAA")
            }
        )

        OutlinedTextField(
            value = estado,
            onValueChange = {
                estado = it
            },
            label = {
                Text("Estado")
            }
        )

        Button(
            onClick = {

                onGuardarCaso(
                    titulo,
                    descripcion,
                    fecha,
                    estado
                )

                titulo = ""
                descripcion = ""
                fecha = ""
                estado = "Abierto"
            }
        ) {
            Text("Guardar caso")
        }

        Button(
            onClick = {
                onVerCasos()
            }
        ) {
            Text("Ver casos")
        }

        if (casoSeleccionado != null) {

            Text("Detalle del caso")

            Text("Título: ${casoSeleccionado.titulo}")

            Text("Descripción: ${casoSeleccionado.descripcion}")

            Text("Fecha: ${casoSeleccionado.fecha}")

            Text("Estado: ${casoSeleccionado.estado}")

            Button(
                onClick = {
                    onVolverLista()
                }
            ) {
                Text("Volver a la lista")
            }

        } else {

            if (casos.isEmpty()) {

                Text("No hay casos registrados.")

            } else {

                LazyColumn {

                    items(casos) { caso ->

                        Column(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onSeleccionarCaso(caso.id)
                                }
                        ) {

                            Text("ID: ${caso.id}")

                            Text("Título: ${caso.titulo}")
                        }
                    }
                }
            }
        }
    }
}
