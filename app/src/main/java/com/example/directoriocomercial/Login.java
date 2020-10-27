package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import bd.AyudanteBD;

public class Login extends AppCompatActivity implements View.OnClickListener {

    EditText usuario,contra;
    Button login,invitado;
    TextView recuperar,suscribirte;
    String user,pass;
    AyudanteBD aBD;
    SQLiteDatabase db=null;

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

        try{
            aBD=new AyudanteBD(this,"Directorio",null,1);
        }//try
        catch (Exception e) {}
    }

    @Override
    public void onClick(View v) {
        String act = "activo";
        int id = 1;
        user = usuario.getText().toString();
        pass = contra.getText().toString();
        Intent intent;
        Bundle bolsa = new Bundle();
        switch (v.getId()) {
            case R.id.btn_login:
                if(!user.isEmpty() && !pass.isEmpty()) {
                    bolsa.putString("USER",user);
                    bolsa.putString("PASS",pass);
                    boolean verif = verificacion();
                    if(verif == true) {
                        try {
                            aBD = new AyudanteBD(this, "Directorio", null, 1);
                            db = aBD.getWritableDatabase();
                            if (db != null) {
                                db.execSQL("INSERT INTO usuarios values (" + id + ",'" + act + "')");
                                db.close();
                                Toast.makeText(this, "Bienvenido", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(this, "Vuelve a intentarlo.", Toast.LENGTH_LONG).show();
                        }//try
                        catch (Exception e) {
                            Toast.makeText(this, "Vuelve a intentarlo.", Toast.LENGTH_LONG).show();
                        }
                        intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtras(bolsa);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(this, "Correo o contraseña incorrectos.", Toast.LENGTH_LONG).show();
                    }
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

    public boolean verificacion(){
        if(user.equals("itzel@gmail.com") && pass.equals("12345")){
            return true;
        }
        else {
            return false;
        }
    }
}
