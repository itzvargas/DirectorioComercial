package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import bd.AyudanteBD;
import bd.Contacto;
import bd.Negocio;
import bd.Direccion;
import clases.Constant;

public class Splash extends AppCompatActivity {

    Negocio neg;
    Direccion direc;
    Contacto contact;
    SQLiteDatabase db2=null;
    SQLiteDatabase db3=null;
    SQLiteDatabase db4=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        neg =new Negocio(this,"Directorio",null,1);
        direc =new Direccion(this,"Directorio",null,1);
        contact =new Contacto(this,"Directorio",null,1);
        actualizacion();
        boolean verif = verificacion();
        if(verif == true) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            },3000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash.this,Login.class);
                    startActivity(intent);
                    finish();
                }
            },3000);
        }
    }

    public boolean verificacion(){
        SharedPreferences userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        return userPref.getBoolean("isLoggedIn",false);
    }

    public void actualizacion(){
        try{
            db2 = neg.getReadableDatabase();
            db3 = direc.getReadableDatabase();
            db4 = contact.getReadableDatabase();
            if (db2!=null || db3!=null || db4!=null) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    // Si hay conexión a Internet en este momento
                    neg.onUpgrade(db2,1,1);
                    db2.close();
                    direc.onUpgrade(db3,1,1);
                    db3.close();
                    contact.onUpgrade(db4,1,1);
                    db4.close();
                    actulizarNegocio();
                    actulizarDireccion();
                    actulizarContacto();
                } else {
                    db2.close();
                    db3.close();
                    db4.close();
                    // No hay conexión a Internet en este momento
                    Toast.makeText(this, "Sin conexión a Internet",Toast.LENGTH_LONG).show();
                }
            }//if
            else {
                db2.close();
                db3.close();
                db4.close();
            }
        }//try
        catch (Exception e) {
            db2.close();
            db3.close();
            db4.close();
            Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }//catch
    }

    public void actulizarNegocio(){
        // Add the request to the RequestQueue.
        StringRequest request = new StringRequest(Request.Method.GET, Constant.ACTUALIZAR_NEGOCIO, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray negocio = new JSONArray(String.valueOf(object.getJSONArray("data")));
                    db2 = neg.getWritableDatabase();
                    for (int i = 0; i<negocio.length(); i++){
                        JSONObject post = negocio.getJSONObject(i);
                        db2.execSQL("INSERT INTO negocios values (" + post.getInt("id") + ",'" + post.getString("denominacion_soc") +
                                "','" + post.getString("slug") + "'," + "'" + post.getString("giro") +"','" +
                                post.getString("descripcion") + "','" + post.getString("principales_prod") + "')");
                    }
                }
                else{
                }
            }
            catch (JSONException e){
            }
        },error -> {
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(Splash.this);
        queue.add(request);
    }

    public void actulizarDireccion(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.ACTUALIZAR_DIRECCION, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray direccion = new JSONArray(String.valueOf(object.getJSONArray("data")));
                    db2 = direc.getWritableDatabase();
                    for (int i = 0; i<direccion.length(); i++){
                        JSONObject post = direccion.getJSONObject(i);
                        db2.execSQL("INSERT INTO direccion values (" + post.getInt("id") + "," + post.getString("negocio_id") +
                                ",'" + post.getString("calle") + "'," + post.getString("no_ext") +"," +
                                post.getString("no_int") + ",'" + post.getString("colonia") + "','" +
                                post.getString("cp") + "','" + post.getString("municipio") + "','"+
                                post.getString("estado") + "','" + post.getString("url_mapa") + "'"+ ")");
                    }
                }
                else{
                }
            }
            catch (JSONException e){
            }
        },error -> {
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(Splash.this);
        queue.add(request);
    }

    public void actulizarContacto(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.ACTUALIZAR_CONTACTO, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray contacto = new JSONArray(String.valueOf(object.getJSONArray("data")));
                    db2 = contact.getWritableDatabase();
                    for (int i = 0; i<contacto.length(); i++){
                        JSONObject post = contacto.getJSONObject(i);
                        db2.execSQL("INSERT INTO contactoNegocio values (" + post.getInt("id") + ","+ post.getInt("negocio_id") +
                                ",'" + post.getString("email") + "','" + post.getString("telefono") +
                                "'," + "'" + post.getString("web") +"','" + post.getString("horario") +
                                "','" + post.getString("facebook") + "','"+ post.getString("instagram") +"')");
                    }
                }
                else{
                }
            }
            catch (JSONException e){
            }
        },error -> {
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(Splash.this);
        queue.add(request);
    }

}
