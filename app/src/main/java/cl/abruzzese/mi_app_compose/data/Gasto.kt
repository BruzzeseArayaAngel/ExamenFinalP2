package cl.abruzzese.mi_app_compose.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Gasto(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var tipoMedidor: String,
    var valorMedidor: Int,
    var fecha: LocalDate
)