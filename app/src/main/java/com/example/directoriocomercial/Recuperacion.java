package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
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

public class Recuperacion extends AppCompatActivity implements View.OnClickListener {

    Button enviar;
    EditText edtCorreo;
    String correo;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recuperacion);

        enviar = (Button)findViewById(R.id.btn_recuperar);
        edtCorreo = (EditText)findViewById(R.id.edt_correoRecuperar);
        enviar.setOnClickListener(this);
        dialog = new ProgressDialog(Recuperacion.this);
        dialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_recuperar){
            correo = edtCorreo.getText().toString();
            if(!correo.isEmpty()){
                //Verificar que exista el correo en la bd
                //Si existe enviarle el correo
            }
            else {
                Toast.makeText(this, "Ingresa tu correo.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void verificarCorreo(){
        dialog.setMessage("Verificando");
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST, Constant.USERS, response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){

                }
                else{
                    Toast.makeText(this, "Correo incorrectos.",Toast.LENGTH_LONG).show();
                }
            }
            catch (JSONException e){
                Toast.makeText(this, "Sin conexión a Internet.\nIntentelo más tarde.",Toast.LENGTH_LONG).show();
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
                map.put("email",correo);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Recuperacion.this);
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Recuperacion.this, Login.class);
        startActivity(intent);
        finish();
    }
}
