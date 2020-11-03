package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    String nom,passw,confPass,em,telef,fac;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscribirme);
        setTitle(getString(R.string.menu_suscribirme));

        Bundle bolsaR = getIntent().getExtras();
        ruta = bolsaR.getString("RUTA");

        nombre = (EditText)findViewById(R.id.edt_nombreU);
        pass = (EditText)findViewById(R.id.edt_contraseñaU);
        email = (EditText)findViewById(R.id.edt_emailU);
        confirP = (EditText)findViewById(R.id.edt_confirmarContraseñaU);
        telefono = (EditText)findViewById(R.id.edt_telefonoU);
        face = (EditText)findViewById(R.id.edt_faceU);
        suscr = (Button)findViewById(R.id.btn_suscribirme);

        dialog = new ProgressDialog(Suscribirme.this);
        dialog.setCancelable(false);

        suscr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        nom = nombre.getText().toString();
        passw = pass.getText().toString();
        confPass = confirP.getText().toString();
        em = email.getText().toString();
        telef = telefono.getText().toString();
        fac = face.getText().toString();

        if(v.getId() == R.id.btn_suscribirme){
            if(!nom.isEmpty() && !passw.isEmpty() && !confPass.isEmpty() && !em.isEmpty()){
                if(passw == confPass) {
                    //Enviar los datos a la BD

                    Toast.makeText(this, "Ahora puedes iniciar sesión y disfrutar de los beneficios.", Toast.LENGTH_LONG).show();
                    //Redireccion
                    if (ruta.equals("login")) {
                        intent = new Intent(Suscribirme.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        intent = new Intent(Suscribirme.this, Inicio_invitado.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Toast.makeText(this, "La contraseña no coincide.",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(this, "Faltan campos por llenar",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void registrar(){
        dialog.setMessage("Verificando");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.REGISTRAR, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    JSONObject user = new JSONObject("user");
                    SharedPreferences userPref = getApplicationContext().getSharedPreferences("user",Login.MODE_PRIVATE);
                    SharedPreferences.Editor editor = userPref.edit();
                    editor.putString("token",object.getString("token"));
                    editor.putString("name",user.getString("name"));
                    editor.putString("email",user.getString("email"));
                    editor.putString("password",user.getString("password"));
                    editor.apply();
                    Toast.makeText(this, "Bienvenido " + user.getString("name"),Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Correo o contraseña incorrectos.",Toast.LENGTH_LONG).show();
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
                map.put("email",em);
                map.put("password",passw);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Suscribirme.this);
        queue.add(request);
    }
}
