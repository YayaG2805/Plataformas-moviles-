package com.example.lab6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab6.ui.theme.Lab6Theme

/*
 * Laboratorio 6 – Aplicación de Contador con Historial y Estadísticas
 * Autor: Diego Guevara – Carné 24128
 * Curso: Plataformas Móviles
 * Descripción: Este programa implementa un contador con botones para aumentar y disminuir,
 * mostrando estadísticas como incrementos, decrementos, valor máximo y mínimo.
 * Además incluye un historial visual de los cambios y un botón para reiniciar.
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Lab6Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ContadorApp()
                }
            }
        }
    }
}

@Composable
fun ContadorApp() {
    // Estado principal del contador y sus estadísticas
    var contador by remember { mutableStateOf(0) } // valor actual del contador
    var inc by remember { mutableStateOf(0) } // número de incrementos
    var dec by remember { mutableStateOf(0) } // número de decrementos
    var max by remember { mutableStateOf(0) } // valor máximo alcanzado
    var min by remember { mutableStateOf(0) } // valor mínimo alcanzado

    // Lista que guarda el historial de cambios (valor y si fue incremento/decremento)
    val historial = remember { mutableStateListOf<Pair<Int, Boolean>>() }

    // Función que registra cada cambio y actualiza estadísticas
    fun mover(nuevo: Int, esInc: Boolean) {
        contador = nuevo
        if (esInc) inc++ else dec++
        max = maxOf(max, contador)
        min = if (inc + dec == 1) contador else minOf(min, contador)
        historial.add(Pair(contador, esInc))
    }

    // Layout principal en columna
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .padding(top = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título con mi nombre
        Text("Diego Guevara", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        // Fila con botones de incremento/decremento y el valor en medio
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Botón restar
            Button(
                onClick = { mover(contador - 1, false) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0))
            ) { Text("-") }

            // Texto del contador
            Text(
                text = "$contador",
                fontSize = 40.sp,
                modifier = Modifier.padding(horizontal = 24.dp),
                fontWeight = FontWeight.Bold
            )

            // Botón sumar
            Button(
                onClick = { mover(contador + 1, true) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0))
            ) { Text("+") }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Estadísticas del contador
        Column(horizontalAlignment = Alignment.Start) {
            Text("Total incrementos: $inc")
            Text("Total decrementos: $dec")
            Text("Valor máximo: $max")
            Text("Valor mínimo: $min")
            Text("Total cambios: ${inc + dec}")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Historial:", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        // Grid para mostrar historial
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(4.dp)
        ) {
            items(historial) { (valor, esInc) ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            if (esInc) Color(0xFF4CAF50) else Color(0xFFF44336),
                            shape = MaterialTheme.shapes.small
                        )
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "$valor", color = Color.White)
                }
            }
        }

        // Botón para reiniciar todo
        Button(
            onClick = {
                contador = 0
                inc = 0
                dec = 0
                max = 0
                min = 0
                historial.clear()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5C6BC0))
        ) { Text("Reiniciar", color = Color.White) }
    }
}
