package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.MenuAdapterPromociones;
import clases.Constant;

public class MisPromociones extends AppCompatActivity implements AdapterView.OnItemClickListener {

    String op;
    ListView lista;
    TextView texto;
    private ArrayList<MenuAdapterPromociones.MenuPromo> menu;
    private MenuAdapterPromociones adapter;
    private SharedPreferences userPref;
    private ProgressDialog dialog;
    int[] idsPromo;
    int[] idsCupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_promociones);

        op = getIntent().getStringExtra("OPCION");
        lista = (ListView)findViewById(R.id.lv_miCuponera);
        texto = (TextView)findViewById(R.id.textView10);
        lista.setOnItemClickListener(this);
        menu=new ArrayList<MenuAdapterPromociones.MenuPromo>();
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(op.equals("Promocion")) {
            setTitle("Mis promociones");
            mostrarPromos();
        }
        else{
            setTitle("Mis cupones");
            texto.setText("Selecciona un cupón para eliminarlo.");
            mostrarCupones();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(op.equals("Promocion")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MisPromociones.this);
            builder.setMessage("¿Estás seguro de eliminar tu promo?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    eliminarPromo(idsPromo[position]);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(MisPromociones.this);
            builder.setMessage("¿Estás seguro de eliminar tu promo?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    eliminarCupon(idsCupon[position]);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
    }

    //Promociones
    public void mostrarPromos(){
        //Metodo para mostrar las promociones
        dialog.setMessage("Cargando promociones");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.MOSTRAR_PROMOS, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray promociones = new JSONArray(String.valueOf(object.getJSONArray("promociones")));
                    int i = 0;
                    idsPromo = new int[promociones.length()];
                    for (int j = 0; j < promociones.length(); j++) {
                        JSONObject post = promociones.getJSONObject(j);
                        JSONObject negocio = post.getJSONObject("negocio");
                        if(negocio.getInt("user_id") == userPref.getInt("id",0)) {
                            idsPromo[i] = post.getInt("id");
                            menu.add(new MenuAdapterPromociones.MenuPromo(post.getString("image") + "", post.getString("titulo") + "",
                                    post.getString("descripcion") + "", post.getString("fechaVigencia") + "", "",
                                    post.getString("denominacion_soc") + ""));
                            i++;
                        }
                    }
                    if(i == 0)
                        Toast.makeText(this, "No tienes promociones",Toast.LENGTH_LONG).show();
                    adapter = new MenuAdapterPromociones(this, menu);
                    lista.setAdapter(adapter);
                }
                else{
                    Toast.makeText(this, "Intentelo mas tarde",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        },error -> {
            Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(MisPromociones.this);
        queue.add(request);
    }

    public void eliminarPromo(int id){
        dialog.setMessage("Eliminando promoción");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.DELETE, Constant.ELIMINAR_PROMO+id+"/destroy", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(this, "Promo eliminada",Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                else{
                    Toast.makeText(this, "Intentelo mas tarde",Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Intentelo mas tarde",Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        },error -> {
            Toast.makeText(this, "Intentelo mas tarde",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("token",userPref.getString("token",""));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MisPromociones.this);
        queue.add(request);
    }

    //Cupones
    public void mostrarCupones(){
        dialog.setMessage("Cargando cupones");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.MOSTRAR_CUPONES, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray promociones = new JSONArray(String.valueOf(object.getJSONArray("promociones")));
                    int i = 0;
                    idsCupon = new int[promociones.length()];
                    for (int j = 0; j < promociones.length(); j++) {
                        JSONObject post = promociones.getJSONObject(j);
                        JSONObject negocio = post.getJSONObject("negocio");
                        if(negocio.getInt("user_id") == userPref.getInt("id",0)) {
                            idsCupon[i] = post.getInt("id");
                            menu.add(new MenuAdapterPromociones.MenuPromo(post.getString("image") + "", post.getString("titulo") + "",
                                    post.getString("descripcion") + "", post.getString("fechaVigencia") + "", post.getString("codigo") + "",
                                    post.getString("denominacion_soc") + ""));
                            i++;
                        }
                    }
                    adapter = new MenuAdapterPromociones(MisPromociones.this, menu);
                    lista.setAdapter(adapter);
                }
                else{
                    Toast.makeText(this, "Intentelo mas tarde",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        },error -> {
            Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(MisPromociones.this);
        queue.add(request);
    }

    public void eliminarCupon(int id){
        dialog.setMessage("Eliminando cupón");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.DELETE, Constant.ELIMINAR_CUPON+id+"/destroy", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(this, "Cupón eliminado",Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                else{
                    Toast.makeText(this, "Intentelo mas tarde",Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Intentelo mas tarde",Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        },error -> {
            Toast.makeText(this, "Intentelo mas tarde",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("token",userPref.getString("token",""));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(MisPromociones.this);
        queue.add(request);
    }
}
