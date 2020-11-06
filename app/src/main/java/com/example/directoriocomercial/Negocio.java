package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Adapters.MenuAdapterC;
import Adapters.MenuAdapterR;
import bd.AyudanteBD;
import bd.Contacto;
import bd.Direccion;
import clases.Constant;

public class Negocio extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    ImageView logo;
    TextView titulo, giro, etiqueta, existeComentarios;
    ListView redes, comentarios;
    EditText coment;
    Button enviar;
    int idNegocio;
    String urlNegocio;
    private ArrayList<MenuR> menu;
    private MenuAdapterR adapter;
    private ArrayList<MenuC> menuComent;
    private MenuAdapterC adapterComent;
    private SharedPreferences userPref;
    int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        logo = (ImageView)findViewById(R.id.iv_logo);
        titulo = (TextView)findViewById(R.id.tv_titulo);
        giro = (TextView)findViewById(R.id.tv_giro_negocio);
        etiqueta = (TextView)findViewById(R.id.txt_comentario);
        existeComentarios = (TextView)findViewById(R.id.txt_existeComentarios);
        redes = (ListView)findViewById(R.id.lv_redes_sociales);
        comentarios = (ListView)findViewById(R.id.lv_comentarios);
        coment = (EditText)findViewById(R.id.edt_comentarioNeg);
        enviar = (Button)findViewById(R.id.btn_enviar_ComenarioNeg);

        enviar.setOnClickListener(this);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(!userPref.getBoolean("isLoggedIn", false)){
            coment.setVisibility(View.INVISIBLE);
            enviar.setVisibility(View.INVISIBLE);
            etiqueta.setVisibility(View.INVISIBLE);
        }
        idUsuario = userPref.getInt("id", 0);

        menu=new ArrayList<MenuR>();
        menuComent=new ArrayList<MenuC>();

        Bundle bolsaR = getIntent().getExtras();
        idNegocio = bolsaR.getInt("ID");
        urlNegocio = bolsaR.getString("URL");
        mostrarInfo();
        //Metodo para mostrar los comentarios del negocio
        listaComentarios();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_enviar_ComenarioNeg){
            String c = coment.getText().toString();
            if(!c.isEmpty()) {
                //Enviar los datos a la bd remota
                enviarComentario();
            }
            else {
                Toast.makeText(this, "Escribe tu comentario.",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void enviarComentario(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.PUBLICAR_COMENTARIO, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(this, "Se ha publicado tu comentario.",Toast.LENGTH_LONG).show();
                    coment.setText("");
                    menuComent.clear();
                    comentarios.setAdapter(null);
                    listaComentarios();
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
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("user_id",idUsuario+"");
                map.put("contenido",coment.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Negocio.this);
        queue.add(request);
    }

    public void mostrarInfo(){
        //Metodo para mostrar la foto del negocio
        SQLiteDatabase db=null;
        bd.Negocio negBD;
        Contacto cont;
        Direccion direc;
        try{
            negBD=new bd.Negocio(this,"Directorio",null,1);
            db = negBD.getReadableDatabase();
            if (db!=null) {
                Cursor cursor = db.rawQuery("SELECT titulo,giro FROM negocios WHERE id="+idNegocio,null);
                if (cursor.moveToNext()){
                    titulo.setText(cursor.getString(0));
                    giro.setText(cursor.getString(1));
                    cursor.close();
                    db.close();
                }
                else{
                    cursor.close();
                    db.close();
                }
            }//if
            else {
            }
        }//try
        catch (Exception e) {}//catch
        try{
            String valor = "";
            cont=new Contacto(this,"Directorio",null,1);
            db = cont.getReadableDatabase();
            if (db!=null) {
                Cursor cursor = db.rawQuery("SELECT email,telefono,web,face,insta FROM contactoNegocio WHERE id="+idNegocio,null);
                if (cursor.moveToNext()){
                    menu.add(new MenuR(R.drawable.correo2, cursor.getString(0)+""));
                    menu.add(new MenuR(R.drawable.llamada2, cursor.getString(1)+""));
                    if(cursor.getString(2).isEmpty())
                        menu.add(new MenuR(R.drawable.web2, cursor.getString(2)+""));
                    if(cursor.getString(3).isEmpty())
                        menu.add(new MenuR(R.drawable.face2, cursor.getString(3)+""));
                    if(cursor.getString(4).isEmpty())
                        menu.add(new MenuR(R.drawable.insta2, cursor.getString(4)+""));
                    adapter = new MenuAdapterR(this, menu);
                    redes.setAdapter(adapter);
                    cursor.close();
                    db.close();
                }
                else{
                    cursor.close();
                    db.close();
                }
            }//if
            else {
            }
        }//try
        catch (Exception e) {}//catch
    }

    //Consulta a la pagina para mostrar comentarios
    public void listaComentarios(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.COMENTARIOS, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray comentariosA = new JSONArray(String.valueOf(object.getJSONArray("comentarios")));
                    for (int i = 0; i<comentariosA.length(); i++){
                        JSONObject post = comentariosA.getJSONObject(i);
                        menuComent.add(new MenuC(R.drawable.usuario, post.getString("nombre"),post.getString("created_at"),post.getString("contenido")));
                    }
                    adapterComent = new MenuAdapterC(this, menuComent);
                    comentarios.setAdapter(adapterComent);
                }
                else{
                    existeComentarios.setText("Sin Comentarios");
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("negocio_id",idNegocio+"");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Negocio.this);
        queue.add(request);
    }

    //Consulta a la pagina para la foto
    public void fotoLogo(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.NEGOCIO, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject negocio = new JSONObject(String.valueOf(object.getJSONObject("negocio")));
                    Picasso.get().load(negocio.getString("image")).into(logo);
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id",idNegocio+"");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Negocio.this);
        queue.add(request);
    }

    //Elaboración de la lista de Comentarios
    public class MenuC {
        private int foto;
        private String nombre;
        private String fecha;
        private String comentario;

        public MenuC(int foto, String nombre, String fecha, String comentario) {
            this.foto = foto;
            this.nombre = nombre;
            this.fecha = fecha;
            this.comentario = comentario;
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
    }

    //Elaboración de la lista de Redes sociales
    public class MenuR {
        private int foto;
        private String nombre;

        public MenuR(int foto, String nombre) {
            this.foto = foto;
            this.nombre = nombre;
        }

        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public int getFoto() { return foto; }
        public void setFoto(int foto) { this.foto = foto; }
    }

}
