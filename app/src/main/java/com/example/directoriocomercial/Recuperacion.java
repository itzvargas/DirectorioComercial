package com.example.directoriocomercial;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Recuperacion extends AppCompatActivity implements View.OnClickListener {

    Button enviar;
    EditText edtCorreo;
    String correo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_recuperacion);

        enviar = (Button)findViewById(R.id.btn_recuperar);
        edtCorreo = (EditText)findViewById(R.id.edt_correoRecuperar);
        enviar.setOnClickListener(this);
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
}
