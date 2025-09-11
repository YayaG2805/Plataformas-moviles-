package com.example.examen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "pantalla1") {

                    // Pantalla 1
                    composable("pantalla1") {
                        Pantalla1 { oper ->
                            navController.navigate("pantalla2/$oper")
                        }
                    }

                    // Pantalla 2
                    composable(
                        route = "pantalla2/{operacion}",
                        arguments = listOf(navArgument("operacion") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val operacion = backStackEntry.arguments?.getString("operacion") ?: "suma"
                        Pantalla2(operacion = operacion)
                    }
                }
            }
        }
    }
}

@Composable
fun Pantalla1(onOperacion: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onOperacion("suma") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("SUMA") }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { onOperacion("resta") },
            modifier = Modifier.fillMaxWidth()
        ) { Text("RESTA") }

        Spacer(Modifier.height(40.dp))
        Text("Diego Guevara 24128")
    }
}

@Composable
fun Pantalla2(operacion: String) {
    var num1 by remember { mutableStateOf("") }
    var num2 by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Operación: ${operacion.uppercase()}",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = num1,
            onValueChange = { num1 = it },
            label = { Text("Número 1") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = num2,
            onValueChange = { num2 = it },
            label = { Text("Número 2") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                val n1 = num1.toIntOrNull() ?: 0
                val n2 = num2.toIntOrNull() ?: 0
                resultado = if (operacion == "suma") (n1 + n2).toString() else (n1 - n2).toString()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("OPERAR")
        }

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Resultado: ${resultado ?: "-"}",
            style = MaterialTheme.typography.titleMedium
        )
    }
}
