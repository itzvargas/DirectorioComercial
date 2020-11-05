package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bd.AyudanteBD;

public class Negocios extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView negocios;
    EditText buscar;
    Button bBuscar, bBorrar;
    String palabra;
    String url = "http://elsitioKOCE.com/base";
    int[] ids;
    String[] nom;
    String[] gir;
    //int[] autorizado;
    private ArrayList<Menu> menu;
    private ListView lvMenu;
    private MenuAdapter adapter;
    AyudanteBD aBD;
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
        sinInternet();

        bBuscar.setOnClickListener(this);
        bBorrar.setOnClickListener(this);
        lvMenu.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_borrar){
            menu.clear();
            lvMenu.setAdapter(null);
            sinInternet();
        }
        if(v.getId() == R.id.btn_buscar){
            String busqueda = buscar.getText().toString();
            if(!busqueda.isEmpty()){
                menu.clear();
                lvMenu.setAdapter(null);
                sinInternet();
            }
            else{
                Toast.makeText(this, "Escribe lo que deseas buscar",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Mostrar datos del item seleccionado
        Bundle bolsa = new Bundle();
        bolsa.putString("URL",url+"/"+ids[position]);
        bolsa.putInt("ID",ids[position]);
        Intent int1 = new Intent(this,Negocio.class);
        int1.putExtras(bolsa);
        startActivity(int1);
    }

    public void sinInternet(){
        Toast.makeText(this, "Sin conexión a Internet.",Toast.LENGTH_LONG).show();
        try{
            aBD=new AyudanteBD(this,"Directorio",null,1);
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
            Toast.makeText(this, "Error.",Toast.LENGTH_LONG).show();
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
    public class MenuAdapter extends ArrayAdapter<Menu> {
        private Context context;
        private ArrayList<Menu> datos;

        public MenuAdapter(Context context, ArrayList<Menu> datos) {
            super(context, R.layout.activity_item_negocio, datos);
            // Guardamos los parámetros en variables de clase.
            this.context = context;
            this.datos = datos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // En primer lugar "inflamos" una nueva vista, que será la que se
            // mostrará en la celda del ListView. Para ello primero creamos el
            // inflater, y después inflamos la vista.
            LayoutInflater inflater = LayoutInflater.from(context);
            View item = inflater.inflate(R.layout.activity_item_negocio, null);


            // A partir de la vista, recogeremos los controles que contiene para
            // poder manipularlos.
            // Recogemos el ImageView y le asignamos una foto.
            ImageView foto = (ImageView) item.findViewById(R.id.img_negocioEditar);
            foto.setImageResource(datos.get(position).getFoto());

            // Recogemos el TextView para mostrar el nombre y establecemos el
            // nombre.
            TextView nombre = (TextView) item.findViewById(R.id.tv_nombre);
            nombre.setText(datos.get(position).getNombre());

            // Recogemos el TextView para mostrar el número de celda y lo
            // establecemos.
            TextView giro = (TextView) item.findViewById(R.id.tv_giro);
            giro.setText(datos.get(position).getGiro());

            // Devolvemos la vista para que se muestre en el ListView.
            return item;
        }
    }
}
