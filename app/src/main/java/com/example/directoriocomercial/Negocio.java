package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
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
import bd.Contacto;
import clases.Constant;

public class Negocio extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    ImageView logo;
    TextView titulo, giro, etiqueta, existeComentarios;
    ListView redes, comentarios;
    EditText coment;
    Button enviar;
    int idNegocio;
    private ArrayList<MenuR> menuR;
    private MenuAdapterR adapterR;
    private ArrayList<MenuC> menuComent;
    private MenuAdapterC adapterComent;
    private SharedPreferences userPref;
    int idUsuario;
    String token;
    String accion[] = new String[6];
    String tipo[] = new String[6];
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
        giro = (TextView)findViewById(R.id.tv_giro_negocio);
        etiqueta = (TextView)findViewById(R.id.txt_comentario);
        existeComentarios = (TextView)findViewById(R.id.txt_existeComentarios);
        redes = (ListView)findViewById(R.id.lv_redes_sociales);
        comentarios = (ListView)findViewById(R.id.lv_comentarios);
        coment = (EditText)findViewById(R.id.edt_comentarioNeg);
        enviar = (Button)findViewById(R.id.btn_enviar_ComenarioNeg);

        titulo.setText(getIntent().getStringExtra("GIRO"));
        setTitle(getIntent().getStringExtra("DENOMINACION"));

        enviar.setOnClickListener(this);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        if(!userPref.getBoolean("isLoggedIn", false)){
            coment.setVisibility(View.INVISIBLE);
            enviar.setVisibility(View.INVISIBLE);
            etiqueta.setVisibility(View.INVISIBLE);
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
                    };
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

    public void enviarComentario(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.PUBLICAR_COMENTARIO, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(this, "Se ha publicado tu comentario.",Toast.LENGTH_LONG).show();
                    coment.setText("");
                    menuComent.clear();
                    comentarios.setAdapter(null);
                    //listaComentarios();
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
                map.put("token",idUsuario+"");
                map.put("contenido",coment.getText().toString());
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
                    if(!(negocio.getString("image") + "").equals("null"))
                        Picasso.get().load(Constant.FOTO+negocio.getString("image")).into(logo);
                    JSONObject post = negocio.getJSONObject("contacto");
                    JSONObject direcc = negocio.getJSONObject("direccion");
                    if(!(post.getString("horario") + "").equals("null"))
                        giro.setText(post.getString("horario")+"");
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

                    JSONArray comentario = new JSONArray(String.valueOf(negocio.getJSONArray("comentarios")));
                    if(comentario.length() == 0)
                        existeComentarios.setText("Sin Comentarios");
                    for (int j = 0; j<comentario.length(); j++){
                        JSONObject coment = comentario.getJSONObject(j);
                        menuComent.add(new MenuC(R.drawable.usuario,coment.getString("autor")+"",coment.getString("created_at"),coment.getString("contenido")));
                    }
                    adapterComent = new MenuAdapterC(this,menuComent);
                    comentarios.setAdapter(adapterComent);
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
