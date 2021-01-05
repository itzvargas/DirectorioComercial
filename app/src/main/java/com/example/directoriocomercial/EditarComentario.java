package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
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

public class EditarComentario extends AppCompatActivity implements View.OnClickListener {

    RatingBar total;
    EditText coment;
    Button editar;
    int idNegocio, idUsuario, bar;
    String comentario, token;
    private SharedPreferences userPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_comentario);
        setTitle("Editar comentario");

        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        idNegocio = getIntent().getIntExtra("ID",0);
        idUsuario = userPref.getInt("id", 0);
        token = userPref.getString("token","");
        bar = getIntent().getIntExtra("Bar",0);
        comentario = getIntent().getStringExtra("Comentario");

        total = (RatingBar)findViewById(R.id.rbar_comentarioEditar);
        coment = (EditText)findViewById(R.id.edt_comentarioEditar);
        editar = (Button)findViewById(R.id.btn_editarComentario);

        total.setRating(bar);
        coment.setText(comentario);
        editar.setOnClickListener(this);

    }

    public void reedireccion(){
        Intent intent = new Intent(EditarComentario.this, Negocio.class);
        intent.putExtra("ID",idNegocio);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        reedireccion();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_editarComentario){
            if(!coment.getText().toString().isEmpty()) {
                //Método para editar comentario
                editarComentario();
            }
            else {
                Toast.makeText(this, "Escribe tu comentario.",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void editarComentario(){
        StringRequest request = new StringRequest(Request.Method.PUT, Constant.EDITAR_COMENTARIO+idNegocio+"/update", response -> {
            try {
                JSONObject object =  new JSONObject(response);
                if(object.getBoolean("success")){
                    Toast.makeText(this, "Se ha editado tu comentario.",Toast.LENGTH_LONG).show();
                    reedireccion();
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
                map.put("contenido",coment.getText().toString());
                map.put("valoracion",total.getRating()+"");
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(EditarComentario.this);
        queue.add(request);
    }
}
