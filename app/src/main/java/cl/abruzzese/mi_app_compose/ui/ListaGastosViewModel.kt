package cl.abruzzese.mi_app_compose.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cl.abruzzese.mi_app_compose.Aplicacion
import cl.abruzzese.mi_app_compose.data.Gasto
import cl.abruzzese.mi_app_compose.data.GastoDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class ListaGastosViewModel(private val gastoDao: GastoDao) : ViewModel() {

    var gastos by mutableStateOf(listOf<Gasto>())
        private set

    var tipoMedidor by mutableStateOf("Agua")
    var valorMedidor by mutableStateOf("")
    var fechaMedicion by mutableStateOf(LocalDate.now())

    init {
        obtenerGastos()
    }

    fun insertarGasto() {
        val valor = valorMedidor.toIntOrNull()
        if (valor != null) {
            val nuevoGasto = Gasto(
                tipoMedidor = tipoMedidor,
                valorMedidor = valor,
                fecha = fechaMedicion
            )
            viewModelScope.launch(Dispatchers.IO) {
                gastoDao.insertar(nuevoGasto)
                obtenerGastos()
                tipoMedidor = "Agua"
                valorMedidor = ""
                fechaMedicion = LocalDate.now()
            }
        } else {
            // TODO: Mostrar un error al usuario si el valor no es válido
        }
    }

    fun obtenerGastos() {
        viewModelScope.launch(Dispatchers.IO) {
            gastos = gastoDao.obtenerTodos()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val aplicacion = (this[APPLICATION_KEY] as Aplicacion)
                ListaGastosViewModel(aplicacion.gastoDao)
            }
        }
    }
}