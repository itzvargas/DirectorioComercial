package com.example.directoriocomercial;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import Adapters.MenuAdapter;
import bd.Negocio;
import clases.Constant;

public class Negocios extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView negocios;
    EditText buscar;
    Button bBuscar, bBorrar;
    String palabra;
    int[] ids;
    String[] nom;
    String[] gir;
    private ArrayList<Menu> menu;
    private ListView lvMenu;
    private MenuAdapter adapter;
    Negocio aBD;
    SQLiteDatabase db=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios);
        setTitle(R.string.menu_principal);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        negocios = (ListView)findViewById(R.id.lv_negocios);
        buscar = (EditText)findViewById(R.id.edt_buscar);
        bBuscar = (Button)findViewById(R.id.btn_buscar);
        bBorrar = (Button)findViewById(R.id.btn_borrar);

        menu=new ArrayList<Menu>();
        lvMenu=(ListView)findViewById(R.id.lv_negocios);

        llenarlita();
        adapter = new MenuAdapter(this, menu);
        lvMenu.setAdapter(adapter);

        bBuscar.setOnClickListener(this);
        bBorrar.setOnClickListener(this);
        lvMenu.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_borrar){
            menu.clear();
            lvMenu.setAdapter(null);
            lista();
        }
        if(v.getId() == R.id.btn_buscar){
            String busqueda = buscar.getText().toString();
            if(!busqueda.isEmpty()){
                menu.clear();
                lvMenu.setAdapter(null);
                lista();
            }
            else{
                Toast.makeText(this, "Escribe lo que deseas buscar",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Mostrar datos del item seleccionado
        Intent int1 = new Intent(Negocios.this,com.example.directoriocomercial.Negocio.class);
        int1.putExtra("ID",ids[position]);
        int1.putExtra("DENOMINACION",nom[position]);
        int1.putExtra("GIRO",gir[position]);
        startActivity(int1);
    }

    public void llenarlita(){
        StringRequest request = new StringRequest(Request.Method.GET, Constant.ACTUALIZAR_NEGOCIO, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONArray negocio = new JSONArray(String.valueOf(object.getJSONArray("data")));
                    ids = new int[negocio.length()];
                    nom = new String[negocio.length()];
                    gir = new String[negocio.length()];
                    for (int i = 0; i<negocio.length(); i++){
                        JSONObject post = negocio.getJSONObject(i);
                        ids[i] = post.getInt("id");
                        nom[i] = post.getString("denominacion_soc");
                        gir[i] = post.getString("giro");
                        menu.add(new Menu(R.drawable.tienda,post.getString("denominacion_soc")+"",post.getString("giro")));
                    }
                    adapter = new MenuAdapter(this, menu);
                    lvMenu.setAdapter(adapter);
                }
                else{
                }
            }
            catch (JSONException e){
                Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
            }
        },error -> {
            Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
        }){

        };
        RequestQueue queue = Volley.newRequestQueue(Negocios.this);
        queue.add(request);

    }

    public void lista(){
        try{
            aBD=new Negocio(this,"Directorio",null,1);
            db = aBD.getReadableDatabase();
            if (db!=null) {
                Cursor cursor;
                if(buscar.getText().toString().isEmpty()){
                    cursor = db.rawQuery("SELECT id,denominacion_soc,giro FROM negocios",null);
                }
                else {
                    cursor = db.rawQuery("SELECT id,denominacion_soc,giro FROM negocios WHERE slug = '"+buscar.getText().toString()+"'",null);
                }
                if (cursor.moveToNext()){
                    ids = new int[cursor.getCount()];
                    nom = new String[cursor.getCount()];
                    gir = new String[cursor.getCount()];
                    int i = 0;
                    while (cursor.moveToNext()){
                        ids[i] = cursor.getInt(0);
                        nom[i] = cursor.getString(1);
                        gir[i] = cursor.getString(2);
                        menu.add(new Menu(R.drawable.tienda,nom[i]+"",gir[i]));
                        i++;
                    }//while
                    adapter = new MenuAdapter(this, menu);
                    lvMenu.setAdapter(adapter);
                }
                else{
                    Toast.makeText(this, "No hay datos.",Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }//if
            else {
                Toast.makeText(this, "No hay datos.",Toast.LENGTH_LONG).show();
            }
            db.close();
        }//try
        catch (Exception e) {
            Toast.makeText(this, e.getMessage()+"",Toast.LENGTH_LONG).show();
        }//catch
    }

    //Elaboración de la lista
    public class Menu {
        private int foto;
        private String nombre;
        private String giro;

        public Menu(int foto, String nombre, String giro) {
            this.foto = foto;
            this.nombre = nombre;
            this.giro = giro;
        }
        public String getNombre() {
            return nombre;
        }
        public void setNombre(String nombre) {
            this.nombre = nombre;
        }
        public String getGiro() {
            return giro;
        }
        public void setGiro(String giro) { this.giro = giro; }
        public int getFoto() { return foto; }
        public void setFoto(int foto) { this.foto = foto; }

    }
}
