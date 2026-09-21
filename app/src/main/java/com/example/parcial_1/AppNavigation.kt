package com.example.parcial_1

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun AppNavigation(
    casoDao: CasoDao,
    hallazgoDao: HallazgoDao,
    cierreDao: CierreDao
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = CaseTrackRoutes.LISTA) {

        composable(CaseTrackRoutes.LISTA) {
            ListaCasosScreen(
                casoDao = casoDao,
                onCasoClick = { id -> navController.navigate(CaseTrackRoutes.detalle(id)) },
                onNuevoCaso = { navController.navigate(CaseTrackRoutes.CREAR) }
            )
        }

        composable(CaseTrackRoutes.CREAR) {
            CrearEditarCasoScreen(
                casoId = null,
                casoDao = casoDao,
                onGuardado = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }

        composable(
            route = CaseTrackRoutes.EDITAR,
            arguments = listOf(navArgument("casoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val casoId = backStackEntry.arguments?.getInt("casoId")
            CrearEditarCasoScreen(
                casoId = casoId,
                casoDao = casoDao,
                onGuardado = { navController.popBackStack() },
                onCancelar = { navController.popBackStack() }
            )
        }

        composable(
            route = CaseTrackRoutes.DETALLE,
            arguments = listOf(navArgument("casoId") { type = NavType.IntType })
        ) { backStackEntry ->
            val casoId = backStackEntry.arguments?.getInt("casoId") ?: return@composable
            DetalleCasoScreen(
                casoId = casoId,
                casoDao = casoDao,
                hallazgoDao = hallazgoDao,
                cierreDao = cierreDao,
                onEditar = { navController.navigate(CaseTrackRoutes.editar(casoId)) },
                onVolver = { navController.popBackStack() }
            )
        }
    }
}