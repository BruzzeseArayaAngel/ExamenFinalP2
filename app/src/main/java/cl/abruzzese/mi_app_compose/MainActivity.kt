package cl.abruzzese.mi_app_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.composable
import cl.abruzzese.mi_app_compose.ui.ListaGastosViewModel
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppMedidoresUI()
        }
    }
}

@Composable
fun AppMedidoresUI(
    vmListaGastos: ListaGastosViewModel = viewModel(factory = ListaGastosViewModel.Factory),
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = "listado") {
        composable("listado") {
            ListaMedicionesScreen(vmListaGastos = vmListaGastos, onNavigateToRegistro = { navController.navigate("registro") })
        }
        composable("registro") {
            RegistroMedidorScreen(vmListaGastos = vmListaGastos, onRegistroGuardado = { navController.popBackStack() })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaMedicionesScreen(vmListaGastos: ListaGastosViewModel, onNavigateToRegistro: () -> Unit) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNavigateToRegistro) {
                Icon(Icons.Filled.Add, stringResource(R.string.nuevo_registro))
            }
        },
        topBar = {

        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues) {
            items(vmListaGastos.gastos) { lectura ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val icon = when (lectura.tipoMedidor) {
                            "Agua" -> painterResource(R.drawable.ic_agua)
                            "Luz" -> painterResource(R.drawable.ic_luz)
                            "Gas" -> painterResource(R.drawable.ic_gas)
                            else -> null
                        }
                        icon?.let {
                            Image(painter = it, contentDescription = lectura.tipoMedidor, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        Text(text = when (lectura.tipoMedidor) {
                            "Agua" -> stringResource(R.string.tipo_agua).uppercase()
                            "Luz" -> stringResource(R.string.tipo_luz).uppercase()
                            "Gas" -> stringResource(R.string.tipo_gas).uppercase()
                            else -> lectura.tipoMedidor.uppercase()
                        })
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${lectura.valorMedidor}")
                    }

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(text = "${lectura.fecha}")
                    }
                }
                HorizontalDivider()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroMedidorScreen(vmListaGastos: ListaGastosViewModel, onRegistroGuardado: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.registro_medidor),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = vmListaGastos.valorMedidor,
            onValueChange = { vmListaGastos.valorMedidor = it },
            label = { Text(stringResource(R.string.valor)) },
            modifier = Modifier.fillMaxWidth(0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))

        var fechaIngresada by remember { mutableStateOf(vmListaGastos.fechaMedicion.toString()) }

        TextField(
            value = fechaIngresada,
            onValueChange = {
                fechaIngresada = it
                try {
                    vmListaGastos.fechaMedicion = LocalDate.parse(it)
                } catch (e: Exception) {
                    Log.e("RegistroMedidorScreen", "Error al parsear la fecha: $it", e)
                }
            },
            label = { Text(stringResource(R.string.fecha_formato)) },
            modifier = Modifier.fillMaxWidth(0.7f)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = CenterHorizontally) {
            Text("Medidor de:")

            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = vmListaGastos.tipoMedidor == "Agua", onClick = { vmListaGastos.tipoMedidor = "Agua" })
                Text(stringResource(R.string.agua))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = vmListaGastos.tipoMedidor == "Luz", onClick = { vmListaGastos.tipoMedidor = "Luz" })
                Text(stringResource(R.string.luz))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(selected = vmListaGastos.tipoMedidor == "Gas", onClick = { vmListaGastos.tipoMedidor = "Gas" })
                Text(stringResource(R.string.gas))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                vmListaGastos.insertarGasto()
                onRegistroGuardado()
            },
            modifier = Modifier.fillMaxWidth(0.7f)
        ) {
            Text(stringResource(R.string.btn_text_registrar_medicion))
        }
    }
}
















