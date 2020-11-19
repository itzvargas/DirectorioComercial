package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import clases.Constant;

public class Suscribirme extends AppCompatActivity implements View.OnClickListener {

    EditText nombre,pass,confirP,email,telefono,face;
    Button suscr;
    String ruta;
    String nom,passw,confPass,em,telef="",fac="";
    private ProgressDialog dialog;
    String rol = "2",enterado;
    Spinner enteradoU;
    ArrayAdapter<String> adEnterado;
    String[] maneras = {"¿Cómo te enteraste de nosotros?","Por referencias personales","Por redes sociales","Por una invitación de correo electrónico",
            "Por un anuncio","Otro"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscribirme);
        setTitle(getString(R.string.menu_suscribirme));

        Bundle bolsaR = getIntent().getExtras();
        ruta = bolsaR.getString("RUTA");

        nombre = (EditText)findViewById(R.id.edt_nombreU);
        pass = (EditText)findViewById(R.id.edt_contrasenaU);
        email = (EditText)findViewById(R.id.edt_emailU);
        confirP = (EditText)findViewById(R.id.edt_confirmarContrasenaU);
        telefono = (EditText)findViewById(R.id.edt_telefonoU);
        face = (EditText)findViewById(R.id.edt_faceU);
        suscr = (Button)findViewById(R.id.btn_suscribirme);
        enteradoU = (Spinner)findViewById(R.id.spinner_enterado);
        adEnterado = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,maneras);
        enteradoU.setAdapter(adEnterado);

        dialog = new ProgressDialog(Suscribirme.this);
        dialog.setCancelable(false);

        suscr.setOnClickListener(this);

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!nombre.getText().toString().isEmpty()){
                    nombre.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!pass.getText().toString().isEmpty()){
                    pass.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        confirP.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!confirP.getText().toString().isEmpty()){
                    confirP.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!email.getText().toString().isEmpty()){
                    email.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        nom = nombre.getText().toString();
        passw = pass.getText().toString();
        confPass = confirP.getText().toString();
        em = email.getText().toString();
        telef = telefono.getText().toString();
        fac = face.getText().toString();

        if(v.getId() == R.id.btn_suscribirme){
            if(validate()){
                //Enviar los datos a la BD
                switch (enteradoU.getSelectedItemPosition()) {
                    case 0:
                        enterado = "1";
                        break;
                    case 1:
                        enterado = "2";
                        break;
                    case 2:
                        enterado = "3";
                        break;
                    case 3:
                        enterado = "4";
                        break;
                    case 4:
                        enterado = "5";
                        break;
                    case 5:
                        enterado = "6";
                        break;
                }
                registrar();
            }
        }
    }

    private boolean validate(){
        if(nom.isEmpty()){
            nombre.setError("Nombre requerido");
            return false;
        }
        if(em.isEmpty()){
            email.setError("Email requerido");
            return false;
        }
        if(passw.isEmpty()){
            pass.setError("Contraseña requerida");
            return false;
        }
        if(confPass.isEmpty()){
            confirP.setError("Confirma contraseña");
            return false;
        }
        if(passw.length() < 8){
            pass.setError("Contraseña igual o mayor a 8 caracteres");
            return false;
        }
        if(!passw.equals(confPass)){
            pass.setError("La contraseña no coincide");
            return false;
        }
        return true;
    }

    public void reedireccion(){
        Intent intent;
        Toast.makeText(this, "Ahora puedes iniciar sesión y disfrutar de los beneficios.", Toast.LENGTH_LONG).show();
        //Redireccion
        intent = new Intent(Suscribirme.this, Login.class);
        startActivity(intent);
        finish();
    }

    public void registrar(){
        dialog.setMessage("Registrando");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTRAR, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject user = new JSONObject(String.valueOf(object.getJSONObject("user")));
                    SharedPreferences userPref = getApplicationContext().getSharedPreferences("user",Suscribirme.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token",object.getString("token"));
                    editor.putString("name",user.getString("name"));
                    editor.putString("email",user.getString("email"));
                    editor.putString("password",user.getString("password"));
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();
                    reedireccion();
                }
                else {
                    Toast.makeText(this, "Sin conexión a Internet. Intentelo más tarde.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
            }
            dialog.dismiss();
        },error -> {
            Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("name",nom);
                map.put("email",em);
                map.put("password",passw);
                //map.put("rol_id",rol);
                map.put("enterado_id",enterado);
                map.put("telefono",telef);
                map.put("face",fac);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Suscribirme.this);
        queue.add(request);
    }
}
