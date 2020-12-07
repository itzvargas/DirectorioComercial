package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import clases.Constant;

public class EditarNegocio extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    //private LocationManager locManager;
    //private Location loc;
    CheckBox sociales,ubicacion;
    //int id_propietario[] = {R.id.edt_nombreUsuario,R.id.edt_telefonoUsuario,R.id.edt_emailUsuario,R.id.edt_fechaNacUsuario,R.id.edt_faceUsuario};
    int id_negocio[] = {R.id.edt_denominacionEditar,R.id.edt_giroEditar,R.id.edt_descripcionEditar,R.id.edt_productosEditar};
    int id_domicilio[] = {R.id.edt_calleEditar,R.id.edt_noExtEditar,R.id.edt_noIntEditar,R.id.edt_coloniaEditar,R.id.edt_codigoPosEditar,R.id.edt_municipioEditar,R.id.edt_estadoEditar};
    int id_contacto[] = {R.id.edt_emailNegEditar,R.id.edt_telefonoNegEditar,R.id.edt_horarioEditar};
    EditText pagina,faceN,instaN;
    //EditText propietario[] = new EditText[5];
    EditText negocio[] = new EditText[4];
    EditText domicilio[] = new EditText[7];
    EditText contacto[] = new EditText[3];
    Button inscr,verUbic;
    ImageView image;
    TextView buscarImage;
    private SharedPreferences userPref;
    private ProgressDialog dialog;
    //Propietario
    //String nombre,telefono,email,fecha,face;
    //Negocio
    String denom,giro,descrip="",producto="";
    //Domicilio
    String calle,noI,noE="",colonia,codigo,munic,estado;
    //Contacto
    String emailN,telefonoN,horario="",page="",faceNe="",instaNe="";
    int idNegocio;
    Bitmap bitmap = null;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_negocio);
        setTitle("Editar mi negocio");
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        idNegocio = getIntent().getIntExtra("ID_N",0);
        for (int i = 0; i<4; i++)
            negocio[i] = (EditText)findViewById(id_negocio[i]);
        for (int i = 0; i<7; i++)
            domicilio[i] = (EditText)findViewById(id_domicilio[i]);
        for (int i = 0; i<3; i++)
            contacto[i] = (EditText)findViewById(id_contacto[i]);
        sociales = (CheckBox)findViewById(R.id.chk_redesEditar);
        ubicacion = (CheckBox)findViewById(R.id.chk_ubicacionEditar);
        pagina = (EditText)findViewById(R.id.edt_paginaWEditar);
        faceN = (EditText)findViewById(R.id.edt_faceNegEditar);
        instaN = (EditText)findViewById(R.id.edt_instaNegEditar);
        inscr = (Button)findViewById(R.id.btn_editar);
        verUbic = (Button)findViewById(R.id.btn_ubicacionEditar);
        image = (ImageView)findViewById(R.id.logoNegEditar);
        buscarImage = (TextView)findViewById(R.id.seleccionarLogoEditar);
        

        ubicacion.setOnClickListener(this);
        verUbic.setOnClickListener(this);
        inscr.setOnClickListener(this);
        sociales.setOnClickListener(this);
        buscarImage.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        mostrarDatos();
        negocio[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!negocio[0].getText().toString().isEmpty()){
                    negocio[0].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        negocio[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!negocio[1].getText().toString().isEmpty()){
                    negocio[1].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[0].getText().toString().isEmpty()){
                    domicilio[0].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[1].getText().toString().isEmpty()){
                    domicilio[1].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[3].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[3].getText().toString().isEmpty()){
                    domicilio[3].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[4].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[4].getText().toString().isEmpty()){
                    domicilio[4].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[5].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[5].getText().toString().isEmpty()){
                    domicilio[5].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        domicilio[6].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!domicilio[6].getText().toString().isEmpty()){
                    domicilio[6].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contacto[0].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!contacto[0].getText().toString().isEmpty()){
                    contacto[0].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        contacto[1].addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!contacto[1].getText().toString().isEmpty()){
                    contacto[1].setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.opciones_negocios, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent;
        switch (id){
            case R.id.negocio_editar:
                intent = new Intent(EditarNegocio.this, EditarNegocio.class);
                intent.putExtra("ID_N",idNegocio);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.chk_redesEditar:
                if(sociales.isChecked()) {
                    pagina.setVisibility(View.VISIBLE);
                    faceN.setVisibility(View.VISIBLE);
                    instaN.setVisibility(View.VISIBLE);
                }
                else {
                    pagina.setVisibility(View.INVISIBLE);
                    faceN.setVisibility(View.INVISIBLE);
                    instaN.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.chk_ubicacionEditar:
                if(ubicacion.isChecked()) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                    } else {
                        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Localizacion Local = new Localizacion();
                        Local.setMainActivity(this);
                        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                        if (!gpsEnabled) {
                            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(settingsIntent);
                        }
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
                            return;
                        }
                        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
                        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);
                    }
                    //locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //url = "<iframe src=\"https://maps.google.com/maps?q="+ loc.getLatitude()+","+ loc.getLongitude()+"&hl=es&z=14&amp;output=embed\" width=\"600\" height=\"450\" frameborder=\"0\" style=\"border:0;\" allowfullscreen=\"\" aria-hidden=\"false\" tabindex=\"0\"></iframe>";
                }
                else {
                    url = "";
                    verUbic.setEnabled(false);
                }
                break;
            case R.id.btn_editar:
                denom = negocio[0].getText().toString();
                giro = negocio[1].getText().toString();
                descrip = negocio[2].getText().toString();
                producto = negocio[3].getText().toString();

                calle = domicilio[0].getText().toString();
                noE = domicilio[1].getText().toString();
                noI = domicilio[2].getText().toString();
                colonia = domicilio[3].getText().toString();
                codigo = domicilio[4].getText().toString();
                munic = domicilio[5].getText().toString();
                estado = domicilio[6].getText().toString();

                emailN = contacto[0].getText().toString();
                telefonoN = contacto[1].getText().toString();
                horario = contacto[2].getText().toString();

                page = pagina.getText().toString();
                faceNe = faceN.getText().toString();
                instaNe = instaN.getText().toString();

                if(validate()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("¿Estás seguro de modificar la información?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editarDatos();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    builder.show();
                }
                break;
            case R.id.seleccionarLogoEditar:
                cargarImagen();
                break;
            case R.id.btn_ubicacionEditar:
                Intent intent1 = new Intent(EditarNegocio.this, Maps.class);
                intent1.putExtra("iframe",url+"");
                startActivity(intent1);
                //Toast.makeText(this, url+"",Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("denom",negocio[0].getText().toString());
        outState.putString("giro",negocio[1].getText().toString());
        outState.putString("descrip",negocio[2].getText().toString());
        outState.putString("producto",negocio[3].getText().toString());

        outState.putString("calle",domicilio[0].getText().toString());
        outState.putString("noE",domicilio[1].getText().toString());
        outState.putString("noI",domicilio[2].getText().toString());
        outState.putString("colonia",domicilio[3].getText().toString());
        outState.putString("codigo",domicilio[4].getText().toString());
        outState.putString("munic",domicilio[5].getText().toString());
        outState.putString("estado",domicilio[6].getText().toString());

        outState.putString("emailN",contacto[0].getText().toString());
        outState.putString("telefonoN",contacto[1].getText().toString());
        outState.putString("horario",contacto[2].getText().toString());

        outState.putString("page",pagina.getText().toString());
        outState.putString("faceNe",faceN.getText().toString());
        outState.putString("instaNe",instaN.getText().toString());

        if(sociales.isChecked())
            outState.putBoolean("redes",true);
        else
            outState.putBoolean("redes",false);
        if(ubicacion.isChecked())
            outState.putBoolean("ubicacion",true);
        else
            outState.putBoolean("ubicacion",true);
        outState.putString("url",url);
        if(bitmap!=null)
            outState.putString("bit",bitmap.toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            negocio[0].setText(savedInstanceState.getString("denom", ""));
            negocio[1].setText(savedInstanceState.getString("giro", ""));
            negocio[2].setText(savedInstanceState.getString("descrip", ""));
            negocio[3].setText(savedInstanceState.getString("producto", ""));

            domicilio[0].setText(savedInstanceState.getString("calle", ""));
            domicilio[1].setText(savedInstanceState.getString("noE", ""));
            domicilio[2].setText(savedInstanceState.getString("noI", ""));
            domicilio[3].setText(savedInstanceState.getString("colonia", ""));
            domicilio[4].setText(savedInstanceState.getString("codigo", ""));
            domicilio[5].setText(savedInstanceState.getString("munic", ""));
            domicilio[6].setText(savedInstanceState.getString("estado", ""));

            contacto[0].setText(savedInstanceState.getString("emailN", ""));
            contacto[1].setText(savedInstanceState.getString("telefonoN", ""));
            contacto[2].setText(savedInstanceState.getString("horario", ""));

            pagina.setText(savedInstanceState.getString("page", ""));
            faceN.setText(savedInstanceState.getString("faceNe", ""));
            instaN.setText(savedInstanceState.getString("instaNe", ""));

            sociales.setChecked(savedInstanceState.getBoolean("redes",false));
            ubicacion.setChecked(savedInstanceState.getBoolean("ubicacion",false));
            url = savedInstanceState.getString("url", "");
            if(!savedInstanceState.getString("bit", "null").equals("null"))
                bitmap = StringToBitMap(savedInstanceState.getString("bit", "null"));
            if(bitmap != null)
                image.setImageBitmap(bitmap);
        }
    }

    public Bitmap StringToBitMap(String encodedString){
        try{
            byte [] encodeByte = Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    public void cargarImagen(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione"),10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == 10){
            Uri path = data.getData();
            //image.setImageURI(path);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validate(){
        if(denom.isEmpty()){
            negocio[0].setError("Denominación requerida");
            return false;
        }
        if(giro.isEmpty()){
            negocio[1].setError("Giro requerido");
            return false;
        }
        if(calle.isEmpty()){
            domicilio[0].setError("Calle requerida");
            return false;
        }
        if(noE.isEmpty()){
            domicilio[1].setError("Numero exterior requerido");
            return false;
        }
        if(colonia.isEmpty()){
            domicilio[3].setError("Colonia requerida");
            return false;
        }
        if(codigo.isEmpty()){
            domicilio[4].setError("Código postal requerido");
            return false;
        }
        if(munic.isEmpty()){
            domicilio[5].setError("Municipio requerido");
            return false;
        }
        if(estado.isEmpty()){
            domicilio[6].setError("Estado requerido");
            return false;
        }
        if(emailN.isEmpty()){
            contacto[0].setError("Email requerido");
            return false;
        }
        if(telefonoN.isEmpty()){
            contacto[1].setError("Teléfono requerido");
            return false;
        }
        if(telefonoN.length() < 10){
            contacto[1].setError("Teléfono debe ser igual a 10 o 12 caracteres");
            return false;
        }
        if(telefonoN.length() == 11 ){
            contacto[1].setError("Teléfono debe ser igual a 10 o 12 caracteres");
            return false;
        }
        return true;
    }

    public void mostrarDatos(){
        dialog.setMessage("Mostrando datos");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.NEGOCIO_INDIVIDUAL+idNegocio, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject neg = object.getJSONObject("negocio");
                    JSONObject contact = neg.getJSONObject("contacto");
                    JSONObject direcc = neg.getJSONObject("direccion");

                    negocio[0].setText((neg.getString("denominacion_soc") + ""));
                    negocio[1].setText((neg.getString("giro") + ""));
                    if(!(neg.getString("descripcion") + "").equals("null"))
                        negocio[2].setText((neg.getString("descripcion")));
                    if(!(neg.getString("principales_prod") + "").equals("null"))
                        negocio[3].setText((neg.getString("principales_prod")));
                    if(!(neg.getString("image") + "").equals("null"))
                        Picasso.get().load(Constant.FOTO+neg.getString("image")).into(image);

                    domicilio[0].setText((direcc.getString("calle") + ""));
                    domicilio[1].setText((direcc.getString("no_ext") + ""));
                    if(!(direcc.getString("no_int") + "").equals("null"))
                        domicilio[2].setText((direcc.getString("no_int")));
                    domicilio[3].setText((direcc.getString("colonia") + ""));
                    domicilio[4].setText((direcc.getString("cp") + ""));
                    domicilio[5].setText((direcc.getString("municipio") + ""));
                    domicilio[6].setText((direcc.getString("estado") + ""));

                    contacto[0].setText((contact.getString("email") + ""));
                    contacto[1].setText((contact.getString("telefono") + ""));
                    if(!(contact.getString("horario") + "").equals("null"))
                        contacto[2].setText((contact.getString("horario")));

                    if(!(contact.getString("web") + "").equals("null"))
                        pagina.setText((contact.getString("web")));
                    if(!(contact.getString("facebook") + "").equals("null"))
                        faceN.setText((contact.getString("facebook")));
                    if(!(contact.getString("instagram") + "").equals("null"))
                        instaN.setText((contact.getString("instagram")));
                }
                else{
                    Toast.makeText(this, "No hay",Toast.LENGTH_LONG).show();
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
        RequestQueue queue = Volley.newRequestQueue(EditarNegocio.this);
        queue.add(request);
    }

    public void editarDatos(){
        dialog.setMessage("Editando negocio");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.PUT, Constant.EDITAR_NEGOCIO+idNegocio+"/update", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(this, "Se modificó con éxito.",Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Hubo un error, revisa tu información e intenta de nuevo.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        }, error -> {
            Toast.makeText(this, error.getMessage(),Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                HashMap<String, String> map = new HashMap<>();
                map.put("token",userPref.getString("token",""));
                //map.put("user_id",userPref.getInt("id",0)+"");
                map.put("denominacion_soc",denom);
                //slug
                map.put("giro",giro);
                //if(!descrip.isEmpty())
                map.put("descripcion",descrip);
                map.put("principales_prod",producto);
                map.put("image",bitmapToString(bitmap));
                //map.put("autorizado",0+"");
                map.put("calle",calle);
                map.put("no_ext",noE);
                //if(!noI.isEmpty())
                map.put("no_int",noI);
                map.put("colonia",colonia);
                map.put("cp",codigo);
                map.put("municipio",munic);
                map.put("estado",estado);
                if(!url.equals(""))
                    map.put("url_mapa",url);
                map.put("email",emailN);
                map.put("telefono",telefonoN);
                map.put("web",page);
                //if(!horario.isEmpty())
                map.put("horario",horario);
                //if(!faceNe.isEmpty())
                map.put("facebook",faceNe);
                //if(!instaNe.isEmpty())
                map.put("instagram",instaNe);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(EditarNegocio.this);
        queue.add(request);
    }

    private String bitmapToString(Bitmap bitmap) {
        if(bitmap!=null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
            byte[] array = byteArrayOutputStream.toByteArray();
            return Base64.encodeToString(array, Base64.DEFAULT);
        }
        return "";
    }

    public class Localizacion implements LocationListener {
        EditarNegocio mainActivity;
        public EditarNegocio getMainActivity() {
            return mainActivity;
        }
        public void setMainActivity(EditarNegocio mainActivity) {
            this.mainActivity = mainActivity;
        }
        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion
            loc.getLatitude();
            loc.getLongitude();
            url = "<iframe src=\"https://maps.google.com/maps?q="+ loc.getLatitude()+
                    ","+ loc.getLongitude()+ "&hl=es&z=14&amp;output=embed\" width=\"600\" height=\"450\" frameborder=\"0\" style=\"border:0;\" allowfullscreen=\"\" aria-hidden=\"false\" tabindex=\"0\"></iframe>";
            verUbic.setEnabled(true);
        }
        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //mensaje1.setText("GPS Desactivado");
        }
        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
            //mensaje1.setText("GPS Activado");
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
}
