package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Ayudanos2 extends AppCompatActivity implements View.OnClickListener {

    EditText nombre,correo,coment;
    Button enviar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayudanos2);
        setTitle(getString(R.string.menu_ayudanos));
        nombre = (EditText)findViewById(R.id.edt_nom);
        correo = (EditText)findViewById(R.id.edt_correo);
        coment = (EditText)findViewById(R.id.edt_comentario);
        enviar = (Button)findViewById(R.id.btn_enviar_comentario);
        enviar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String nom, corr, com;
        Intent intent;
        if(v.getId() == R.id.btn_enviar_comentario){
            nom = nombre.getText().toString();
            corr = correo.getText().toString();
            com = coment.getText().toString();
            if(!nom.isEmpty() && !corr.isEmpty() && !com.isEmpty()){
                intent = new Intent(Ayudanos2.this, Inicio_invitado.class);
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(this, "Faltan campos por llenar",Toast.LENGTH_LONG).show();
            }
        }
    }
}
