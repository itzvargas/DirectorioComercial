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

public class Direccion extends SQLiteOpenHelper {

    String url = "http://elsitioKOCE.com/base";
    int[] ids;
    int[] neg_id;
    int[] noE;
    int[]noI;
    String[] call;
    String[] col;
    String[] codp;
    String[] muni;
    String[] esta;
    String[] url_mapa;

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
        // En la práctica deberemos migrar datos de la tabla antigua a la nueva
        //Eliminamos la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS direccion");
        //y luego creamos la nueva
        db.execSQL(creacionDireccNeg);
        Tarea t=new Tarea();
        t.execute(url);
        try {
            if (db != null) {
                for(int i = 0;i<ids.length;i++){
                    if(noI != null){
                        db.execSQL("INSERT INTO direccion values (" + ids[i] + ","+ neg_id[i] +",'" + call[i] + "', " + noE[i] + "," +
                                noI[i] + ",'" + col[i] + "','" + codp[i] +"','" + muni[i] + "','" + esta[i] + "','" + url_mapa[i] + "')");
                    }
                    else {
                        db.execSQL("INSERT INTO direccion (id,negocio_id,calle,no_ext,colonia,cp,municipio,estado,url_mapa) " +
                                "values (" + ids[i] + ","+ neg_id[i] +",'" + call[i] + "', " + noE[i] + "," +
                                "'" + col[i] + "','" + codp[i] +"','" + muni[i] + "','" + esta[i] + "','" + url_mapa[i] + "')");
                    }
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
                call = new String[arreglo.length()];
                noE = new int[arreglo.length()];
                noI = new int[arreglo.length()];
                col = new String[arreglo.length()];
                codp = new String[arreglo.length()];
                muni = new String[arreglo.length()];
                esta = new String[arreglo.length()];
                url_mapa = new String[arreglo.length()];
                for (int i = 0; i<arreglo.length(); i++){
                    JSONObject renglon = arreglo.getJSONObject(i);
                    ids[i] = renglon.getInt("id");
                    neg_id[i] = renglon.getInt("negocio_id");
                    call[i] = renglon.getString("calle");
                    noE[i] = renglon.getInt("no_ext");
                    noI[i] = renglon.getInt("no_int");
                    col[i] = renglon.getString("colonia");
                    codp[i] = renglon.getString("cp");
                    muni[i] = renglon.getString("municipio");
                    esta[i] = renglon.getString("estado");
                    url_mapa[i] = renglon.getString("url_mapa");
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