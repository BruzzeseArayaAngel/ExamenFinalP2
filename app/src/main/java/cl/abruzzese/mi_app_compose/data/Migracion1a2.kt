import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migracion1a2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("DROP TABLE IF EXISTS Gasto")

        database.execSQL("""
            CREATE TABLE IF NOT EXISTS Gasto (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                fecha INTEGER NOT NULL,
                tipoMedidor TEXT NOT NULL,
                valorMedidor REAL NOT NULL,
                monto REAL NOT NULL
            )
        """)
    }
}