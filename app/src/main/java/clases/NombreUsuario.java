package clases;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NombreUsuario extends AsyncTask<String,Void,String> {
    String nombre = "";
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
            for (int i = 0; i < arreglo.length(); i++) {
                 JSONObject renglon = arreglo.getJSONObject(i);
                 nombre = renglon.getString("name");
            }
        }
        catch(Exception e){
            //Si no hay internet
        }
    }

    public String getNombreUsuario(String url){
        NombreUsuario nu = new NombreUsuario();
        nu.execute(url);
        return nombre;
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
