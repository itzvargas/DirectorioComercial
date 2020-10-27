package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Suscribirme extends AppCompatActivity implements View.OnClickListener {

    EditText nombre,pass,email,telefono,face;
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
        pass = (EditText)findViewById(R.id.edt_contraseñaU);
        email = (EditText)findViewById(R.id.edt_emailU);
        telefono = (EditText)findViewById(R.id.edt_telefonoU);
        face = (EditText)findViewById(R.id.edt_faceU);
        suscr = (Button)findViewById(R.id.btn_suscribirme);

        suscr.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        String nom,passw,em,telef,fac;
        nom = nombre.getText().toString();
        passw = pass.getText().toString();
        em = email.getText().toString();
        telef = telefono.getText().toString();
        fac = face.getText().toString();

        if(v.getId() == R.id.btn_suscribirme){
            if(!nom.isEmpty() && !passw.isEmpty() && !em.isEmpty()){
                //Enviar los datos a la BD y notificar al usuario que le llegarán los datos de su sesión por correo

                Toast.makeText(this, "Ahora puedes iniciar sesión y disfrutar de los beneficios.",Toast.LENGTH_LONG).show();
                //Redireccion
                if(ruta.equals("login")){
                    intent = new Intent(Suscribirme.this, Login.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    intent = new Intent(Suscribirme.this, Inicio_invitado.class);
                    startActivity(intent);
                    finish();
                }
            }
            else {
                Toast.makeText(this, "Faltan campos por llenar",Toast.LENGTH_LONG).show();
            }
        }
    }
}
