package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Suscribirme extends AppCompatActivity implements View.OnClickListener {

    EditText nombre,fecha,email,telefono,face;
    Button suscr;
    String ruta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscribirme);
        setTitle(getString(R.string.menu_suscribirme));

        Bundle bolsaR = getIntent().getExtras();
        ruta = bolsaR.getString("RUTA");

        nombre = (EditText)findViewById(R.id.edt_nombreU);
        fecha = (EditText)findViewById(R.id.edt_fechaNU);
        email = (EditText)findViewById(R.id.edt_emailU);
        telefono = (EditText)findViewById(R.id.edt_telefonoU);
        face = (EditText)findViewById(R.id.edt_faceU);
        suscr = (Button)findViewById(R.id.btn_suscribirme);

        suscr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String nom,fec,em,telef,fac;
        nom = nombre.getText().toString();
        fec = fecha.getText().toString();
        em = email.getText().toString();
        telef = telefono.getText().toString();
        fac = face.getText().toString();

        if(v.getId() == R.id.btn_suscribirme){
            if(!nom.isEmpty() && !fec.isEmpty() && !em.isEmpty() && !telef.isEmpty()){

            }
            else {
                Toast.makeText(this, "Faltan campos por llenar",Toast.LENGTH_LONG).show();
            }
        }
    }
}
