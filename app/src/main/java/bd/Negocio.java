package bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Negocio extends SQLiteOpenHelper {

    //Sentencia para crear la tabla de Negocio
    String creacionNegocios="CREATE TABLE negocios (id BIGINTEGER(20) primary key,denominacion_soc TEXT, slug TEXT, giro TEXT, descripcion TEXT," +
            "principales_prod TEXT)";

    public Negocio(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(creacionNegocios);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Deberemos migrar datos de la tabla antigua a la nueva.
        //Eliminamos la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS negocios");
        //y luego creamos la nueva
        db.execSQL(creacionNegocios);
    }
}
