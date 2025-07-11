package cl.abruzzese.mi_app_compose.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface GastoDao {

    @Query("SELECT * FROM Gasto ORDER BY fecha DESC")
    suspend fun obtenerTodos(): List<Gasto>

    @Query("SELECT * FROM Gasto WHERE id = :id")
    suspend fun obtenerPorId(id: Long): Gasto

    @Insert
    suspend fun insertar(gasto: Gasto)

    @Update
    suspend fun modificar(gasto: Gasto)

    @Delete
    suspend fun eliminar(gasto: Gasto)
}