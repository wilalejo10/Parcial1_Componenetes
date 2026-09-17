package com.example.parcial_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
                CrearCasoScreen(
                    onGuardarCaso = { titulo, descripcion ->

                        lifecycleScope.launch {
                            val caso = Caso(
                                titulo = titulo,
                                descripcion = descripcion
                            )

                            database.casoDao().insertarCaso(caso)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CrearCasoScreen(
    onGuardarCaso: (String, String) -> Unit
) {

    var titulo by remember {
        mutableStateOf("")
    }

    var descripcion by remember {
        mutableStateOf("")
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

        Button(
            onClick = {
                onGuardarCaso(titulo, descripcion)

                titulo = ""
                descripcion = ""
            }
        ) {
            Text("Guardar caso")
        }
    }
}