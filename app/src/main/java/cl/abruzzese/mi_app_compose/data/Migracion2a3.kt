package cl.abruzzese.mi_app_compose.data

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migracion2a3 : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS Gasto_temp (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fecha INTEGER NOT NULL,
                tipoMedidor TEXT NOT NULL,
                valorMedidor INTEGER NOT NULL,
                monto REAL NOT NULL
            )
        """)

        database.execSQL("""
            INSERT INTO Gasto_temp (id, fecha, tipoMedidor, valorMedidor, monto)
            SELECT id, fecha, tipoMedidor, CAST(valorMedidor AS INTEGER), monto
            FROM Gasto
        """)

        database.execSQL("DROP TABLE Gasto")

        database.execSQL("ALTER TABLE Gasto_temp RENAME TO Gasto")
    }
}