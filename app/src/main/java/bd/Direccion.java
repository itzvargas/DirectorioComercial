package bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Direccion extends SQLiteOpenHelper {

    //Sentencia para crear la tabla de Usuarios
    String creacionDireccNeg="CREATE TABLE direccion (id BIGINTEGER(20) primary key,negocio_id BIGINTEGER(20),calle TEXT," +
            "no_ext INTEGER, no_int INTEGER, colonia TEXT, cp TEXT, municipio TEXT, estado TEXT, url_mapa TEXT)";

    public Direccion(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(creacionDireccNeg);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En la práctica deberemos migrar datos de la tabla antigua
        // a la nueva, por lo que este método deberá ser más elaborado.
        //Eliminamos la versión anterior de la tabla
        //db.execSQL("DROP TABLE IF EXISTS autos");
        //y luego creamos la nueva
        //db.execSQL(sqlCreate);
    }
}