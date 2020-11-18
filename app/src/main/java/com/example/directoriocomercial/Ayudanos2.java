package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

public class Ayudanos2 extends AppCompatActivity implements View.OnClickListener {

    EditText nombre,correo,coment,telef,asunto;
    Button enviar;
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
    }

    @Override
    public void onClick(View v) {
        String nom, corr, com, tel, asun;
        Intent intent;
        if(v.getId() == R.id.btn_enviar_comentario){
            nom = nombre.getText().toString();
            corr = correo.getText().toString();
            com = coment.getText().toString();
            tel = telef.getText().toString();
            asun = asunto.getText().toString();
            if(!nom.isEmpty() && !corr.isEmpty() && !asun.isEmpty() && !com.isEmpty()){
                enviarComentario();
            }
            else {
                Toast.makeText(this, "Faltan campos por llenar",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void enviarComentario(){
        StringRequest request = new StringRequest(Request.Method.POST, Constant.AYUDANOS_SIN_LOGIN, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Intent intent = new Intent(Ayudanos2.this, Inicio_invitado.class);
                    startActivity(intent);
                    finish();
                    Toast.makeText(this, "Gracias por tu comentario.",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
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
                map.put("name",nombre.getText().toString());
                map.put("email",correo.getText().toString());
                map.put("comentario",coment.getText().toString());
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Ayudanos2.this);
        queue.add(request);
    }
}
