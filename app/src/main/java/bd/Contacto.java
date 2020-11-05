package bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Contacto extends SQLiteOpenHelper {

    //Sentencia para crear la tabla de Usuarios
    String creacionContatNeg="CREATE TABLE contactoNegocio (id BIGINTEGER(20) primary key, negocio_id BIGINTEGER(20), email TEXT, " +
            "telefono TEXT, web TEXT, horario TEXT, face TEXT, insta TEXT)";

    public Contacto(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(creacionContatNeg);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // En la práctica deberemos migrar datos de la tabla antigua a la nueva
        //Eliminamos la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS contactoNegocio");
        //y luego creamos la nueva
        db.execSQL(creacionContatNeg);
    }
}