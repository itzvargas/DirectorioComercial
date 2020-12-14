package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
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

public class Ayudanos2 extends AppCompatActivity implements View.OnClickListener {

    EditText nombre,correo,coment,telef,asunto;
    Button enviar;
    String nom, corr, com, tel, asun;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayudanos2);
        setTitle(getString(R.string.menu_ayudanos));
        nombre = (EditText)findViewById(R.id.edt_nom);
        correo = (EditText)findViewById(R.id.edt_correo);
        coment = (EditText)findViewById(R.id.edt_comentario);
        telef = (EditText)findViewById(R.id.edt_telefono_ayudanos2);
        asunto = (EditText)findViewById(R.id.edt_asunto_ayudanos2);
        enviar = (Button)findViewById(R.id.btn_enviar_comentario);
        enviar.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);

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
        correo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!correo.getText().toString().isEmpty()){
                    correo.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        telef.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!telef.getText().toString().isEmpty()){
                    telef.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        asunto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!asunto.getText().toString().isEmpty()){
                    asunto.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        coment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!coment.getText().toString().isEmpty()){
                    coment.setError(null);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_enviar_comentario){
            nom = nombre.getText().toString();
            corr = correo.getText().toString();
            com = coment.getText().toString();
            tel = telef.getText().toString();
            asun = asunto.getText().toString();
            if(validate()){
                enviarComentario();
            }
        }
    }

    public boolean validate(){
        if(nom.isEmpty()){
            nombre.setError("Nombre requerido");
            return false;
        }
        if(corr.isEmpty()){
            correo.setError("Email requerido");
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(corr).matches()){
            correo.setError("Email invalido");
            return false;
        }
        if(tel.isEmpty()){
            telef.setError("Teléfono requerido");
            return false;
        }
        if(asun.isEmpty()){
            asunto.setError("Asunto requerido");
            return false;
        }
        if(com.isEmpty()){
            coment.setError("Comentario requerido");
            return false;
        }
        return true;
    }

    public void enviarComentario(){
        dialog.setMessage("Enviando...");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.AYUDANOS_A_MEJORAR, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Intent intent = new Intent(Ayudanos2.this, Inicio_invitado.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Gracias por tu comentario.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }
            catch (JSONException e){
                Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        },error -> {
            Toast.makeText(this, "Intentelo más tarde.",Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }){
            //Agregar parametros
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("nombre",nom);
                map.put("email",corr);
                map.put("telefono",tel);
                map.put("asunto",asun);
                map.put("comentario",com);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Ayudanos2.this);
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Ayudanos2.this, Inicio_invitado.class);
        startActivity(intent);
        finish();
    }
}
