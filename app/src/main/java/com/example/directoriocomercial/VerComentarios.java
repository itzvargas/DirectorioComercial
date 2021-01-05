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

import Adapters.MenuAdapterC;
import Adapters.MenuAdapterCTotal;
import clases.Constant;

public class VerComentarios extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView com;
    private ArrayList<MenuC> menuComent;
    private MenuAdapterCTotal adapterComent;
    int idNegocio,idUsuario;
    private SharedPreferences userPref;
    int[] eliminarComentarios;
    private ProgressDialog dialog;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_comentarios);
        setTitle("Comentarios");
        com = (ListView)findViewById(R.id.lv_totalComentarios);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        idUsuario = userPref.getInt("id", 0);
        idNegocio = getIntent().getIntExtra("ID",0);
        token = userPref.getString("token","");
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        menuComent=new ArrayList<MenuC>();
        //com.setOnItemClickListener(this);
        mostrarInfo();
    }

    public void mostrarInfo(){
        //Metodo para mostrar la foto del negocio
        dialog.setMessage("Cargando comentarios");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.NEGOCIO_INDIVIDUAL+idNegocio, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject negocio = object.getJSONObject("negocio");

                    JSONArray comentario = new JSONArray(String.valueOf(negocio.getJSONArray("opiniones")));
                    if(comentario.length() == 0)
                        setTitle("Sin Comentarios");
                    eliminarComentarios = new int[comentario.length()];
                    for (int j = 0; j<comentario.length(); j++){
                        JSONObject coment = comentario.getJSONObject(j);
                        /*if(coment.getInt("user_id") == idUsuario) {
                            menuComent.add(new MenuC(R.drawable.usuario, coment.getString("autor") + "",
                                    coment.getString("fecha_creado"), coment.getString("contenido"),"Eliminar",
                                    coment.getInt("valoracion")));
                            eliminarComentarios[j] = coment.getInt("id");
                        }
                        else {*/
                            menuComent.add(new MenuC(R.drawable.usuario, coment.getString("autor") + "",
                                    coment.getString("fecha_creado"), coment.getString("contenido"),"",
                                    coment.getInt("valoracion")));
                            eliminarComentarios[j] = 0;
                        //}
                    }
                    adapterComent = new MenuAdapterCTotal(this,menuComent);
                    com.setAdapter(adapterComent);
                }
                else{
                    Toast.makeText(this, "No hay negocios",Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
            catch (JSONException e){
                Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        },error -> {
            Toast.makeText(this, error.getMessage()+"",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
        };
        RequestQueue queue = Volley.newRequestQueue(VerComentarios.this);
        queue.add(request);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*if(idUsuario != 0) {
            if (eliminarComentarios[position] != 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(VerComentarios.this);
                builder.setMessage("¿Estás seguro de eliminar tu comentario?");
                builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        destryComentario(eliminarComentarios[position]);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        }*/
    }
    public void destryComentario(int id){
        StringRequest request = new StringRequest(Request.Method.DELETE, Constant.OPINIONES+id+"/destroy", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
                }
                else {
                    Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("token",token);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(VerComentarios.this);
        queue.add(request);
    }

    //Elaboración de la lista de Comentarios
    public class MenuC {
        private int foto;
        private String nombre;
        private String fecha;
        private String comentario;
        private String eliminar;
        private int valor;

        public MenuC(int foto, String nombre, String fecha, String comentario, String eliminar, int valor) {
            this.foto = foto;
            this.nombre = nombre;
            this.fecha = fecha;
            this.comentario = comentario;
            this.eliminar = eliminar;
            this.valor = valor;
        }

        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public int getFoto() { return foto; }
        public void setFoto(int foto) { this.foto = foto; }
        public String getFecha() { return fecha; }
        public void setFecha(String fecha) { this.fecha = fecha; }
        public String getComentario() { return comentario; }
        public void setComentario(String comentario) { this.comentario = comentario; }
        public String getEliminar() { return eliminar; }
        public void setEliminar(String eliminar) { this.eliminar = eliminar; }
        public int getValor() { return valor; }
        public void setValor(int valor) { this.valor = valor; }
    }

}
