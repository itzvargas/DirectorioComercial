package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

import bd.AyudanteBD;
import bd.Contacto;
import bd.Direccion;
import clases.NombreUsuario;

public class Negocio extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    ImageView logo;
    TextView titulo, giro, etiqueta;
    ListView redes, comentarios;
    EditText coment;
    Button enviar;
    int idNegocio;
    String urlNegocio;
    private ArrayList<MenuR> menu;
    private MenuAdapterR adapter;
    private ArrayList<MenuC> menuComent;
    private MenuAdapterC adapterComent;
    String modo = "";
    int sesion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        logo = (ImageView)findViewById(R.id.iv_logo);
        titulo = (TextView)findViewById(R.id.tv_titulo);
        giro = (TextView)findViewById(R.id.tv_giro_negocio);
        etiqueta = (TextView)findViewById(R.id.txt_comentario);
        redes = (ListView)findViewById(R.id.lv_redes_sociales);
        comentarios = (ListView)findViewById(R.id.lv_comentarios);
        coment = (EditText)findViewById(R.id.edt_comentarioNeg);
        enviar = (Button)findViewById(R.id.btn_enviar_ComenarioNeg);

        idSesion();
        enviar.setOnClickListener(this);
        if(sesion != 0){
            coment.setVisibility(View.INVISIBLE);
            enviar.setVisibility(View.INVISIBLE);
            etiqueta.setVisibility(View.INVISIBLE);
        }

        Bundle bolsaR = getIntent().getExtras();
        idNegocio = bolsaR.getInt("ID");
        urlNegocio = bolsaR.getString("URL");
        mostrarInfo();
        //URL que selecciona todos los comentarios del id del negocio
        String urlComent = "" + idNegocio;
        Tarea tC=new Tarea();
        tC.execute(urlComent);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_enviar_ComenarioNeg){
            String c = coment.getText().toString();
            if(!c.isEmpty()) {
                AyudanteBD aBD;
                SQLiteDatabase db = null;
                try {
                    aBD = new AyudanteBD(this, "Directorio", null, 1);
                    db = aBD.getReadableDatabase();
                    if (db != null) {
                        Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE actividad='activo'", null);
                        if (cursor.moveToNext()) {
                            int id = cursor.getInt(0);
                            //Enviar los datos a la bd remota

                            Toast.makeText(this, "Se ha enviado tu comentario.",Toast.LENGTH_LONG).show();
                            cursor.close();
                            db.close();
                        } else {
                            cursor.close();
                            db.close();
                        }
                    }//if
                    else {
                    }
                }//try
                catch (Exception e) {
                    Toast.makeText(this, "Intentalo más tarde.",Toast.LENGTH_LONG).show();
                }//catch
            }
            else {
                Toast.makeText(this, "Escribe tu comentario.",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void idSesion(){
        AyudanteBD aBD;
        SQLiteDatabase db = null;
        try {
            aBD = new AyudanteBD(this, "Directorio", null, 1);
            db = aBD.getReadableDatabase();
            if (db != null) {
                Cursor cursor = db.rawQuery("SELECT id FROM usuarios WHERE actividad='activo'", null);
                if (cursor.moveToNext()) {
                    sesion = 1;
                    cursor.close();
                    db.close();
                } else {
                    cursor.close();
                    db.close();
                }
            }//if
            else {
            }
        }//try
        catch (Exception e) {
        }//catch
    }

    public void sinInernet(){
        Toast.makeText(this, "Sin conexión a Internet.",Toast.LENGTH_LONG).show();
    }

    public void mostrarInfo(){
        Tarea tM=new Tarea();
        tM.execute(urlNegocio);
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
        modo = "Mostrar Comentarios";
    }

    //Consulta a la pagina para la foto
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
                if(modo.isEmpty()) {
                    for (int i = 0; i < arreglo.length(); i++) {
                        JSONObject renglon = arreglo.getJSONObject(i);
                        String foto = renglon.getString("foto");
                        Picasso.get().load(foto).into(logo);
                    }
                }
                else{
                    String name = "";
                    NombreUsuario nu = new NombreUsuario();
                    for (int i = 0; i < arreglo.length(); i++) {
                        JSONObject renglon = arreglo.getJSONObject(i);
                        //Obtener el nombre del que escribio el comentario para el ID del negocio seleccionado
                        name = nu.getNombreUsuario("https:.../"+idNegocio);
                        menuComent.add(new MenuC(R.drawable.usuario, name+"",renglon.getString("created_at")+"",renglon.getString("contenido")+""));
                    }
                }
            }
            catch(Exception e){
                //Si no hay internet
                sinInernet();
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
    public class MenuAdapterC extends ArrayAdapter<MenuC> {
        private Context context;
        private ArrayList<MenuC> datos;

        public MenuAdapterC(Context context, ArrayList<MenuC> datos) {
            super(context, R.layout.activity_item_comentario, datos);
            // Guardamos los parámetros en variables de clase.
            this.context = context;
            this.datos = datos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View item = inflater.inflate(R.layout.activity_item_comentario, null);
            ImageView foto = (ImageView) item.findViewById(R.id.iv_usuario);
            foto.setImageResource(datos.get(position).getFoto());
            TextView nombre = (TextView) item.findViewById(R.id.tv_nombreComent);
            nombre.setText(datos.get(position).getNombre());
            TextView fecha = (TextView) item.findViewById(R.id.tv_fechaComent);
            fecha.setText(datos.get(position).getFecha());
            TextView coment = (TextView) item.findViewById(R.id.tv_coment);
            coment.setText(datos.get(position).getComentario());
            return item;
        }
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
    public class MenuAdapterR extends ArrayAdapter<MenuR> {
        private Context context;
        private ArrayList<MenuR> datos;

        public MenuAdapterR(Context context, ArrayList<MenuR> datos) {
            super(context, R.layout.activity_item_redes, datos);
            // Guardamos los parámetros en variables de clase.
            this.context = context;
            this.datos = datos;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View item = inflater.inflate(R.layout.activity_item_redes, null);
            ImageView foto = (ImageView) item.findViewById(R.id.iv_redS);
            foto.setImageResource(datos.get(position).getFoto());
            TextView nombre = (TextView) item.findViewById(R.id.tv_redS);
            nombre.setText(datos.get(position).getNombre());
            return item;
        }
    }
}
