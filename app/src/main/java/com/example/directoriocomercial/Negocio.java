package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import clases.Constant;

public class Negocio extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    ImageView logo;
    TextView titulo, verComent, etiqueta, existeComentarios, eliminarCo;
    ListView redes, comentarios;
    EditText comentarioNeg;
    Button enviar;
    RatingBar total, estrellaComent;
    int idNegocio;
    private ArrayList<MenuR> menuR;
    private MenuAdapterR adapterR;
    private ArrayList<MenuC> menuComent;
    private MenuAdapterC adapterComent;
    private SharedPreferences userPref;
    int idUsuario, idComentario;
    String token;
    String accion[] = new String[6];
    String tipo[] = new String[6];
    //int[] eliminarComentarios;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Bundle balsaNeg = this.getIntent().getExtras();
        //idNegocio = balsaNeg.getInt("IDNeg");
        idNegocio = getIntent().getIntExtra("ID",0);

        logo = (ImageView)findViewById(R.id.iv_logo);
        titulo = (TextView)findViewById(R.id.tv_titulo);
        total = (RatingBar)findViewById(R.id.rBar_total);
        verComent = (TextView)findViewById(R.id.txt_verComentarios);
        etiqueta = (TextView)findViewById(R.id.txt_comentario);
        existeComentarios = (TextView)findViewById(R.id.txt_existeComentarios);
        redes = (ListView)findViewById(R.id.lv_redes_sociales);
        comentarios = (ListView)findViewById(R.id.lv_comentarios);
        estrellaComent = (RatingBar)findViewById(R.id.rbar_comentario);
        comentarioNeg = (EditText)findViewById(R.id.edt_comentarioNeg);
        eliminarCo = (TextView)findViewById(R.id.txt_eliminarC);
        enviar = (Button)findViewById(R.id.btn_enviar_ComenarioNeg);

        titulo.setText(getIntent().getStringExtra("GIRO"));
        setTitle(getIntent().getStringExtra("DENOMINACION"));

        enviar.setOnClickListener(this);
        verComent.setOnClickListener(this);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(!userPref.getBoolean("isLoggedIn", false)){
            comentarioNeg.setVisibility(View.INVISIBLE);
            enviar.setVisibility(View.INVISIBLE);
            etiqueta.setVisibility(View.INVISIBLE);
            estrellaComent.setVisibility(View.INVISIBLE);
            eliminarCo.setVisibility(View.INVISIBLE);
        }
        idUsuario = userPref.getInt("id", 0);
        token = userPref.getString("token","");

        menuR=new ArrayList<MenuR>();
        menuComent=new ArrayList<MenuC>();

        redes.setOnItemClickListener(this);

        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        mostrarInfo();
        //Metodo para mostrar los comentarios del negocio
        //listaComentarios();
        redes.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                redes.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        redes.requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        redes.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;
                }
                return false;
            }
        });

        comentarios.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                comentarios.requestDisallowInterceptTouchEvent(true);
                int action = event.getActionMasked();
                switch (action) {
                    case MotionEvent.ACTION_UP:
                        comentarios.requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        comentarios.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;
                }
                return false;
            }
        });

        /*comentarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(idUsuario != 0) {
                    if (eliminarComentarios[position] != 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Negocio.this);
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
                }
            }
        });*/
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_enviar_ComenarioNeg){
            String c = comentarioNeg.getText().toString();
            if(!c.isEmpty()) {
                if(enviar.getText() == "Publicar") {
                    //Enviar los datos a la bd remota
                    enviarComentario();
                }
                if(enviar.getText() == "Editar"){
                    //Nueva actividad para editar comentario
                    Intent int1 = new Intent(Negocio.this,EditarComentario.class);
                    int1.putExtra("ID",idComentario);
                    int1.putExtra("IDuser",idUsuario);
                    int1.putExtra("Bar",estrellaComent.getRating());
                    int1.putExtra("Comentario",comentarioNeg.getText());
                    startActivity(int1);
                    finish();
                }
            }
            else {
                Toast.makeText(this, "Escribe tu comentario.",Toast.LENGTH_LONG).show();
            }
        }
        if(v.getId() == R.id.txt_eliminarC){
            AlertDialog.Builder builder = new AlertDialog.Builder(Negocio.this);
            builder.setMessage("¿Estás seguro de eliminar tu comentario?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    destryComentario();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();
        }
        if(v.getId() == R.id.txt_verComentarios){
            Intent int1 = new Intent(Negocio.this,VerComentarios.class);
            int1.putExtra("ID",idNegocio);
            startActivity(int1);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String tip = tipo[position];
        switch (tip){
            case "Email":
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",accion[position]+"", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Android APP - ");
                startActivity(Intent.createChooser(emailIntent,"Enviar con..."));
                break;
            case "Telefono":
                String numero = accion[position];
                try{
                    String dial = "tel:" +numero;
                    if(ActivityCompat.checkSelfPermission(
                            this, Manifest.permission.CALL_PHONE)
                            != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(Negocio.this,new String[]
                                { Manifest.permission.CALL_PHONE,},1000);
                    }else{
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
                catch (Exception e){
                    Toast.makeText(this,"Vuelve a intentarlo.", Toast.LENGTH_LONG).show();
                }
                break;
            case "Web":
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://"+accion[position])));
                break;
            case "Face":
                String FACEBOOK_URL = "https://www.facebook.com/" + accion[position];
                String FACEBOOK_PAGE_ID = accion[position];
                String uri;
                PackageManager packageManager = this.getPackageManager();
                try {
                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                    if (versionCode >= 3002850) { //versiones nuevas de facebook
                        uri = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                    } else { //versiones antiguas de fb
                        uri = "fb://page/" + FACEBOOK_PAGE_ID;
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    uri = FACEBOOK_URL; //normal web url
                }
                Intent int1 = new Intent(Intent.ACTION_VIEW);
                int1.setData(Uri.parse(uri));
                startActivity(int1);
                break;
            case "Insta":
                Uri uriI = Uri.parse("http://instagram.com/_u/"+accion[position]);
                Intent intent = new Intent(Intent.ACTION_VIEW, uriI);
                intent.setPackage("com.instagram.android");
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //No encontró la aplicación, abre la versión web.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+accion[position])));
                }
                break;
            case "Maps":
                Intent intent1 = new Intent(Negocio.this, Maps.class);
                intent1.putExtra("iframe",accion[position]);
                startActivity(intent1);
                break;
        }
    }

    public void destryComentario(){
        StringRequest request = new StringRequest(Request.Method.DELETE, Constant.ELIMINAR_COMENTARIO+idComentario+"/destroy", response -> {
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
        RequestQueue queue = Volley.newRequestQueue(Negocio.this);
        queue.add(request);
    }

    public void enviarComentario(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.PUBLICAR_COMENTARIO+idNegocio+"/create", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(this, "Se ha publicado tu comentario.",Toast.LENGTH_LONG).show();
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
                map.put("contenido",comentarioNeg.getText().toString());
                map.put("valoracion",estrellaComent.getRating()+"");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Negocio.this);
        queue.add(request);
    }

    public void mostrarInfo(){
        //Metodo para mostrar la foto del negocio
        dialog.setMessage("Cargando información");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.NEGOCIO_INDIVIDUAL+idNegocio, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject negocio = object.getJSONObject("negocio");
                    total.setRating((float) negocio.getDouble("valoracionPromedio"));
                    if(!(negocio.getString("image") + "").equals("null"))
                        Picasso.get().load(Constant.FOTO+negocio.getString("image")).into(logo);
                    JSONObject post = negocio.getJSONObject("contacto");
                    JSONObject direcc = negocio.getJSONObject("direccion");
                    if(!(post.getString("horario") + "").equals("null"))
                        titulo.append("\n"+post.getString("horario"));
                    int i = 0;
                    menuR.add(new MenuR(R.drawable.correo2, post.getString("email")+""));
                    accion[i] = post.getString("email")+"";
                    tipo[i] = "Email";
                    i++;
                    menuR.add(new MenuR(R.drawable.llamada2, post.getString("telefono")+""));
                    accion[i] = post.getString("telefono")+"";
                    tipo[i] = "Telefono";
                    i++;
                    if(!(post.getString("web") + "").equals("null")) {
                        menuR.add(new MenuR(R.drawable.web2, post.getString("web") + ""));
                        accion[i] = post.getString("web")+"";
                        tipo[i] = "Web";
                        i++;
                    }
                    if(!(post.getString("facebook") + "").equals("null")) {
                        menuR.add(new MenuR(R.drawable.face2, post.getString("facebook") + ""));
                        accion[i] = post.getString("facebook")+"";
                        tipo[i] = "Face";
                        i++;
                    }
                    if(!(post.getString("instagram") + "").equals("null")) {
                        menuR.add(new MenuR(R.drawable.insta2, post.getString("instagram") + ""));
                        accion[i] = post.getString("instagram")+"";
                        tipo[i] = "Insta";
                        i++;
                    }
                    if(!(direcc.getString("url_mapa") + "").equals("null")) {
                        menuR.add(new MenuR(R.drawable.maps, "¿Cómo llegar al negocio?"));
                        accion[i] = direcc.getString("url_mapa")+"";
                        tipo[i] = "Maps";
                    }
                    adapterR = new MenuAdapterR(this, menuR);
                    redes.setAdapter(adapterR);

                    //Comentarios
                    JSONArray comentario = new JSONArray(String.valueOf(negocio.getJSONArray("opiniones")));
                    if(comentario.length() == 0)
                        existeComentarios.setText("Sin Comentarios");
                    else {
                        if(comentario.length() == 1){
                            //eliminarComentarios = new int[comentario.length()];
                            for (int j = 0; j < 1; j++) {
                                JSONObject coment = comentario.getJSONObject(j);
                                if (coment.getInt("user_id") == idUsuario) {
                                    estrellaComent.setRating(coment.getInt("valoracion"));
                                    estrellaComent.setIsIndicator(true);
                                    comentarioNeg.setText(coment.getString("contenido"));
                                    enviar.setText("Editar");
                                    idComentario = coment.getInt("id");
                                    eliminarCo.setVisibility(View.VISIBLE);
                                    /*menuComent.add(new MenuC(R.drawable.usuario, coment.getString("autor") + "",
                                            coment.getString("fecha_creado"), coment.getString("contenido"), "Eliminar",
                                            coment.getInt("valoracion")));
                                    eliminarComentarios[j] = coment.getInt("id");*/
                                } else {
                                    menuComent.add(new MenuC(R.drawable.usuario, coment.getString("autor") + "",
                                            coment.getString("fecha_creado"), coment.getString("contenido"), "",
                                            coment.getInt("valoracion")));
                                    //eliminarComentarios[j] = 0;
                                }
                            }
                            adapterComent = new MenuAdapterC(this, menuComent);
                            comentarios.setAdapter(adapterComent);
                        }
                        else {
                            //eliminarComentarios = new int[comentario.length()];
                            for (int j = 0; j < 2; j++) {
                                JSONObject coment = comentario.getJSONObject(j);
                                if (coment.getInt("user_id") == idUsuario) {
                                    estrellaComent.setRating(coment.getInt("valoracion"));
                                    comentarioNeg.setText(coment.getString("contenido"));
                                    enviar.setText("Editar");
                                    idComentario = coment.getInt("id");
                                    eliminarCo.setVisibility(View.VISIBLE);
                                    estrellaComent.setIsIndicator(true);
                                    /*menuComent.add(new MenuC(R.drawable.usuario, coment.getString("autor") + "",
                                            coment.getString("fecha_creado"), coment.getString("contenido"), "Eliminar",
                                            coment.getInt("valoracion")));
                                    eliminarComentarios[j] = coment.getInt("id");*/
                                } else {
                                    menuComent.add(new MenuC(R.drawable.usuario, coment.getString("autor") + "",
                                            coment.getString("fecha_creado"), coment.getString("contenido"), "",
                                            coment.getInt("valoracion")));
                                    //eliminarComentarios[j] = 0;
                                }
                            }
                            adapterComent = new MenuAdapterC(this, menuComent);
                            comentarios.setAdapter(adapterComent);
                        }
                    }
                }
                else{
                    Toast.makeText(this, "No hay negocios",Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
            catch (JSONException e){
                Toast.makeText(this, e.getMessage()+"",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        },error -> {
            Toast.makeText(this, error.getMessage()+"",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
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
