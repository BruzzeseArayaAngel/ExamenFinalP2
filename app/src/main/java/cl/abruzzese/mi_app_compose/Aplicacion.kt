package cl.abruzzese.mi_app_compose

import android.app.Application
import androidx.room.Room
import cl.abruzzese.mi_app_compose.data.BaseDatos

class Aplicacion : Application() {

    val db by lazy { Room.databaseBuilder(this, BaseDatos::class.java, "gastos.db").build()}
    val gastoDao by lazy { db.gastoDao() }
}