package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText usuario,contra;
    Button login,invitado;
    TextView recuperar,suscribirte;
    String user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        usuario = (EditText)findViewById(R.id.edt_usuario);
        contra = (EditText)findViewById(R.id.edt_contrasena);
        login = (Button)findViewById(R.id.btn_login);
        invitado = (Button)findViewById(R.id.btn_invitado);
        recuperar = (TextView)findViewById(R.id.txt_recuperar);
        suscribirte = (TextView)findViewById(R.id.txt_suscribirse);

        login.setOnClickListener(this);
        invitado.setOnClickListener(this);
        recuperar.setOnClickListener(this);
        suscribirte.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        user = usuario.getText().toString();
        pass = contra.getText().toString();
        Intent intent;
        Bundle bolsa = new Bundle();
        switch (v.getId()) {
            case R.id.btn_login:
                if(!user.isEmpty() && !pass.isEmpty()) {
                    bolsa.putString("USER",user);
                    bolsa.putString("PASS",pass);
                    intent = new Intent(Login.this, MainActivity.class);
                    intent.putExtras(bolsa);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(this, "Faltan campos por llenar",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.btn_invitado:
                intent = new Intent(Login.this, Inicio_invitado.class);
                startActivity(intent);
                break;
            case R.id.txt_recuperar:
                break;
            case R.id.txt_suscribirse:
                intent = new Intent(Login.this, Suscribirme.class);
                bolsa.putString("RUTA","login");
                intent.putExtras(bolsa);
                startActivity(intent);
                break;
        }
    }
}
