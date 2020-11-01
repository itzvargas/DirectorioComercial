package bd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.directoriocomercial.Negocios;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Negocio extends SQLiteOpenHelper {

    String denominacion, giro, descripcion,principales_Prod, mensaje = "",slug;
    int id,autorizado;
    String url = "http://elsitioKOCE.com/base";
    int[] ids;
    String[] denom;
    String[] sl;
    String[] gir;
    String[] desc;
    String[] pp;

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
        Tarea tN=new Tarea();
        tN.execute(url);
        try {
            if (db != null) {
                for(int i = 0;i<ids.length;i++){
                    db.execSQL("INSERT INTO negocios values (" + ids[i] + ",'" + denom[i] + "','" + sl[i] + "','" + gir[i] + "'," +
                            "'" + desc[i] +"','" + pp[i] + "')");
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
                denom = new String[arreglo.length()];
                sl = new String[arreglo.length()];
                gir = new String[arreglo.length()];
                desc = new String[arreglo.length()];
                pp = new String[arreglo.length()];
                int j = 0;
                for (int i = 0; i<arreglo.length(); i++){
                    JSONObject renglon = arreglo.getJSONObject(i);
                    id = renglon.getInt("id");
                    denominacion = renglon.getString("denominacion_soc");
                    slug = renglon.getString("slug");
                    giro = renglon.getString("giro");
                    descripcion = renglon.getString("descripcion");
                    principales_Prod = renglon.getString("principales_prod");
                    autorizado = renglon.getInt("autorizado");
                    if (autorizado == 1){
                        ids[j] = id;
                        denom[j] = denominacion;
                        sl[j] = slug;
                        gir[j] = giro;
                        desc[j] = descripcion;
                        pp[j] = principales_Prod;
                        j++;
                    }
                }
            }
            catch(Exception e){
                //Si no hay internet
                mensaje = "Sin conexion a Internet";
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
