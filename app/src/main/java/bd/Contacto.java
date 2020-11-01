package bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Contacto extends SQLiteOpenHelper {

    String url = "http://elsitioKOCE.com/base";
    int[] ids;
    int[] neg_id;
    String[] email;
    String[] telef;
    String[] web;
    String[] horario;
    String[] face;
    String[] insta;

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
        Tarea tC=new Tarea();
        tC.execute(url);
        try {
            if (db != null) {
                for(int i = 0;i<ids.length;i++){
                    db.execSQL("INSERT INTO contactoNegocio values (" + ids[i] + ","+ neg_id[i] +",'" + email[i] + "',"+
                            "'" + telef[i] + "','" + web[i] +"','" + horario[i] + "','" + face[i] + "','" + insta[i] + "')");
                }
                db.close();
            } else {
            }
        }//try
        catch (Exception e) {
        }
    }

    //Consulta a la pagina
    class Tarea extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            String salida=ConexionWeb(strings[0]);
            return salida;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONArray arreglo = new JSONArray(s);
                ids = new int[arreglo.length()];
                neg_id = new int[arreglo.length()];
                email = new String[arreglo.length()];
                telef = new String[arreglo.length()];
                web = new String[arreglo.length()];
                horario = new String[arreglo.length()];
                face = new String[arreglo.length()];
                insta = new String[arreglo.length()];
                for (int i = 0; i<arreglo.length(); i++){
                    JSONObject renglon = arreglo.getJSONObject(i);
                    ids[i] = renglon.getInt("id");
                    neg_id[i] = renglon.getInt("negocio_id");
                    email[i] = renglon.getString("email");
                    telef[i] = renglon.getString("telefono");
                    web[i] = renglon.getString("web");
                    horario[i] = renglon.getString("horario");
                    face[i] = renglon.getString("facebook");
                    insta[i] = renglon.getString("instagram");
                }
            }
            catch(Exception e){
                //Si no hay internet
            }
        }
    }
    String ConexionWeb(String direccion) {
        String pagina="";
        try {
            URL url = new URL(direccion);
            HttpURLConnection conexion = (HttpURLConnection) url.openConnection();
            if (conexion.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conexion.getInputStream()));
                String linea = reader.readLine();
                while (linea != null) {
                    pagina += linea + "\n";
                    linea = reader.readLine();
                }
                reader.close();
            } else {
                pagina += "ERROR: " + conexion.getResponseMessage() + "\n";
            }
            conexion.disconnect();
        }
        catch (Exception e){
            pagina+=e.getMessage();
        }
        return pagina;
    }
}