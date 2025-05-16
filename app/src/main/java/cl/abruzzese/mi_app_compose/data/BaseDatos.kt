package cl.abruzzese.mi_app_compose.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Gasto::class], version = 3)
@TypeConverters(LocalDateConverter::class)
abstract class BaseDatos : RoomDatabase() {
    abstract fun gastoDao(): GastoDao

    companion object {
        @Volatile
        private var INSTANCE: BaseDatos? = null

        fun getDatabase(context: Context): BaseDatos {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BaseDatos::class.java,
                    "mi_app_db"
                )
                    .addMigrations(Migracion1a2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}