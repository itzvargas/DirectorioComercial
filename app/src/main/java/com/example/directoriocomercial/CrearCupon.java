package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import clases.Constant;
import clases.DatePickerFragment;

public class CrearCupon extends AppCompatActivity implements View.OnClickListener {

    ImageView banner;
    TextView seleccionar;
    EditText titulo, descripcion, fecha;
    Button crear;
    private SharedPreferences userPref;
    private ProgressDialog dialog;
    int idNegocio;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cupon);
        setTitle("Crear cupón");
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        idNegocio = getIntent().getIntExtra("ID_N",0);

        banner = (ImageView)findViewById(R.id.img_bannerCupon);
        seleccionar = (TextView)findViewById(R.id.txt_seleccionarBannerCupon);
        titulo = (EditText)findViewById(R.id.edt_tituloCupon);
        descripcion = (EditText)findViewById(R.id.edt_descripcionCupon);
        fecha = (EditText)findViewById(R.id.edt_fechaVigCupon);
        crear = (Button)findViewById(R.id.btn_crearCupon);

        seleccionar.setOnClickListener(this);
        crear.setOnClickListener(this);
        fecha.setOnClickListener(this);

        titulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (titulo.getText().toString().isEmpty()){
                    titulo.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        descripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (descripcion.getText().toString().isEmpty()){
                    descripcion.setError(null);
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
                intent = new Intent(CrearCupon.this, EditarNegocio.class);
                intent.putExtra("ID_N",idNegocio);
                startActivity(intent);
                finish();
                break;
            case R.id.negocio_promocion:
                intent = new Intent(CrearCupon.this, CrearPromocion.class);
                intent.putExtra("ID_N",idNegocio);
                startActivity(intent);
                finish();
                break;
            case R.id.negocio_cupon:
                intent = new Intent(CrearCupon.this, CrearCupon.class);
                intent.putExtra("ID_N",idNegocio);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_seleccionarBannerCupon:
                cargarImagen();
                break;
            case R.id.btn_crearCupon:
                if(validate()){
                    //Crear promocion
                }
                break;
            case R.id.edt_fechaVigCupon:
                fecha.setError(null);
                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = year + "-" + twoDigits(month+1) + "-" + twoDigits(day);
                fecha.setText(selectedDate);
            }
        });

        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
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
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                banner.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public boolean validate(){
        if(titulo.getText().toString().isEmpty()){
            titulo.setError("Título requerido");
            return false;
        }
        if(descripcion.getText().toString().isEmpty()){
            descripcion.setError("Descripción requerida");
            return false;
        }
        if(fecha.getText().toString().isEmpty()){
            fecha.setError("Fecha requerida");
            return false;
        }
        return true;
    }

    public void crearPromo(){
        dialog.setMessage("Creando cupón...");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.CREAR_CUPON, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(this, "Se ha creado la promoción.",Toast.LENGTH_SHORT).show();
                    finish();
                    overridePendingTransition(0, 0);
                    startActivity(getIntent());
                    overridePendingTransition(0, 0);
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
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("titulo",titulo.getText().toString());
                map.put("descripcion",descripcion.getText().toString());
                map.put("fechaVigencia",fecha.getText().toString());
                map.put("image",bitmapToString(bitmap));
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(CrearCupon.this);
        queue.add(request);
    }
}
