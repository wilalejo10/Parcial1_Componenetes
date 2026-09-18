package com.example.parcial_1

import org.junit.Assert.assertEquals
import org.junit.Test

class CasoTest { // El nombre de la clase debe ser igual al del archivo (CasoTest)

    @Test
    fun crearCaso_debeGuardarLosDatosCorrectamente() {
        val caso = Caso(
            id = 1,
            titulo = "Robo en establecimiento",
            descripcion = "Se reportó un robo",
            fecha = "17/09/2026",
            estado = "Abierto"
        )

        assertEquals(1, caso.id)
        assertEquals("Robo en establecimiento", caso.titulo)
        assertEquals("Se reportó un robo", caso.descripcion)
        assertEquals("17/09/2026", caso.fecha)
        assertEquals("Abierto", caso.estado)
    }
}